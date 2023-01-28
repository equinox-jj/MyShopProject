package com.myshopproject.presentation.trolley.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myshopproject.databinding.ItemProductCartBinding
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.utils.DiffUtilRecycler

class TrolleyAdapter: RecyclerView.Adapter<TrolleyVH>() {

    private var data = listOf<CartEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrolleyVH {
        val binding = ItemProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrolleyVH(binding)
    }

    override fun onBindViewHolder(holder: TrolleyVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun submitData(newData: List<CartEntity>) {
            val diffUtilRecycler = DiffUtilRecycler(data, newData)
            val diffResult = DiffUtil.calculateDiff(diffUtilRecycler)
            data = newData
            diffResult.dispatchUpdatesTo(this)
        }

}