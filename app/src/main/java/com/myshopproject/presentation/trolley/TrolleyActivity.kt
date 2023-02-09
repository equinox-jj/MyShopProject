package com.myshopproject.presentation.trolley

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.myshopproject.databinding.ActivityTrolleyBinding
import com.myshopproject.domain.entities.UpdateStockItem
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.buysuccess.BuySuccessActivity
import com.myshopproject.presentation.trolley.adapter.TrolleyAdapter
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.presentation.viewmodel.LocalViewModel
import com.myshopproject.utils.Constants.LIST_PRODUCT_ID
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import com.myshopproject.utils.toIDRPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrolleyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrolleyBinding

    private var trolleyAdapter: TrolleyAdapter? = null

    private val viewModel by viewModels<TrolleyViewModel>()
    private val localViewModel by viewModels<LocalViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrolleyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        initObserver()
        initRecyclerView()
        setupListener()
        initDataStore()
        postProductTrolley()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarTrolley)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initDataStore() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userId = prefViewModel.getUserId.first().toString()
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                localViewModel.getAllProduct().collect { result ->
                    if (result.isNotEmpty()) {
                        var totalPrice = 0
                        val filterResult = result.filter { it.isChecked }

                        for (i in filterResult.indices) {
                            totalPrice = totalPrice.plus(filterResult[i.toString().toInt()].itemTotalPrice!!)
                        }

                        binding.tvProductPriceTrlly.text = totalPrice.toString().toIDRPrice()
                        binding.cbTrolley.isChecked = result.size == filterResult.size

                        binding.rvTrolley.adapter = trolleyAdapter
                        binding.rvTrolley.setHasFixedSize(true)
                        trolleyAdapter?.submitData(result)

                        binding.cbTrolley.show()
                        binding.rvTrolley.show()
                        binding.bottomAppBarTrolley.show()
                    } else {
                        binding.cbTrolley.hide()
                        binding.rvTrolley.hide()
                        binding.bottomAppBarTrolley.hide()
                    }
                }
            }
        }
    }

    private fun setupListener() {
        binding.cbTrolley.setOnClickListener {
            if (binding.cbTrolley.isChecked) {
                localViewModel.updateProductIsCheckedAll(true)
            } else {
                localViewModel.updateProductIsCheckedAll(false)
            }
        }
    }

    private fun postProductTrolley() {
        val dataStockItems = arrayListOf<UpdateStockItem>()
        val listOfProductId = arrayListOf<String>()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                localViewModel.getAllCheckedProduct().collectLatest { result ->
                    for (i in result.indices) {
                        dataStockItems.add(UpdateStockItem(result[i].id.toString(), result[i].quantity!!))
                        listOfProductId.add(result[i].id.toString())
                    }
                    binding.btnBuyTrlly.setOnClickListener {
                        if (result.isEmpty()) {
                            binding.btnBuyTrlly.isClickable = false
                        } else {
                            viewModel.updateStock(userId, dataStockItems)
                            viewModel.updateStockState.observe(this@TrolleyActivity) { response ->
                                when (response) {
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        dataStockItems.forEach { viewModel.deleteProductByIdFromTrolley(it.id_product.toInt()) }
                                        val intent = Intent(this@TrolleyActivity, BuySuccessActivity::class.java)
                                        intent.putExtra(LIST_PRODUCT_ID, listOfProductId)
                                        startActivity(intent)
                                        Log.d("StartActivities", "$intent")
                                        finishAffinity()
                                    }
                                    is Resource.Error -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            trolleyAdapter = TrolleyAdapter(
                onDeleteItem = { localViewModel.deleteProductByIdFromTrolley(it.id) },
                onAddQuantity = {
                    val productId = it.id
                    val quantity = it.quantity
                    val price = it.price
                    localViewModel.updateProductData(
                        quantity = quantity?.plus(1),
                        itemTotalPrice = price?.toInt()?.times(quantity.toString().toInt().plus(1)),
                        id = productId
                    )
                },
                onMinQuantity = {
                    val productId = it.id
                    val quantity = it.quantity
                    val price = it.price
                    localViewModel.updateProductData(
                        quantity = quantity?.minus(1),
                        itemTotalPrice = price?.toInt()?.times(quantity.toString().toInt().minus(1)),
                        id = productId
                    )
                },
                onCheckedItem = {
                    val productId = it.id
                    val isChecked = !it.isChecked
                    binding.btnBuyTrlly.isClickable = false
                    localViewModel.updateProductIsCheckedById(isChecked, productId)
                },
            )
        }
    }

}