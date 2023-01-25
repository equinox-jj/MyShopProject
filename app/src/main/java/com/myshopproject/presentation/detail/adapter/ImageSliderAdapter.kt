package com.myshopproject.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.databinding.ItemImageSliderBinding
import com.myshopproject.domain.entities.DetailProductImage

class ImageSliderAdapter(private val listImage: List<DetailProductImage>): RecyclerView.Adapter<ImageSliderAdapter.ImageVH>() {

    class ImageVH(private val binding: ItemImageSliderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DetailProductImage) {
            binding.apply {
                ivProductDtl.load(data.imageProduct)
                tvCarouselProductDtl.text = data.titleProduct
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageVH {
        val binding = ItemImageSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageVH(binding)
    }

    override fun onBindViewHolder(holder: ImageVH, position: Int) {
        holder.bind(listImage[position])
    }

    override fun getItemCount(): Int = listImage.size

}