package com.myshopproject.presentation.trolley

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.myshopproject.R
import com.myshopproject.data.utils.toIDRPrice
import com.myshopproject.databinding.ActivityTrolleyBinding
import com.myshopproject.domain.entities.CartDataDomain
import com.myshopproject.domain.entities.PaymentResult
import com.myshopproject.domain.entities.UpdateStockItem
import com.myshopproject.domain.repository.FirebaseAnalyticsRepository
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.buysuccess.BuySuccessActivity
import com.myshopproject.presentation.payment.PaymentActivity
import com.myshopproject.presentation.trolley.adapter.TrolleyAdapter
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.presentation.viewmodel.LocalViewModel
import com.myshopproject.utils.Constants.LIST_PRODUCT_ID
import com.myshopproject.utils.Constants.PAYMENT_DATA_INTENT
import com.myshopproject.utils.Constants.PAYMENT_ID_INTENT
import com.myshopproject.utils.Constants.PAYMENT_NAME_INTENT
import com.myshopproject.utils.Constants.PRICE_INTENT
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrolleyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrolleyBinding

    private var trolleyAdapter: TrolleyAdapter? = null

    private val viewModel by viewModels<TrolleyViewModel>()
    private val localViewModel by viewModels<LocalViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    private var paymentParcel: PaymentResult? = null
    private var userId = ""
    private var totalPrice = 0

    @Inject
    lateinit var analyticRepository: FirebaseAnalyticsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrolleyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentParcel = intent.getParcelableExtra(PAYMENT_DATA_INTENT)

        setupToolbar()
        initObserver()
        initRecyclerView()
        setupListener()
        initDataStore()
        postProductTrolley()
    }

    override fun onResume() {
        super.onResume()
        analyticRepository.onTrolleyLoadScreen(this@TrolleyActivity.javaClass.simpleName)
    }

    override fun onSupportNavigateUp(): Boolean {
        analyticRepository.onClickButtonBackTrolley()
        finish()
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarTrolley)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initDataStore() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userId = prefViewModel.getUserId.first().toString()
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                localViewModel.getAllProduct().collect { result ->
                    if (result.isNotEmpty()) {
                        var priceTotal = 0
                        val filterResult = result.filter { it.isChecked }

                        filterResult.forEach {
                            priceTotal = priceTotal.plus(it.itemTotalPrice ?: 0)
                        }

                        binding.tvProductPriceTrlly.text = priceTotal.toString().toIDRPrice()
                        binding.cbTrolley.isChecked = result.size == filterResult.size
                        trolleyAdapter?.submitData(result)
                        totalPrice = priceTotal

                        binding.cbTrolley.show()
                        binding.rvTrolley.show()
                        binding.bottomAppBarTrolley.show()
                        binding.emptyStateTrolley.root.hide()
                    } else {
                        binding.emptyStateTrolley.root.show()
                        binding.cbTrolley.hide()
                        binding.rvTrolley.hide()
                        binding.bottomAppBarTrolley.hide()
                    }
                }
            }
        }
    }

    private fun setupListener() {
        binding.cbTrolley.setOnClickListener {
            if (binding.cbTrolley.isChecked) {
                localViewModel.updateProductIsCheckedAll(true)
            } else {
                localViewModel.updateProductIsCheckedAll(false)
            }
        }
    }

    private fun postProductTrolley() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                localViewModel.getAllCheckedProduct().collect { result ->
                    val dataStockItems = arrayListOf<UpdateStockItem>()
                    val listOfProductId = arrayListOf<String>()
                    for (i in result.indices) {
                        dataStockItems.add(UpdateStockItem(result[i].id.toString(), result[i].quantity))
                        listOfProductId.add(result[i].id.toString())
                    }

                    when {
                        result.isEmpty() -> {
                            binding.btnBuyTrlly.setOnClickListener {
                                analyticRepository.onClickButtonBuyTrolley()
                                Toast.makeText(this@TrolleyActivity, "You haven't select any product yet", Toast.LENGTH_SHORT).show()
                            }
                        }
                        paymentParcel == null -> {
                            binding.btnBuyTrlly.setOnClickListener {
                                analyticRepository.onClickButtonBuyTrolley()
                                val intent = Intent(this@TrolleyActivity, PaymentActivity::class.java)
                                startActivity(intent)
                            }
                            binding.llTrllyPayment.hide()
                        }
                        else -> {
                            binding.llTrllyPayment.setOnClickListener {
                                analyticRepository.onClickIconBankTrolley(paymentParcel?.name.toString())
                                val intent = Intent(this@TrolleyActivity, PaymentActivity::class.java)
                                startActivity(intent)
                            }
                            binding.tvTrllyPaymentName.text = paymentParcel?.name
                            binding.llTrllyPayment.show()
                            when (paymentParcel?.id) {
                                "va_bca" -> {
                                    binding.ivTrllyPaymentImage.load(R.drawable.img_bca)
                                }
                                "va_mandiri" -> {
                                    binding.ivTrllyPaymentImage.load(R.drawable.img_mandiri)
                                }
                                "va_bri" -> {
                                    binding.ivTrllyPaymentImage.load(R.drawable.img_bri)
                                }
                                "va_bni" -> {
                                    binding.ivTrllyPaymentImage.load(R.drawable.img_bni)
                                }
                                "va_btn" -> {
                                    binding.ivTrllyPaymentImage.load(R.drawable.img_btn)
                                }
                                "va_danamon" -> {
                                    binding.ivTrllyPaymentImage.load(R.drawable.img_danamon)
                                }
                                "ewallet_gopay" -> {
                                    binding.ivTrllyPaymentImage.load(R.drawable.img_gopay)
                                }
                                "ewallet_ovo" -> {
                                    binding.ivTrllyPaymentImage.load(R.drawable.img_ovo)
                                }
                                "ewallet_dana" -> {
                                    binding.ivTrllyPaymentImage.load(R.drawable.img_dana)
                                }
                            }
                            binding.btnBuyTrlly.setOnClickListener {
                                viewModel.updateStock(userId, dataStockItems)
                                viewModel.updateStockState.observe(this@TrolleyActivity) { response ->
                                    when (response) {
                                        is Resource.Loading -> {}
                                        is Resource.Success -> {
                                            analyticRepository.onClickButtonBuyNowWithPaymentTrolley(totalPrice.toDouble(), paymentParcel?.name.toString())
                                            dataStockItems.forEach { localViewModel.deleteProductByIdFromTrolley(it.id_product?.toInt()) }
                                            val intent = Intent(this@TrolleyActivity, BuySuccessActivity::class.java)
                                            intent.putExtra(LIST_PRODUCT_ID, listOfProductId)
                                            intent.putExtra(PRICE_INTENT, totalPrice)
                                            intent.putExtra(PAYMENT_ID_INTENT, paymentParcel?.id)
                                            intent.putExtra(PAYMENT_NAME_INTENT, paymentParcel?.name)
                                            startActivity(intent)
                                        }
                                        is Resource.Error -> {}
                                    }
                                }
                                analyticRepository.onClickButtonBuyTrolley()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            trolleyAdapter = TrolleyAdapter(
                onDeleteItem = { setDialogDeleteItem(it) },
                onAddQuantity = {
                    val productId = it.id
                    val quantity = it.quantity
                    val price = it.price
                    localViewModel.updateProductData(
                        quantity = quantity?.plus(1),
                        itemTotalPrice = price?.replace(Regex("\\D"), "")?.toInt()?.times(quantity.toString().toInt().plus(1)),
                        id = productId
                    )
                    analyticRepository.onClickQuantityTrolley(quantity = "+", total = quantity?.plus(1) ?: 0, productId = productId ?: 0, it.nameProduct.toString())
                },
                onMinQuantity = {
                    val productId = it.id
                    val quantity = it.quantity
                    val price = it.price
                    localViewModel.updateProductData(
                        quantity = quantity?.minus(1),
                        itemTotalPrice = price?.replace(Regex("\\D"), "")?.toInt()?.times(quantity.toString().toInt().minus(1)),
                        id = productId
                    )
                    analyticRepository.onClickQuantityTrolley(quantity = "-", total = quantity?.minus(1) ?: 0, productId = productId ?: 0, it.nameProduct.toString())
                },
                onCheckedItem = {
                    val productId = it.id
                    val isChecked = !it.isChecked
                    binding.btnBuyTrlly.isClickable = false
                    localViewModel.updateProductIsCheckedById(isChecked, productId)
                    analyticRepository.onCheckBoxTrolley(productId = productId ?: 0, productName = it.nameProduct.toString())
                },
            )
            binding.rvTrolley.adapter = trolleyAdapter
            binding.rvTrolley.setHasFixedSize(true)
//            binding.rvTrolley.itemAnimator = null
        }
    }

    private fun setDialogDeleteItem(data: CartDataDomain) {
        AlertDialog.Builder(this@TrolleyActivity)
            .setTitle("Remove item from trolley")
            .setMessage("Are you sure you want to remove this item?")
            .setPositiveButton("Ok") { _, _ ->
                localViewModel.deleteProductByIdFromTrolley(data.id)
                analyticRepository.onClickDeleteTrolley(productId = data.id ?: 0, data.nameProduct.toString())
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }
}