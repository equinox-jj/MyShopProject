package com.myshopproject.presentation.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.R
import com.myshopproject.databinding.ItemProductListBinding
import com.myshopproject.domain.entities.DataProduct
import com.myshopproject.utils.DiffUtilRecycler
import com.myshopproject.utils.ItemType
import com.myshopproject.utils.hide
import com.myshopproject.utils.show

class ProductFavoriteAdapter(private val type: ItemType, private val onClick: (DataProduct) -> Unit) : RecyclerView.Adapter<ProductFavoriteAdapter.ProductFavoriteVH>() {

    private var listProduct = listOf<DataProduct>()

    inner class ProductFavoriteVH(private val binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataProduct) {
            when(type) {
                ItemType.IS_FAVORITE_PRODUCT -> {
                    bindProductFavorite(data)
                }
                ItemType.IS_DETAIL_PRODUCT -> {
                    bindProductDetail(data)
                }
            }
        }

        private fun bindProductFavorite(data: DataProduct) {
            binding.apply {
                ivProductFavIcon.show()
                ivProductImage.load(data.image)
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

        private fun bindProductDetail(data: DataProduct) {
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
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductFavoriteVH {
        val binding = ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductFavoriteVH(binding)
    }

    override fun onBindViewHolder(holder: ProductFavoriteVH, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int = listProduct.size

    fun submitData(newData: List<DataProduct>) {
        val diffUtilRecycler = DiffUtilRecycler(listProduct, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtilRecycler)
        listProduct = newData
        diffResult.dispatchUpdatesTo(this)
    }
}