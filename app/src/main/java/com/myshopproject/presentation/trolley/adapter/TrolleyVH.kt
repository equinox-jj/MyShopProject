package com.myshopproject.presentation.trolley.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.databinding.ItemProductCartBinding
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.utils.toIDRPrice

class TrolleyVH(private val binding: ItemProductCartBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(cartEntity: CartEntity) {
        binding.apply {
            ivTrolleyProduct.load(cartEntity.image)
            tvTrolleyProductName.text = cartEntity.product_name
            tvTrolleyProductPrice.text = cartEntity.price.toIDRPrice()
            tvTrolleyQuantity.text = cartEntity.quantity.toString()
        }
    }
}