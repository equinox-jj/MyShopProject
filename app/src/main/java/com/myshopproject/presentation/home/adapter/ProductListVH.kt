package com.myshopproject.presentation.home.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.myshopproject.databinding.ItemProductListBinding
import com.myshopproject.domain.entities.DataProduct
import com.myshopproject.utils.toIDRPrice

class ProductListVH(private val binding: ItemProductListBinding) : ViewHolder(binding.root) {
    fun bind(data: DataProduct) {
        binding.apply {
            ivProductImage.load(data.image) {
                crossfade(800)
            }
            tvProductDate.text = data.date
            tvProductName.isSelected = true
            tvProductName.text = data.nameProduct
            tvProductPrice.text = data.harga.toIDRPrice()
            rbProductRate.rating = data.rate.div(2).toFloat()
        }
    }
}