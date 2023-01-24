package com.myshopproject.presentation.home.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.myshopproject.databinding.ItemProductListBinding
import com.myshopproject.domain.entities.DataProduct
import com.myshopproject.presentation.detail.DetailActivity
import com.myshopproject.utils.enumhelper.ProductType
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import com.myshopproject.utils.toIDRPrice

class ProductListVH(private val binding: ItemProductListBinding) : ViewHolder(binding.root) {
    fun bind(data: DataProduct, type: ProductType) {
        when(type) {
            ProductType.PRODUCT_LIST -> {
                bindProductList(data)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("product_id", data.id)
                    itemView.context.startActivity(intent)
                }
            }
            ProductType.PRODUCT_FAV_LIST -> {
                bindProductFavList(data)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("product_id", data.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    private fun bindProductList(data: DataProduct) {
        binding.apply {
            ivProductFavIcon.setVisibilityGone()
            ivProductImage.load(data.image)
            tvProductDate.text = data.date
            tvProductName.isSelected = true
            tvProductName.text = data.nameProduct
            tvProductPrice.text = data.harga.toIDRPrice()
            rbProductRate.rating = data.rate.toFloat()
        }
    }

    private fun bindProductFavList(data: DataProduct) {
        binding.apply {
            ivProductFavIcon.setVisibilityVisible()
            ivProductImage.load(data.image)
            tvProductDate.text = data.date
            tvProductName.isSelected = true
            tvProductName.text = data.nameProduct
            tvProductPrice.text = data.harga.toIDRPrice()
            rbProductRate.rating = data.rate.toFloat()
        }
    }
}