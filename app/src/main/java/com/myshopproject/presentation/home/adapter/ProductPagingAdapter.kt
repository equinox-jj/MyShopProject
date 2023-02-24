package com.myshopproject.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.R
import com.myshopproject.databinding.ItemProductListBinding
import com.myshopproject.domain.entities.DataProduct
import com.myshopproject.utils.hide

class ProductPagingAdapter(
    private val onClick: (DataProduct) -> Unit
): PagingDataAdapter<DataProduct, ProductPagingAdapter.ProductPagingVH>(PRODUCT_COMPARATOR) {

    inner class ProductPagingVH(private val binding: ItemProductListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataProduct) {
            binding.apply {
                ivProductFavIcon.hide()
                ivProductImage.load(data.image) {
                    crossfade(800)
                    placeholder(R.drawable.ic_image_placeholder_filled)
                }
                tvProductDate.text = data.date
                tvProductName.isSelected = true
                tvProductName.text = data.nameProduct
                tvProductPrice.text = data.harga
                rbProductRate.rating = data.rate.toFloat()
                itemView.setOnClickListener {
                    onClick.invoke(data)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ProductPagingVH, position: Int) {
        val result = getItem(position)
        if (result != null) {
            holder.bind(result)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPagingVH {
        val binding = ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductPagingVH(binding)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<DataProduct>() {
            override fun areItemsTheSame(oldItem: DataProduct, newItem: DataProduct) = oldItem.id == newItem.id && oldItem.nameProduct == newItem.nameProduct
            override fun areContentsTheSame(oldItem: DataProduct, newItem: DataProduct) = oldItem == newItem
        }
    }
}