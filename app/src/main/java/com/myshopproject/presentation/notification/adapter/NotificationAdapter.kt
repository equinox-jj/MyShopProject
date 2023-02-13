package com.myshopproject.presentation.notification.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myshopproject.R
import com.myshopproject.databinding.ItemNotificationBinding
import com.myshopproject.domain.entities.FcmDataDomain
import com.myshopproject.utils.DiffUtilRecycler
import com.myshopproject.utils.hide
import com.myshopproject.utils.show

class NotificationAdapter(
    private val context: Context,
    private val isMultipleSelect: Boolean,
    private val onItemClicked: (FcmDataDomain) -> Unit,
    private val onCheckboxChecked: (FcmDataDomain) -> Unit
): RecyclerView.Adapter<NotificationAdapter.NotificationVH>() {

    private var data = listOf<FcmDataDomain>()

    inner class NotificationVH(private val binding: ItemNotificationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(fcmDataDomain: FcmDataDomain) {
            binding.apply {
                tvNotificationTitle.text = fcmDataDomain.notificationTitle
                tvNotificationBody.text = fcmDataDomain.notificationBody
                tvNotificationDate.text = fcmDataDomain.notificationDate

                if (isMultipleSelect) {
                    cbNotification.show()
                    cbNotification.isChecked = fcmDataDomain.isChecked
                } else {
                    cbNotification.hide()
                }

                cvNotification.setCardBackgroundColor(if (fcmDataDomain.isRead) Color.WHITE else ContextCompat.getColor(context,R.color.bg_notification_card))

                cvNotification.setOnClickListener {
                    if (!isMultipleSelect) {
                        onItemClicked.invoke(fcmDataDomain)
                    }
                }

                cbNotification.setOnClickListener {
                    onCheckboxChecked.invoke(fcmDataDomain)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationVH(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: NotificationVH, position: Int) {
        holder.bind(data[position])
    }

    fun submitData(newListData: List<FcmDataDomain>) {
        val diffUtilRecycler = DiffUtilRecycler(data, newListData)
        val diffResult = DiffUtil.calculateDiff(diffUtilRecycler)
        data = newListData
        diffResult.dispatchUpdatesTo(this)
    }
}