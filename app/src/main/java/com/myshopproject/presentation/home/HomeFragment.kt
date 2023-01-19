package com.myshopproject.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.myshopproject.R
import com.myshopproject.databinding.FragmentHomeBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.home.adapter.ProductListAdapter
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ProductListAdapter() }
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initObserver()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.apply {
            rvHome.adapter = adapter
            rvHome.setHasFixedSize(true)
        }
    }

    private fun initObserver() {
        viewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.shimmerHome.root.startShimmer()
                    binding.shimmerHome.root.setVisibilityVisible()
                    binding.rvHome.setVisibilityGone()
                }
                is Resource.Success -> {
                    binding.shimmerHome.root.stopShimmer()
                    binding.shimmerHome.root.setVisibilityGone()
                    binding.rvHome.setVisibilityVisible()
                    response.data?.success?.data?.let { adapter.submitData(it) }
                }
                is Resource.Error -> {
                    binding.shimmerHome.root.stopShimmer()
                    binding.shimmerHome.root.setVisibilityGone()
                    binding.rvHome.setVisibilityGone()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}