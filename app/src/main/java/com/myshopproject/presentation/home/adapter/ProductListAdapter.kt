package com.myshopproject.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.databinding.ItemProductListBinding
import com.myshopproject.domain.entities.DataProduct
import com.myshopproject.presentation.favorite.FavoriteFragmentDirections
import com.myshopproject.presentation.home.HomeFragmentDirections
import com.myshopproject.utils.DiffUtilRecycler
import com.myshopproject.utils.enums.ProductType
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import com.myshopproject.utils.toIDRPrice

class ProductListAdapter(private val type: ProductType) : RecyclerView.Adapter<ProductListAdapter.ProductListVH>() {

    private var listProduct = listOf<DataProduct>()

    class ProductListVH(private val binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataProduct, type: ProductType) {
            when(type) {
                ProductType.PRODUCT_LIST -> {
                    bindProductList(data)
                    itemView.setOnClickListener {
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailActivity(data.id)
                        it.findNavController().navigate(action)
                    }
                }
                ProductType.PRODUCT_FAV_LIST -> {
                    bindProductFavList(data)
                    itemView.setOnClickListener {
                        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailActivity(data.id)
                        it.findNavController().navigate(action)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListVH {
        val binding = ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductListVH(binding)
    }

    override fun onBindViewHolder(holder: ProductListVH, position: Int) {
        holder.bind(listProduct[position], type)
    }

    override fun getItemCount(): Int = listProduct.size

    fun submitData(newData: List<DataProduct>) {
        val diffUtilRecycler = DiffUtilRecycler(listProduct, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtilRecycler)
        listProduct = newData
        diffResult.dispatchUpdatesTo(this)
    }
}