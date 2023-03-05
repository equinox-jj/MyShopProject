package com.myshopproject.presentation.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.R
import com.myshopproject.databinding.ItemPaymentHeaderBinding
import com.myshopproject.domain.entities.PaymentResult
import com.myshopproject.domain.entities.PaymentTypeResponse
import com.myshopproject.utils.DiffUtilRecycler

class PaymentHeaderAdapter(
    private val onBodyClick: (PaymentResult) -> Unit,
    private val onHeaderClick: (PaymentTypeResponse) -> Unit
): RecyclerView.Adapter<PaymentHeaderAdapter.PaymentHeaderVH>() {

    private var data = listOf<PaymentTypeResponse>()

    inner class PaymentHeaderVH(private val binding: ItemPaymentHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PaymentTypeResponse) {
            binding.apply {
                val paymentBodyAdapter = PaymentBodyAdapter(
                    onClick = { onBodyClick.invoke(it) }
                )
                data.data.let { result -> paymentBodyAdapter.submitData(result.sortedBy { it.order }) }
                rvItemBodyPayment.adapter = paymentBodyAdapter
                rvItemBodyPayment.setHasFixedSize(true)
                rvItemBodyPayment.addItemDecoration(DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL))

                binding.lineDivider.isVisible = data.order != 0
                tvPaymentItemHeader.text = data.type
                binding.clPaymentHeader.setOnClickListener {
                    onHeaderClick.invoke(data)
                    val bodyVisibility = binding.rvItemBodyPayment.isVisible
                    if (bodyVisibility){
                        binding.ivDropdownPayment.load(R.drawable.ic_downward_filled)
                    } else {
                        binding.ivDropdownPayment.load(R.drawable.ic_upward_filled)
                    }
                    binding.rvItemBodyPayment.isVisible = !bodyVisibility
                }
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