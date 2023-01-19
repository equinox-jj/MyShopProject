package com.myshopproject.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myshopproject.databinding.ItemProductListBinding
import com.myshopproject.domain.entities.DataProduct
import com.myshopproject.utils.DiffUtilRecycler

class ProductListAdapter : RecyclerView.Adapter<ProductListVH>() {

    private var listProduct = listOf<DataProduct>()

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
        val diffUtilCallback = DiffUtil.calculateDiff(diffUtilRecycler)
        listProduct = newData
        diffUtilCallback.dispatchUpdatesTo(this)
    }
}