package com.myshopproject.presentation.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.myshopproject.databinding.ActivityDetailBinding
import com.myshopproject.domain.entities.DetailProductData
import com.myshopproject.domain.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel>()
    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productId = intent.getIntExtra("product_id", 0)

        initObserver()
    }

    private fun initObserver() {
        viewModel.getProductList(productId)
        viewModel.state.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    response.data?.success?.data?.let { initView(it) }
                }
                is Resource.Error -> {

                }
            }
        }
    }

    private fun initView(data: DetailProductData) {
        binding.apply {
            tvNameProductDtl.text = data.nameProduct
        }
    }
}