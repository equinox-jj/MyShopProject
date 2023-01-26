package com.myshopproject.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.myshopproject.R
import com.myshopproject.databinding.BottomSheetProductDetailBinding
import com.myshopproject.domain.entities.DetailProductData
import com.myshopproject.utils.toIDRPrice

class DetailBottomSheet(private val data: DetailProductData) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetProductDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailBottomSheetViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetProductDetailBinding.inflate(inflater, container, false)

        initObserver()
        initView()
        setupListener()
        return binding.root
    }

    private fun setupListener() {
        binding.btnIncreaseBottSheet.setOnClickListener {
            viewModel.increaseQuantity(data.stock)
        }
        binding.btnDecreaseBottSheet.setOnClickListener {
            viewModel.decreaseQuantity()
        }
    }

    private fun initView() {
        binding.apply {
            ivProductBottSht.load(data.image)
            tvProductPriceBottSht.text = data.harga.toIDRPrice()
            tvStockProductBottSht.text = data.stock.toString()
        }
    }

    private fun initObserver() {
        viewModel.quantity.observe(viewLifecycleOwner) {
            binding.tvQuantityBottSheet.text = it.toString()
            if (it == data.stock) {
                binding.btnIncreaseBottSheet.background = ContextCompat.getDrawable(requireContext(), R.color.text_grey)
            } else if(it == 1) {
                binding.btnIncreaseBottSheet.background = ContextCompat.getDrawable(requireContext(), R.color.black)
            }
        }
        viewModel.setPrice(data.harga.toInt())
        viewModel.price.observe(viewLifecycleOwner) {
            binding.tvProductPriceBottSht.text = it.toString().toIDRPrice()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}