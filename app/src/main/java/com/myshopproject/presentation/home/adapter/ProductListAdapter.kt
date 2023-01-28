package com.myshopproject.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myshopproject.databinding.ItemProductListBinding
import com.myshopproject.domain.entities.DataProduct
import com.myshopproject.utils.DiffUtilRecycler
import com.myshopproject.utils.enums.ProductType

class ProductListAdapter(private val type: ProductType) : RecyclerView.Adapter<ProductListVH>() {

    private var listProduct = listOf<DataProduct>()

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