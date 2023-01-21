package com.myshopproject.presentation.home.adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.myshopproject.databinding.ItemProductListBinding
import com.myshopproject.domain.entities.DataProduct
import com.myshopproject.presentation.home.HomeFragmentDirections
import com.myshopproject.utils.enumhelper.ProductType
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import com.myshopproject.utils.toIDRPrice

class ProductListVH(private val binding: ItemProductListBinding) : ViewHolder(binding.root) {
    fun bind(data: DataProduct, type: ProductType) {
        when(type) {
            ProductType.PRODUCT_LIST -> { bindProductList(data) }
            ProductType.PRODUCT_FAV_LIST -> { bindProductFavList(data) }
        }
    }

    private fun bindProductList(data: DataProduct) {
        binding.apply {
            ivProductFavIcon.setVisibilityGone()
            ivProductImage.load(data.image) {
                crossfade(800)
            }
            tvProductDate.text = data.date
            tvProductName.isSelected = true
            tvProductName.text = data.nameProduct
            tvProductPrice.text = data.harga.toIDRPrice()
            rbProductRate.rating = data.rate.div(2).toFloat()
            cvProduct.setOnClickListener {
                val productId = data.stock
                val action = HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(1)
                it.findNavController().navigate(action)
            }
        }
    }

    private fun bindProductFavList(data: DataProduct) {
        binding.apply {
            ivProductFavIcon.setVisibilityVisible()
            ivProductImage.load(data.image) {
                crossfade(800)
            }
            tvProductDate.text = data.date
            tvProductName.isSelected = true
            tvProductName.text = data.nameProduct
            tvProductPrice.text = data.harga.toIDRPrice()
            rbProductRate.rating = data.rate.div(2).toFloat()
            cvProduct.setOnClickListener {
                val productId = data.stock
                val action = HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(1)
                it.findNavController().navigate(action)
            }
        }
    }
}