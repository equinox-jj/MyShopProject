package com.myshopproject.presentation.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
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
import androidx.navigation.navArgs
import coil.imageLoader
import coil.request.ImageRequest
import com.myshopproject.R
import com.myshopproject.databinding.ActivityDetailBinding
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.domain.entities.DetailProductData
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.detail.adapter.ImageSliderAdapter
import com.myshopproject.presentation.detail.bottomsheet.DetailBottomSheet
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import com.myshopproject.utils.toIDRPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var imageSliderAdapter: ImageSliderAdapter

    private val viewModel by viewModels<DetailViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    private val args by navArgs<DetailActivityArgs>()

    private lateinit var dataDetailProduct: DetailProductData
    private lateinit var productImage: String
    private var productId: Int = 0
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        productId = args.productId

        setupToolbarMenu()
        coroutineScope()
        refreshListener()

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
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
//                        Picasso.get().load(productImage).into(object : com.squareup.picasso.Target {
//                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                                val intent = Intent(Intent.ACTION_SEND)
//                                intent.type = "image/*"
//                                intent.putExtra(
//                                    Intent.EXTRA_TEXT,
//                                    "Name : ${dataDetailProduct.nameProduct}\nStock : ${dataDetailProduct.stock}\nWeight : ${dataDetailProduct.weight}\nSize : ${dataDetailProduct.size}\nLink : https://joshuaj.com/deeplink?id=$productId"
//                                )
//                                intent.putExtra(Intent.EXTRA_STREAM, getBitmapFromView(bitmap))
//                                startActivity(Intent.createChooser(intent, "Share To"))
//                            }
//
//                            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                                Log.v("IMG Downloader", "Bitmap Failed...");
//                            }
//
//                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                Log.v("IMG Downloader", "Bitmap Preparing Load...");
//                            }
//                        })
                        val request = ImageRequest.Builder(this@DetailActivity)
                            .data(dataDetailProduct.image)
                            .target(
                                onStart = { },
                                onSuccess = { result ->
                                    val intent = Intent(Intent.ACTION_SEND)
                                    val bitmap = (result as BitmapDrawable).bitmap
                                    intent.type = "image/*"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Name : ${dataDetailProduct.nameProduct}\nStock : ${dataDetailProduct.stock}\nWeight : ${dataDetailProduct.weight}\nSize : ${dataDetailProduct.size}\nLink : https://joshuaj.com/deeplink?id=$productId"
                                    )
                                    intent.putExtra(Intent.EXTRA_STREAM, getBitmapFromView(bitmap))
                                    startActivity(Intent.createChooser(intent, "Share To"))
                                },
                                onError = { }
                            ).build()
                        this@DetailActivity.imageLoader.enqueue(request)

                        /*val shareDeeplink = Intent(Intent.ACTION_SEND)
                        shareDeeplink.type = "text/plain"
                        shareDeeplink.putExtra(Intent.EXTRA_TEXT, "https://joshuaj.com/deeplink?id=$productId")
                        startActivity(Intent.createChooser(shareDeeplink, "Share link using"))*/
                    }
                }
                return true
            }
        })
    }

    private fun refreshListener() {
        binding.refreshDetail.setOnRefreshListener {
            binding.refreshDetail.isRefreshing = false
            viewModel.onRefresh(productId, userId)
        }
    }

    private fun coroutineScope() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    prefViewModel.getUserId.collect {
                        userId = it
                    }
                }
                launch {
                    initObserver()
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.getProductDetail(productId, userId)
        viewModel.detailState.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.shimmerDetail.root.show()
                    binding.nestedScrollDtl.hide()
                    binding.btnDtlBuy.hide()
                    binding.btnDtlTrolley.hide()
                    binding.toolbarDetail.title
                }
                is Resource.Success -> {
                    binding.shimmerDetail.root.hide()
                    binding.nestedScrollDtl.show()
                    binding.btnDtlBuy.show()
                    binding.btnDtlTrolley.show()
                    response.data?.success?.data?.let { dataDetailProduct = it }
                    response.data?.success?.data?.let { initView(it) }
                    response.data?.success?.data?.let { setupListener(it) }
                }
                is Resource.Error -> {
                    binding.shimmerDetail.root.hide()
                    binding.nestedScrollDtl.hide()
                    binding.btnDtlBuy.hide()
                    binding.btnDtlTrolley.hide()
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
            tvSizeProductDtl.text = data.size
            tvWeightProductDtl.text = data.weight
            tvTypeProductDtl.text = data.type
            tvDescProductDtl.text = data.desc
            productImage = data.image

            if (data.stock == 1) tvStockProductDtl.text = "Out of stock."
            else tvStockProductDtl.text = data.stock.toString()
            if (data.isFavorite) ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_filled)
            else ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_outlined)
        }
    }

    private fun setupListener(data: DetailProductData) {
        binding.apply {
            btnDtlBuy.setOnClickListener {
                val bottSheet = DetailBottomSheet(data)
                bottSheet.show(supportFragmentManager, DetailActivity::class.java.simpleName)
            }
            btnDtlTrolley.setOnClickListener {
                if (data.stock > 1) {
                    viewModel.insertCart(
                        CartEntity(
                            id = data.id,
                            image = data.image,
                            nameProduct = data.nameProduct,
                            quantity = 1,
                            price = data.harga,
                            itemTotalPrice = data.harga.toInt(),
                            stock = data.stock,
                            isChecked = false
                        )
                    )
                    finish()
                    Toast.makeText(this@DetailActivity, "Add to trolley.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailActivity, "Failed add to trolley.", Toast.LENGTH_SHORT).show()
                }
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

    private fun getBitmapFromView(bmp: Bitmap?): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(this.externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
            val out = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.close()
            bmpUri = Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

}