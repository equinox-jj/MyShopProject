package com.myshopproject.presentation.payment

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
import com.myshopproject.presentation.payment.adapter.PaymentHeaderAdapter

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var remoteTest: FirebaseRemoteConfig

    private var adapterPayment: PaymentHeaderAdapter? = null

    private var dataRemoteConfig: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        remoteTest = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }

        remoteTest.setConfigSettingsAsync(configSettings)
        remoteTest.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dataRemoteConfig = remoteTest.getString("payment_json")
                    val dataList = Gson().fromJson<List<PaymentTypeResponse>>(dataRemoteConfig, object : TypeToken<List<PaymentTypeResponse>>() {}.type)

                    adapterPayment = PaymentHeaderAdapter()
                    binding.rvItemHeaderPayment.adapter = adapterPayment
                    binding.rvItemHeaderPayment.setHasFixedSize(true)
                    adapterPayment?.submitData(dataList)
                } else { }
            }
    }
}