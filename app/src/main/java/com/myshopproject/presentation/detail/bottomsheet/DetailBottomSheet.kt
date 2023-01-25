package com.myshopproject.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
        return binding.root
    }

    private fun initView() {
        binding.apply {
            ivProductBottSht.load(data.image)
            tvProductPriceBottSht.text = data.harga.toIDRPrice()
            tvProductStockBottSht.text = "Stock : ${data.stock}"
        }
    }

    private fun initObserver() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}