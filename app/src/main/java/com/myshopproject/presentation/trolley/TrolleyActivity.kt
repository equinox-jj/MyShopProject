package com.myshopproject.presentation.trolley

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myshopproject.databinding.ActivityTrolleyBinding

class TrolleyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrolleyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrolleyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}