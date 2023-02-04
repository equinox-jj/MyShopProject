package com.myshopproject.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myshopproject.databinding.ItemLoadStatePagingBinding

class ItemLoadAdapter(private val retry: () -> Unit): LoadStateAdapter<ItemLoadAdapter.LoadStateVH>() {

    inner class LoadStateVH(private val binding: ItemLoadStatePagingBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnLoadAdapter.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                pbLoadAdapter.isVisible = loadState is LoadState.Loading
                btnLoadAdapter.isVisible = loadState is LoadState.Error
                tvLoadAdapter.isVisible = loadState is LoadState.Error

                if (loadState is LoadState.Error) {
                    tvLoadAdapter.text = loadState.error.localizedMessage
                }
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateVH, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateVH {
        val binding = ItemLoadStatePagingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateVH(binding)
    }
}