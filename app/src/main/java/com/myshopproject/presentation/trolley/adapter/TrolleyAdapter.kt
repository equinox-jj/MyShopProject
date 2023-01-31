package com.myshopproject.presentation.trolley.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.databinding.ItemProductCartBinding
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.utils.DiffUtilRecycler
import com.myshopproject.utils.toIDRPrice

class TrolleyAdapter(
    val onDeleteClick: (Int) -> Unit
): RecyclerView.Adapter<TrolleyAdapter.TrolleyVH>() {

    private var data = listOf<CartEntity>()

    inner class TrolleyVH(private val binding: ItemProductCartBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cartEntity: CartEntity) {
            binding.apply {
                ivTrolleyProduct.load(cartEntity.image)
                tvTrolleyProductName.text = cartEntity.productName
                tvTrolleyProductPrice.text = cartEntity.price.toIDRPrice()
                tvTrolleyQuantity.text = cartEntity.quantity.toString()
                btnTrolleyDelete.setOnClickListener {
                    onDeleteClick.invoke(cartEntity.id)
                }
            }
        }
    }

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