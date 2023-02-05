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
import com.myshopproject.utils.DiffUtilRecycler
import com.myshopproject.utils.show
import com.myshopproject.utils.toIDRPrice

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductListVH>() {

    private var listProduct = listOf<DataProduct>()

    inner class ProductListVH(private val binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataProduct) {
            binding.apply {
                ivProductFavIcon.show()
                ivProductImage.load(data.image)
                tvProductDate.text = data.date
                tvProductName.isSelected = true
                tvProductName.text = data.nameProduct
                tvProductPrice.text = data.harga.toIDRPrice()
                rbProductRate.rating = data.rate.toFloat()

                itemView.setOnClickListener {
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailActivity(data.id)
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListVH {
        val binding = ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductListVH(binding)
    }

    override fun onBindViewHolder(holder: ProductListVH, position: Int) {
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