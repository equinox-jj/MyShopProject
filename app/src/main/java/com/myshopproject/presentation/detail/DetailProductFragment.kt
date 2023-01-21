package com.myshopproject.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.myshopproject.R
import com.myshopproject.databinding.FragmentDetailProductBinding

class DetailProductFragment : Fragment(R.layout.fragment_detail_product) {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailProductBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}