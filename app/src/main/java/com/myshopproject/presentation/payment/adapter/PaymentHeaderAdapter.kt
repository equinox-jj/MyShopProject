package com.myshopproject.presentation.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.myshopproject.databinding.ItemPaymentHeaderBinding
import com.myshopproject.domain.entities.PaymentResult
import com.myshopproject.domain.entities.PaymentTypeResponse
import com.myshopproject.utils.DiffUtilRecycler

class PaymentHeaderAdapter(
    private val onBodyClick: (PaymentResult) -> Unit
): RecyclerView.Adapter<PaymentHeaderAdapter.PaymentHeaderVH>() {

    private var data = listOf<PaymentTypeResponse>()

    inner class PaymentHeaderVH(private val binding: ItemPaymentHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PaymentTypeResponse) {
            binding.apply {
                val paymentBodyAdapter = PaymentBodyAdapter(
                    onClick = { onBodyClick.invoke(it) }
                )
                paymentBodyAdapter.submitData(data.data.sortedBy { it.order })
                rvItemBodyPayment.adapter = paymentBodyAdapter
                rvItemBodyPayment.setHasFixedSize(true)
                rvItemBodyPayment.addItemDecoration(DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL))

                tvPaymentItemHeader.text = data.type
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHeaderVH {
        val binding = ItemPaymentHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentHeaderVH(binding)
    }

    override fun onBindViewHolder(holder: PaymentHeaderVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun submitData(newData: List<PaymentTypeResponse>) {
        val diffUtilRecycler = DiffUtilRecycler(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtilRecycler)
        data = newData
        diffResult.dispatchUpdatesTo(this)
    }
}