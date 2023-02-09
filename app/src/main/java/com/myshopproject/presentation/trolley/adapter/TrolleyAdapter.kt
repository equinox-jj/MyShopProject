package com.myshopproject.presentation.trolley.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.databinding.ItemProductCartBinding
import com.myshopproject.domain.entities.CartDataDomain
import com.myshopproject.utils.DiffUtilRecycler
import com.myshopproject.utils.toIDRPrice

class TrolleyAdapter(
    private val onDeleteItem: (CartDataDomain) -> Unit,
    private val onAddQuantity: (CartDataDomain) -> Unit,
    private val onMinQuantity: (CartDataDomain) -> Unit,
    private val onCheckedItem: (CartDataDomain) -> Unit,
) : RecyclerView.Adapter<TrolleyAdapter.TrolleyVH>() {

    private var data = listOf<CartDataDomain>()

    inner class TrolleyVH(private val binding: ItemProductCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartDataDomain: CartDataDomain) {
            binding.apply {
                ivTrolleyProduct.load(cartDataDomain.image)
                tvTrolleyProductName.text = cartDataDomain.nameProduct
                tvTrolleyProductPrice.text = cartDataDomain.price?.toIDRPrice()
                tvTrolleyQuantity.text = cartDataDomain.quantity.toString()
                cbTrolleyList.isChecked = cartDataDomain.isChecked

                btnTrolleyDelete.setOnClickListener { onDeleteItem.invoke(cartDataDomain) }
                cbTrolleyList.setOnClickListener { onCheckedItem.invoke(cartDataDomain) }

                btnTrolleyIncrement.setOnClickListener {
                    cartDataDomain.stock?.let {
                        if (tvTrolleyQuantity.text.toString().toInt() < it) {
                            onAddQuantity.invoke(cartDataDomain)
                        }
                    }
                }

                btnTrolleyDecrement.setOnClickListener {
                    if (tvTrolleyQuantity.text.toString().toInt() > 1) {
                        onMinQuantity.invoke(cartDataDomain)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrolleyVH {
        val binding =
            ItemProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrolleyVH(binding)
    }

    override fun onBindViewHolder(holder: TrolleyVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun submitData(newListData: List<CartDataDomain>) {
        val diffUtilRecycler = DiffUtilRecycler(data, newListData)
        val diffResult = DiffUtil.calculateDiff(diffUtilRecycler)
        data = newListData
        diffResult.dispatchUpdatesTo(this)
    }
}