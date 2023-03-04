package com.myshopproject.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.myshopproject.R
import com.myshopproject.databinding.ItemImageSliderBinding
import com.myshopproject.domain.entities.DetailProductImage

class ImageSliderAdapter(
    private val listImage: List<DetailProductImage>,
    private val onClick: (String?) -> Unit
    ): RecyclerView.Adapter<ImageSliderAdapter.ImageVH>() {

    inner class ImageVH(private val binding: ItemImageSliderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DetailProductImage) {
            binding.apply {
                ivProductDtl.load(data.imageProduct) {
                    crossfade(800)
                    placeholder(R.drawable.ic_image_placeholder_filled)
                }
                tvCarouselProductDtl.text = data.titleProduct

                ivProductDtl.setOnClickListener {
                    onClick.invoke(data.imageProduct)
                }
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