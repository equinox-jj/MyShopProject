package com.myshopproject.presentation.detail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.myshopproject.R
import com.myshopproject.databinding.ActivityDetailBinding
import com.myshopproject.databinding.CustomDialogImageDetailBinding
import com.myshopproject.domain.entities.CartDataDomain
import com.myshopproject.domain.entities.DetailProductData
import com.myshopproject.domain.entities.PaymentResult
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.detail.adapter.ImageSliderAdapter
import com.myshopproject.presentation.detail.bottomsheet.DetailBottomSheet
import com.myshopproject.presentation.favorite.adapter.ProductFavoriteAdapter
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.presentation.viewmodel.LocalViewModel
import com.myshopproject.utils.Constants.PAYMENT_DATA_INTENT
import com.myshopproject.utils.Constants.PRODUCT_ID_INTENT
import com.myshopproject.utils.ItemType
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import com.myshopproject.utils.toIDRPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private var adapter: ProductFavoriteAdapter? = null

    private val viewModel by viewModels<DetailViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()
    private val localViewModel by viewModels<LocalViewModel>()

    private lateinit var dataDetailProduct: DetailProductData

    private var paymentParcel: PaymentResult? = null
    private var productId = 0
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentParcel = intent.getParcelableExtra(PAYMENT_DATA_INTENT)

        setupToolbar()
        coroutineScope()
        refreshListener()
        checkProductId()
        initRecyclerView()

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    private fun initRecyclerView() {
        binding.apply {
            adapter = ProductFavoriteAdapter(
                type = ItemType.IS_DETAIL_PRODUCT,
                onClick = {}
            )
            contentProductOther.rvProductOther.adapter = adapter
            contentProductOther.rvProductOther.setHasFixedSize(true)

            contentProductHistory.rvProductHistory.adapter = adapter
            contentProductHistory.rvProductHistory.setHasFixedSize(true)
        }
    }

    private fun checkProductId() {
        val idProduct = intent.getIntExtra(PRODUCT_ID_INTENT, 0)
        productId = idProduct
        if (productId == 0) {
            val data: Uri? = intent?.data
            val id = data?.getQueryParameter("id")
            if (id != null) {
                productId = id.toInt()
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> {
                        finish()
                    }
                    R.id.menu_share -> {
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
                                        "Name : ${dataDetailProduct.nameProduct}\nStock : ${dataDetailProduct.stock}\nWeight : ${dataDetailProduct.weight}\nSize : ${dataDetailProduct.size}\nLink : https://joshuaj.com/detail_product?id=$productId"
                                    )

                                    val path = MediaStore.Images.Media.insertImage(
                                        contentResolver,
                                        bitmap,
                                        "image desc",
                                        null
                                    )

                                    val uri = Uri.parse(path)

                                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                                    if (intent.resolveActivity(packageManager) != null) {
                                        startActivity(Intent.createChooser(intent, "Share To"))
                                    }
                                },
                                onError = { }
                            ).build()
                        this@DetailActivity.imageLoader.enqueue(request)
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
                    binding.stickyScrollDetail.hide()
                    binding.btnDtlBuy.hide()
                    binding.btnDtlTrolley.hide()
                    binding.toolbarDetail.title
                }
                is Resource.Success -> {
                    binding.shimmerDetail.root.hide()
                    binding.stickyScrollDetail.show()
                    binding.btnDtlBuy.show()
                    binding.btnDtlTrolley.show()
                    response.data?.success?.data?.let { dataDetailProduct = it }
                    response.data?.success?.data?.let { initView(it) }
                    response.data?.success?.data?.let { setupListener(it) }
                }
                is Resource.Error -> {
                    binding.shimmerDetail.root.hide()
                    binding.stickyScrollDetail.hide()
                    binding.btnDtlBuy.hide()
                    binding.btnDtlTrolley.hide()
                }
            }
        }

        viewModel.getProductOther(userId)
        viewModel.otherProductState.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.view1.hide()
                    binding.toolbarOtherProduct.hide()
                    binding.contentProductOther.rvProductOther.hide()
                }
                is Resource.Success -> {
                    if (response.data?.success?.data?.isNotEmpty() == true) {
                        binding.view1.show()
                        binding.toolbarOtherProduct.show()
                        binding.contentProductOther.rvProductOther.show()
                        response.data?.success?.data?.let { adapter?.submitData(it) }
                    } else {
                        binding.view1.hide()
                        binding.toolbarOtherProduct.hide()
                        binding.contentProductOther.rvProductOther.hide()
                    }
                }
                is Resource.Error -> {
                    binding.view1.hide()
                    binding.toolbarOtherProduct.hide()
                    binding.contentProductOther.rvProductOther.hide()
                }
            }
        }

        viewModel.getProductHistory(userId)
        viewModel.historyProductState.observe(this@DetailActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.view2.hide()
                    binding.toolbarHistoryProduct.hide()
                    binding.contentProductHistory.rvProductHistory.hide()
                }
                is Resource.Success -> {
                    if (response.data?.success?.data?.isNotEmpty() == true) {
                        binding.view2.show()
                        binding.toolbarHistoryProduct.show()
                        binding.contentProductHistory.rvProductHistory.show()
                        response.data?.success?.data?.let { adapter?.submitData(it) }
                    } else {
                        binding.view2.hide()
                        binding.toolbarHistoryProduct.hide()
                        binding.contentProductHistory.rvProductHistory.hide()
                    }
                }
                is Resource.Error -> {
                    binding.view2.hide()
                    binding.toolbarHistoryProduct.hide()
                    binding.contentProductHistory.rvProductHistory.hide()
                }
            }
        }
    }

    private fun initView(data: DetailProductData) {
        binding.apply {
            toolbarDetail.title = data.nameProduct
            imageSliderAdapter = ImageSliderAdapter(
                data.imageProduct,
                onClick = {
                    showImageDetail(this@DetailActivity, it)
                }
            )
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

            if (data.stock == 1) tvStockProductDtl.text = resources.getString(R.string.out_of_stock)
            else tvStockProductDtl.text = data.stock.toString()
            if (data.isFavorite) ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_filled)
            else ivImageFavProductDtl.setImageResource(R.drawable.ic_favorite_outlined)
        }
    }

    private fun setupListener(data: DetailProductData) {
        binding.apply {
            if (paymentParcel != null) {
                val bottSheet = DetailBottomSheet(data, paymentParcel)
                bottSheet.show(supportFragmentManager, DetailActivity::class.java.simpleName)
            }

            btnDtlBuy.setOnClickListener {
                val bottSheet = DetailBottomSheet(data, paymentParcel)
                bottSheet.show(supportFragmentManager, DetailActivity::class.java.simpleName)
            }

            btnDtlTrolley.setOnClickListener {
                if (data.stock > 1) {
                    localViewModel.insertCart(
                        CartDataDomain(
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
                    Toast.makeText(this@DetailActivity, "Add to trolley.", Toast.LENGTH_SHORT).show()
                    finish()
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

    private fun showImageDetail(context: Context, drawable: String) {
        val dialogBinding = CustomDialogImageDetailBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context, R.style.Ctm_ImageDialog)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.create()
        dialog.show()

        dialogBinding.imageDetailDialog.load(drawable)
        Log.d("DataImage", drawable)
    }

}