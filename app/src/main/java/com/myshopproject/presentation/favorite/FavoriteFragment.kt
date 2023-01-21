package com.myshopproject.presentation.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.myshopproject.R
import com.myshopproject.databinding.FragmentFavoriteBinding
import com.myshopproject.presentation.home.adapter.ProductListAdapter
import com.myshopproject.utils.enumhelper.ProductType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ProductListAdapter(ProductType.PRODUCT_FAV_LIST) }
    private val viewModel by viewModels<FavoriteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}