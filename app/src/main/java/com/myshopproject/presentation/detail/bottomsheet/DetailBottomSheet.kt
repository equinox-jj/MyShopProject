package com.myshopproject.presentation.detail.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.R
import com.myshopproject.data.source.remote.dto.ErrorResponseDTO
import com.myshopproject.databinding.BottomSheetProductDetailBinding
import com.myshopproject.domain.entities.DetailProductData
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.buysuccess.BuySuccessActivity
import com.myshopproject.utils.Constants
import com.myshopproject.utils.toIDRPrice
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class DetailBottomSheet(private val data: DetailProductData) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetProductDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailBottomSheetViewModel>()
    private var quantity: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetProductDetailBinding.inflate(inflater, container, false)

        initObserver()
        initView()
        setupListener()
        return binding.root
    }

    private fun initView() {
        binding.apply {
            ivProductBottSht.load(data.image)
            tvProductPriceBottSht.text = data.harga.toIDRPrice()
            if (data.stock == 1) tvStockProductBottSht.text = "Out of stock."
            else tvStockProductBottSht.text = data.stock.toString()
            (resources.getString(R.string.buy_now) + data.harga.toIDRPrice()).also { btnBuyNowBottSheet.text = it }
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
            binding.tvProductPriceBottSht.text = it.toString().toIDRPrice()
        }

        binding.btnBuyNowBottSheet.setOnClickListener {
            updateStock(data.id.toString(), quantity)
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

    private fun updateStock(idProduct: String, stock: Int) {
        viewModel.updateStock(idProduct, stock)
        viewModel.updateStockState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val intent = Intent(context, BuySuccessActivity::class.java)
                    intent.putExtra(Constants.PRODUCT_ID, data.id)
                    startActivity(intent)
                    Toast.makeText(requireContext(), response.data?.success?.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    try {
                        val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                        val gson = Gson()
                        val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                        val errorResponse = gson.fromJson(jsonObject, ErrorResponseDTO::class.java)
                        Toast.makeText(requireContext(), "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                    }catch (e: Exception) {
//                        val errors = response.errorCode
//                        Toast.makeText(requireContext(), "$errors", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}