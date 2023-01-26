package com.myshopproject.presentation.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.R
import com.myshopproject.data.source.remote.dto.ErrorResponseDTO
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var imageSliderAdapter: ImageSliderAdapter

    private val viewModel by viewModels<DetailViewModel>()

    private var productId: Int = 0
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productId = intent.getIntExtra(PRODUCT_ID, 0)

        initDataStore()
        initObserver()
    }

    private fun initDataStore() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val userid = viewModel.getUserId.first()
                userId = userid
            }
        }
    }

    private fun initObserver() {
        viewModel.getProductDetail(productId, userId)
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
            tvStockProductDtl.text = data.stock.toString()
            tvSizeProductDtl.text = data.size
            tvWeightProductDtl.text = data.weight
            tvTypeProductDtl.text = data.type
            tvDescProductDtl.text = data.desc
            if (data.isFavorite) {
                binding.ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                binding.ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_outlined)
            }
            binding.ivImageFavProductDtl.setOnClickListener {
                if (data.isFavorite) {
                    removeFavorite()
                } else {
                    addFavorite()
                }
            }
        }
    }

    private fun addFavorite() {
        viewModel.addProductFavorite(productId, userId)
        viewModel.favState.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_filled)
                    Toast.makeText(this, "Success add to favorite", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                    val gson = Gson()
                    val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                    val errorResponse = gson.fromJson(jsonObject, ErrorResponseDTO::class.java)

                    Toast.makeText(this@DetailActivity, "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun removeFavorite() {
        viewModel.removeProductFavorite(productId, userId)
        viewModel.unFavState.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_outlined)
                    Toast.makeText(this, "Success remove from favorite", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                    val gson = Gson()
                    val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                    val errorResponse = gson.fromJson(jsonObject, ErrorResponseDTO::class.java)

                    Toast.makeText(this@DetailActivity, "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupListener(data: DetailProductData) {
        binding.apply {
            btnDtlBuy.setOnClickListener {
                val bottSheet = DetailBottomSheet(data)
                bottSheet.show(supportFragmentManager, DetailActivity::class.java.simpleName)
            }
            btnDtlTrolley.setOnClickListener {

            }
        }
    }
}