package com.myshopproject.presentation.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.myshopproject.R
import com.myshopproject.databinding.ActivityDetailBinding
import com.myshopproject.domain.entities.DetailProductData
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.detail.adapter.ImageSliderAdapter
import com.myshopproject.presentation.detail.bottomsheet.DetailBottomSheet
import com.myshopproject.utils.Constants.PRODUCT_ID
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import com.myshopproject.utils.toIDRPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var indicator: CircleIndicator3

    private val viewModel by viewModels<DetailViewModel>()

    private var productId: Int = 0
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productId = intent.getIntExtra(PRODUCT_ID, 0)

        initObserver()
        initDataStore()
    }

    private fun initDataStore() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUserId.collect {
                    userId = it
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.getProductList(productId, userId)
        viewModel.state.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.shimmerDetail.root.setVisibilityVisible()
                    binding.nestedScrollDtl.setVisibilityGone()
                }
                is Resource.Success -> {
                    binding.shimmerDetail.root.setVisibilityGone()
                    binding.nestedScrollDtl.setVisibilityVisible()
                    response.data?.success?.data?.let { initView(it) }
                    response.data?.success?.data?.let { setupListener(it) }
                }
                is Resource.Error -> {
                    binding.shimmerDetail.root.setVisibilityGone()
                    binding.nestedScrollDtl.setVisibilityGone()
                }
            }
        }
    }

    private fun initView(data: DetailProductData) {
        binding.apply {
            imageSliderAdapter = ImageSliderAdapter(data.imageProduct)
            vpImageSliderProductDtl.adapter = imageSliderAdapter
            indicatorSlider.setViewPager(vpImageSliderProductDtl)
            tvNameProductDtl.isSelected = true
            tvNameProductDtl.text = data.nameProduct
            tvPriceProductDtl.text = data.harga.toIDRPrice()
            rbProductDtl.rating = data.rate.toFloat()
            tvStockProductDtl.text = "Stock     :${data.stock}"
            tvSizeProductDtl.text = "Size       :${data.size}"
            tvWeightProductDtl.text = "Weight   :${data.weight}"
            tvTypeProductDtl.text = "Type       :${data.type}"
            tvDescProductDtl.text = data.desc
            if (data.isFavorite) {
                binding.ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                binding.ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_outlined)
            }
        }
    }

    private fun setupListener(data: DetailProductData) {
        binding.apply {
            btnDtlTrolley.setOnClickListener {
                val bottSheet = DetailBottomSheet(data)
                bottSheet.show(supportFragmentManager, DetailActivity::class.java.simpleName)
            }
        }
    }
}