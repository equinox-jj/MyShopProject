package com.myshopproject.presentation.buysuccess

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.databinding.ActivityBuySuccessBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.main.MainActivity
import com.myshopproject.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class BuySuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuySuccessBinding

    private val viewModel by viewModels<BuySuccessViewModel>()

    private var productId = 0
    private var listProductId: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getIntExtra(Constants.PRODUCT_ID, 0)
        listProductId = intent.getStringArrayListExtra(Constants.LIST_PRODUCT_ID)

        initObserver()
    }

    private fun initObserver() {
        binding.btnSubmitBuySccss.setOnClickListener {
            val rate = binding.rbBuySccss.rating.toString()
            if (productId != 0) {
                viewModel.updateRate(id = productId, updateRate = rate)
                viewModel.state.observe(this@BuySuccessActivity) { response ->
                    when (response) {
                        is Resource.Loading -> {

                        }
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
                            } catch (e: Exception) { }
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
                                    try {
                                        val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                                        val gson = Gson()
                                        val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                                        val errorResponse = gson.fromJson(jsonObject, com.myshopproject.data.source.remote.dto.ErrorResponseDTO::class.java)

                                        Toast.makeText(this@BuySuccessActivity, "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {}
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