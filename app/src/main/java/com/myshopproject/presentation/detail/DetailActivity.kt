package com.myshopproject.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.myshopproject.R
import com.myshopproject.databinding.ActivityDetailBinding
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.domain.entities.DetailProductData
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.DataStoreViewModel
import com.myshopproject.presentation.detail.adapter.ImageSliderAdapter
import com.myshopproject.presentation.detail.bottomsheet.DetailBottomSheet
import com.myshopproject.utils.Constants.PRODUCT_ID
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import com.myshopproject.utils.toIDRPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var imageSliderAdapter: ImageSliderAdapter

    private val viewModel by viewModels<DetailViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    private var productId: Int = 0
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        productId = intent.getIntExtra(PRODUCT_ID, 0)

        if(productId == 0) {
            val uri: Uri? = intent.data
            val id = uri?.getQueryParameter("id")
            if (id != null) {
                productId = id.toInt()
            }
        }

        initDataStore()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                getDetailProducts()
            }
        }
        setupToolbarMenu()
    }

    private fun setupToolbarMenu() {
        addMenuProvider(object  : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    android.R.id.home -> {
                        finish()
                    }
                    R.id.menu_share -> {
                        val shareDeeplink = Intent(Intent.ACTION_SEND)
                        shareDeeplink.type = "text/plain"
                        shareDeeplink.putExtra(Intent.EXTRA_TEXT, "https://joshuaj.com/deeplink?id=$productId")
                        startActivity(Intent.createChooser(shareDeeplink, "Share link using"))
                    }
                }
                return true
            }

        })
    }

    private fun initDataStore() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                prefViewModel.getUserId.collect {
                    userId = it
                }
            }
        }
    }

    private fun getDetailProducts() {
        viewModel.getProductDetail(productId, userId)
        viewModel.detailState.observe(this@DetailActivity) { response ->
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
            toolbarDetail.title = data.nameProduct
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
                ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_outlined)
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
                viewModel.insertCart(
                    CartEntity(
                        id = data.id,
                        product_name = data.nameProduct,
                        price = data.harga,
                        image = data.image,
                        quantity = 1
                    )
                )
            }
            ivImageFavProductDtl.setOnClickListener {
                if (data.isFavorite) {
                    viewModel.removeProductFavorite(productId, userId)
                    removeFavorite()
                } else {
                    viewModel.addProductFavorite(productId, userId)
                    addFavorite()
                }
            }
        }
    }



    private fun addFavorite() {
        viewModel.favState.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_filled)
                    Toast.makeText(this, "Success add to favorite.", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    Toast.makeText(this@DetailActivity, response.errorBody.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun removeFavorite() {
        viewModel.unFavState.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_outlined)
                    Toast.makeText(this, "Success remove from favorite.", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    Toast.makeText(this@DetailActivity, response.errorBody.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}