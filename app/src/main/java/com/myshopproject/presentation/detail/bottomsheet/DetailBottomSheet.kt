package com.myshopproject.presentation.detail.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.R
import com.myshopproject.data.source.remote.dto.ErrorResponseDTO
import com.myshopproject.databinding.FragmentBottomSheetDetailBinding
import com.myshopproject.domain.entities.DetailProductData
import com.myshopproject.domain.entities.PaymentResult
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.buysuccess.BuySuccessActivity
import com.myshopproject.presentation.payment.PaymentActivity
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.utils.Constants.PAYMENT_ID_INTENT
import com.myshopproject.utils.Constants.PAYMENT_NAME_INTENT
import com.myshopproject.utils.Constants.PRICE_INTENT
import com.myshopproject.utils.Constants.PRODUCT_ID
import com.myshopproject.utils.Constants.PRODUCT_ID_INTENT
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import com.myshopproject.utils.toIDRPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class DetailBottomSheet(
    private val data: DetailProductData,
    private val paymentData: PaymentResult?
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailBottomSheetViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    private var userId = ""
    private var totalPrice = 0
    private var quantity = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetDetailBinding.inflate(inflater, container, false)

        initObserver()
        initView()
        setupListener()
        initDataStore()

        return binding.root
    }

    private fun initDataStore() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userId = prefViewModel.getUserId.first().toString()
            }
        }
    }

    private fun initView() {
        binding.apply {
            ivProductBottSht.load(data.image)
            tvProductPriceBottSht.text = data.harga.toIDRPrice()
            (resources.getString(R.string.buy_now) + data.harga.toIDRPrice()).also { btnBuyNowBottSheet.text = it }
            if (data.stock == 1) tvStockProductBottSht.text = resources.getString(R.string.out_of_stock)
            else tvStockProductBottSht.text = data.stock.toString()
            if (paymentData == null) {
                binding.btnBuyNowBottSheet.setOnClickListener {
                    val intent = Intent(requireContext(), PaymentActivity::class.java)
                    intent.putExtra(PRODUCT_ID_INTENT, data.id)
                    requireContext().startActivity(intent)
                }
                binding.llBottPayment.hide()
            } else {
                binding.llBottPayment.setOnClickListener {
                    val intent = Intent(requireContext(), PaymentActivity::class.java)
                    intent.putExtra(PRODUCT_ID_INTENT, data.id)
                    requireContext().startActivity(intent)
                }
                binding.btnBuyNowBottSheet.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            updateStock(userId, data.id.toString(), quantity)
                        }
                    }
                }
                binding.tvBottPaymentName.text = paymentData.name
                when (paymentData.id) {
                    "va_bca" -> {
                        ivBottPaymentImage.load(R.drawable.img_bca)
                    }
                    "va_mandiri" -> {
                        ivBottPaymentImage.load(R.drawable.img_mandiri)
                    }
                    "va_bri" -> {
                        ivBottPaymentImage.load(R.drawable.img_bri)
                    }
                    "va_bni" -> {
                        ivBottPaymentImage.load(R.drawable.img_bni)
                    }
                    "va_btn" -> {
                        ivBottPaymentImage.load(R.drawable.img_btn)
                    }
                    "va_danamon" -> {
                        ivBottPaymentImage.load(R.drawable.img_danamon)
                    }
                    "ewallet_gopay" -> {
                        ivBottPaymentImage.load(R.drawable.img_gopay)
                    }
                    "ewallet_ovo" -> {
                        ivBottPaymentImage.load(R.drawable.img_ovo)
                    }
                    "ewallet_dana" -> {
                        ivBottPaymentImage.load(R.drawable.img_dana)
                    }
                }
                binding.llBottPayment.show()
            }
        }
    }

    private fun initObserver() {
        viewModel.quantity.observe(viewLifecycleOwner) {
            quantity = it
            binding.tvQuantityBottSheet.text = it.toString()
            when (it) {
                data.stock -> {
                    binding.btnIncreaseBottSheet.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_round_grey)
                    binding.btnDecreaseBottSheet.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_round_black)
                }
                1 -> {
                    binding.btnIncreaseBottSheet.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_round_black)
                    binding.btnDecreaseBottSheet.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_round_grey)
                }
                else -> {
                    binding.btnIncreaseBottSheet.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_round_black)
                    binding.btnDecreaseBottSheet.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_round_black)
                }
            }
        }

        viewModel.setPrice(data.harga.toInt())

        viewModel.price.observe(viewLifecycleOwner) {
            totalPrice = it
            binding.tvProductPriceBottSht.text = it.toString().toIDRPrice()
        }
    }

    private fun setupListener() {
        binding.btnIncreaseBottSheet.setOnClickListener {
            viewModel.increaseQuantity(data.stock)
            val sum = binding.tvQuantityBottSheet.text.toString()
            val total = (sum.toInt() * data.harga.toInt())
            (resources.getString(R.string.buy_now) + total.toString().toIDRPrice()).also { binding.btnBuyNowBottSheet.text = it }
        }
        binding.btnDecreaseBottSheet.setOnClickListener {
            viewModel.decreaseQuantity()
            val sum = binding.tvQuantityBottSheet.text.toString()
            val total = (sum.toInt() * data.harga.toInt())
            (resources.getString(R.string.buy_now) + total.toString().toIDRPrice()).also { binding.btnBuyNowBottSheet.text = it }
        }
    }

    private fun updateStock(userId: String, idProduct: String, stock: Int) {
        viewModel.updateStock(userId, idProduct, stock)
        viewModel.updateStockState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val intent = Intent(context, BuySuccessActivity::class.java)
                    intent.putExtra(PRODUCT_ID, data.id)
                    intent.putExtra(PRICE_INTENT, totalPrice)
                    intent.putExtra(PAYMENT_ID_INTENT, paymentData?.id)
                    intent.putExtra(PAYMENT_NAME_INTENT, paymentData?.name)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    try {
                        val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                        val gson = Gson()
                        val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                        val errorResponse = gson.fromJson(jsonObject, ErrorResponseDTO::class.java)
                        Toast.makeText(requireContext(), "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                    } catch (_: Exception) {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}