package com.myshopproject.presentation.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myshopproject.databinding.ActivityPaymentBinding
import com.myshopproject.domain.entities.PaymentTypeResponse
import com.myshopproject.presentation.detail.DetailActivity
import com.myshopproject.presentation.payment.adapter.PaymentHeaderAdapter
import com.myshopproject.presentation.trolley.TrolleyActivity
import com.myshopproject.utils.Constants.FB_CONFIG_KEY
import com.myshopproject.utils.Constants.PAYMENT_DATA_INTENT
import com.myshopproject.utils.Constants.PRODUCT_ID_INTENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var remoteTest: FirebaseRemoteConfig

    private var adapterPayment: PaymentHeaderAdapter? = null

    private var productId = 0
    private var dataRemoteConfig = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idProduct = intent.getIntExtra(PRODUCT_ID_INTENT, 0)
        productId = idProduct
        remoteTest = Firebase.remoteConfig

        setupToolbar()
        remoteConfigSetup()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun remoteConfigSetup() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }

        remoteTest.setConfigSettingsAsync(configSettings)
        remoteTest.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dataRemoteConfig = remoteTest.getString(FB_CONFIG_KEY)
                    val dataList = Gson().fromJson<List<PaymentTypeResponse>>(dataRemoteConfig, object : TypeToken<List<PaymentTypeResponse>>() {}.type)

                    adapterPayment = PaymentHeaderAdapter(
                        onBodyClick = { data ->
                            if (productId == 0) {
                                val intent = Intent(this@PaymentActivity, TrolleyActivity::class.java)
                                intent.putExtra(PAYMENT_DATA_INTENT, data)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                finish()
                            } else {
                                val intent = Intent(this@PaymentActivity, DetailActivity::class.java)
                                intent.putExtra(PRODUCT_ID_INTENT, productId)
                                intent.putExtra(PAYMENT_DATA_INTENT, data)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }
                    )
                    binding.rvItemHeaderPayment.adapter = adapterPayment
                    binding.rvItemHeaderPayment.setHasFixedSize(true)
                    adapterPayment?.submitData(dataList.sortedBy { it.order })
                }
            }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarPayment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}