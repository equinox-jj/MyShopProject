package com.myshopproject.presentation.trolley

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import com.myshopproject.utils.Constants.LIST_PRODUCT_ID
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import com.myshopproject.utils.toIDRPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrolleyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrolleyBinding

    private var trolleyAdapter: TrolleyAdapter? = null
    private val viewModel by viewModels<TrolleyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrolleyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTrolley)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initObserver()
        initRecyclerView()
        setupListener()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllProduct().collect { result ->
                    var totalPrice = 0
                    val filterResult = result.filter { it.isChecked }

                    for (i in filterResult.indices) {
                        totalPrice = totalPrice.plus(filterResult[i.toString().toInt()].itemTotalPrice!!)
                    }
                    binding.tvProductPriceTrlly.text = totalPrice.toString().toIDRPrice()
                    binding.cbTrolley.isChecked = result.size == filterResult.size

                    if (result.isNotEmpty()) {
                        binding.rvTrolley.show()
                        trolleyAdapter?.submitData(result)
                    } else {
                        binding.rvTrolley.hide()
                    }

                }
            }
        }
    }

    private fun setupListener() {
        binding.cbTrolley.setOnClickListener {
            if (binding.cbTrolley.isChecked) {
                viewModel.updateProductIsCheckedAll(true)
            } else {
                viewModel.updateProductIsCheckedAll(false)
            }
        }
        binding.btnBuyTrlly.setOnClickListener {
            val dataStockItems = arrayListOf<UpdateStockItem>()
            val listOfProductId = arrayListOf<String>()
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getAllCheckedProduct().collect { result ->
                        for (i in result.indices) {
                            dataStockItems.add(UpdateStockItem(result[i].id.toString(), result[i].quantity!!))
                            listOfProductId.add(result[i].id.toString())
                            buyProduct(dataStockItems, listOfProductId)
                        }
                    }
                }
            }
        }
    }

    private fun buyProduct(body: List<UpdateStockItem>, productId: ArrayList<String>) {
        viewModel.updateStock(body)
        viewModel.updateStockState.observe(this@TrolleyActivity) { response ->
            when (response) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val intent = Intent(this@TrolleyActivity, BuySuccessActivity::class.java)
                    intent.putExtra(LIST_PRODUCT_ID, productId)
                    startActivity(intent)
                    Toast.makeText(this@TrolleyActivity, response.data?.success?.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            trolleyAdapter = TrolleyAdapter(
                onDeleteItem = { viewModel.deleteProductByIdFromTrolley(it.id) },
                onAddQuantity = {
                    val productId = it.id
                    val quantity = it.quantity
                    val price = it.price
                    viewModel.updateProductData(
                        productId,
                        price?.toInt()?.times(quantity.toString().toInt().plus(1)),
                        quantity?.plus(1)
                    )
                },
                onMinQuantity = {
                    val productId = it.id
                    val quantity = it.quantity
                    val price = it.price
                    viewModel.updateProductData(
                        productId,
                        price?.toInt()?.times(quantity.toString().toInt().minus(1)),
                        quantity?.minus(1)
                    )
                },
                onCheckedItem = {
                    val productId = it.id
                    val isChecked = !it.isChecked
                    viewModel.updateProductIsCheckedById(isChecked, productId)
                },
            )
            rvTrolley.adapter = trolleyAdapter
            rvTrolley.setHasFixedSize(true)
        }
    }

}