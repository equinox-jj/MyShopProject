package com.myshopproject.presentation.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.myshopproject.databinding.ActivityDetailBinding
import com.myshopproject.domain.entities.DetailProductData
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.detail.bottomsheet.DetailBottomSheet
import com.myshopproject.utils.Constants.PRODUCT_ID
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import com.myshopproject.utils.toIDRPrice
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
        productId = intent.getIntExtra(PRODUCT_ID, 0)

        initObserver()
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            btnDtlBuy.setOnClickListener {
                val bottSheet = DetailBottomSheet()
                bottSheet.show(supportFragmentManager, DetailActivity::class.java.simpleName)
            }
        }
    }

    private fun initObserver() {
        viewModel.getProductList(productId)
        viewModel.state.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.shimmerDetail.root.setVisibilityVisible()
                }
                is Resource.Success -> {
                    binding.shimmerDetail.root.setVisibilityGone()
                    response.data?.success?.data?.let { initView(it) }
                }
                is Resource.Error -> {
                    binding.shimmerDetail.root.setVisibilityGone()
                }
            }
        }
    }

    private fun initView(data: DetailProductData) {
        binding.apply {
            ivProductDtl.load(data.imageProduct)
            data.imageProduct.forEach { tvCarouselProductDtl.text = it.titleProduct }
            rbProductDtl.rating = data.rate.toFloat()
            tvNameProductDtl.text = data.nameProduct
            tvPriceProductDtl.text = data.harga.toIDRPrice()
            tvStockProductDtl.text = "Stock : ${data.stock}"
            tvSizeProductDtl.text = "Size : ${data.size}"
            tvWeightProductDtl.text = "Weight : ${data.weight}"
            tvTypeProductDtl.text = "Type : ${data.type}"
            tvDescProductDtl.text = data.desc
        }
    }
}