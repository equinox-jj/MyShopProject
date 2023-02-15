package com.myshopproject.presentation.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.R
import com.myshopproject.databinding.ItemPaymentBodyBinding
import com.myshopproject.domain.entities.PaymentResult
import com.myshopproject.utils.DiffUtilRecycler

class PaymentBodyAdapter(
    private val onClick: (PaymentResult) -> Unit
): RecyclerView.Adapter<PaymentBodyAdapter.PaymentBodyVH>() {

    private var listData = listOf<PaymentResult>()

    inner class PaymentBodyVH(private val binding: ItemPaymentBodyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PaymentResult) {
            binding.apply {
                when (data.id) {
                    "va_bca" -> {
                        ivImagePayment.load(R.drawable.img_bca)
                    }
                    "va_mandiri" -> {
                        ivImagePayment.load(R.drawable.img_mandiri)
                    }
                    "va_bri" -> {
                        ivImagePayment.load(R.drawable.img_bri)
                    }
                    "va_bni" -> {
                        ivImagePayment.load(R.drawable.img_bni)
                    }
                    "va_btn" -> {
                        ivImagePayment.load(R.drawable.img_btn)
                    }
                    "va_danamon" -> {
                        ivImagePayment.load(R.drawable.img_danamon)
                    }
                    "ewallet_gopay" -> {
                        ivImagePayment.load(R.drawable.img_gopay)
                    }
                    "ewallet_ovo" -> {
                        ivImagePayment.load(R.drawable.img_ovo)
                    }
                    "ewallet_dana" -> {
                        ivImagePayment.load(R.drawable.img_dana)
                    }
                }

                if (data.status) {
                    clPaymentBody.alpha = 1.0f
                    clPaymentBody.isClickable = true
                    clPaymentBody.setOnClickListener { onClick.invoke(data) }
                } else {
                    clPaymentBody.alpha = 0.4f
                    clPaymentBody.isClickable = false
                }

                tvPaymentName.text = data.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentBodyVH {
        val binding = ItemPaymentBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentBodyVH(binding)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: PaymentBodyVH, position: Int) {
        holder.bind(listData[position])
    }

    fun submitData(newData: List<PaymentResult>) {
        val diffUtilRecycler = DiffUtilRecycler(listData, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtilRecycler)
        listData = newData
        diffResult.dispatchUpdatesTo(this)
    }
}