package com.myshopproject.presentation.trolley.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.R
import com.myshopproject.databinding.ItemProductCartBinding
import com.myshopproject.domain.entities.CartEntity
import com.myshopproject.utils.toIDRPrice

class TrolleyAdapter(
    private val onDeleteItem: (CartEntity) -> Unit,
    private val onAddQuantity: (CartEntity) -> Unit,
    private val onMinQuantity: (CartEntity) -> Unit,
    private val onCheckedItem: (CartEntity) -> Unit,
) : RecyclerView.Adapter<TrolleyAdapter.TrolleyVH>() {

    private var data = ArrayList<CartEntity>()
    private var isCheckedAll: Boolean = false

    inner class TrolleyVH(private val binding: ItemProductCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartEntity: CartEntity) {
            binding.apply {
                ivTrolleyProduct.load(cartEntity.image)
                tvTrolleyProductName.text = cartEntity.nameProduct
                tvTrolleyProductPrice.text = cartEntity.price?.toIDRPrice()
                tvTrolleyQuantity.text = cartEntity.quantity.toString()
                cbTrolleyList.isChecked = cartEntity.isChecked
                btnTrolleyDelete.setOnClickListener {
                    onDeleteItem.invoke(cartEntity)
                }
                btnTrolleyIncrement.setOnClickListener {
                    val stock = cartEntity.stock
                    val quantity = (cartEntity.quantity)
                    if (quantity == stock) {
                        btnTrolleyIncrement.isClickable = false
                        Toast.makeText(itemView.context, "Out of stock.", Toast.LENGTH_SHORT).show()
                    } else {
                        onAddQuantity.invoke(cartEntity)
                        binding.btnTrolleyIncrement.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_button_round_grey)
                    }
                }
                btnTrolleyDecrement.setOnClickListener {
                    val quantity = (cartEntity.quantity)
                    if (quantity != 1) {
                        onMinQuantity.invoke(cartEntity)
                    } else {
                        btnTrolleyDecrement.isClickable = false
                        binding.btnTrolleyDecrement.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_button_round_grey)
                    }
                }
                cbTrolleyList.setOnClickListener {
                    onCheckedItem.invoke(cartEntity)
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

    fun selectAll(isChecked: Boolean) {
        isCheckedAll = isChecked
        notifyDataSetChanged()
    }

    fun submitData(newListData: List<CartEntity>?) {
        if (newListData == null) return
        data.clear()
        data.addAll(newListData)
        notifyItemRemoved(newListData.size)
        notifyDataSetChanged()
    }
}