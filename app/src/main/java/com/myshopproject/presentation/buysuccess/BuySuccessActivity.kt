package com.myshopproject.presentation.buysuccess

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.R
import com.myshopproject.data.utils.toIDRPrice
import com.myshopproject.databinding.ActivityBuySuccessBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.main.MainActivity
import com.myshopproject.utils.Constants.LIST_PRODUCT_ID
import com.myshopproject.utils.Constants.PAYMENT_ID_INTENT
import com.myshopproject.utils.Constants.PAYMENT_NAME_INTENT
import com.myshopproject.utils.Constants.PRICE_INTENT
import com.myshopproject.utils.Constants.PRODUCT_ID
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class BuySuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuySuccessBinding

    private val viewModel by viewModels<BuySuccessViewModel>()

    private var productId = 0
    private var listProductId: List<String>? = null

    private var totalPrice = 0
    private var paymentId = ""
    private var paymentName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getIntExtra(PRODUCT_ID, 0)
        listProductId = intent.getStringArrayListExtra(LIST_PRODUCT_ID)
        paymentId = intent.getStringExtra(PAYMENT_ID_INTENT).toString()
        paymentName = intent.getStringExtra(PAYMENT_NAME_INTENT).toString()
        totalPrice = intent.getIntExtra(PRICE_INTENT, 0)

        initObserver()
    }

    private fun initObserver() {
        when (paymentId) {
            "va_bca" -> {
                binding.ivPaymentImageSuccess.load(R.drawable.img_bca)
            }
            "va_mandiri" -> {
                binding.ivPaymentImageSuccess.load(R.drawable.img_mandiri)
            }
            "va_bri" -> {
                binding.ivPaymentImageSuccess.load(R.drawable.img_bri)
            }
            "va_bni" -> {
                binding.ivPaymentImageSuccess.load(R.drawable.img_bni)
            }
            "va_btn" -> {
                binding.ivPaymentImageSuccess.load(R.drawable.img_btn)
            }
            "va_danamon" -> {
                binding.ivPaymentImageSuccess.load(R.drawable.img_danamon)
            }
            "ewallet_gopay" -> {
                binding.ivPaymentImageSuccess.load(R.drawable.img_gopay)
            }
            "ewallet_ovo" -> {
                binding.ivPaymentImageSuccess.load(R.drawable.img_ovo)
            }
            "ewallet_dana" -> {
                binding.ivPaymentImageSuccess.load(R.drawable.img_dana)
            }
        }
        binding.tvPaymentNameSuccess.text = paymentName
        binding.tvTotalPriceSuccess.text = totalPrice.toString().toIDRPrice()
        binding.btnSubmitBuySccss.setOnClickListener {
            val rate = binding.rbBuySccss.rating.toString()
            if (productId != 0) {
                viewModel.updateRate(id = productId, updateRate = rate)
                viewModel.state.observe(this@BuySuccessActivity) { response ->
                    when (response) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            val intent = Intent(this@BuySuccessActivity, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                        is Resource.Error -> {
                            try {
                                val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                                val gson = Gson()
                                val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                                val errorResponse = gson.fromJson(jsonObject, com.myshopproject.data.source.remote.dto.ErrorResponseDTO::class.java)

                                Toast.makeText(this@BuySuccessActivity, "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                            } catch (_: Exception) {}
                        }
                    }
                }
            } else {
                listProductId?.let { listProductIds ->
                    for (i in listProductIds.indices) {
                        viewModel.updateRate(id = listProductIds[i].toInt(), updateRate = rate)
                        viewModel.state.observe(this@BuySuccessActivity) { response ->
                            when (response) {
                                is Resource.Loading -> {

                                }
                                is Resource.Success -> {
                                    finishAffinity()
                                }
                                is Resource.Error -> {
                                    Toast.makeText(this@BuySuccessActivity, response.message, Toast.LENGTH_SHORT).show()
                                    try {
                                        val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                                        val gson = Gson()
                                        val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                                        val errorResponse = gson.fromJson(jsonObject, com.myshopproject.data.source.remote.dto.ErrorResponseDTO::class.java)

                                        Toast.makeText(this@BuySuccessActivity, "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                                    } catch (_: Exception) {}
                                }
                            }
                        }
                    }
                    val intent = Intent(this@BuySuccessActivity, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }
}