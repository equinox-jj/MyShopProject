package com.myshopproject.presentation.buysuccess

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myshopproject.databinding.ActivityBuySuccessBinding

class BuySuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}