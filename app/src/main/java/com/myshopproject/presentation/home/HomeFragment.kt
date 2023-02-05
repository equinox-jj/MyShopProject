package com.myshopproject.presentation.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.myshopproject.R
import com.myshopproject.databinding.FragmentHomeBinding
import com.myshopproject.presentation.home.adapter.ItemLoadAdapter
import com.myshopproject.presentation.home.adapter.ProductPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var adapter: ProductPagingAdapter? = null

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecyclerView()
        setupListener()

        initObserver(null)
        refreshListener()
    }

    private fun refreshListener() {
        binding.refreshHome.setOnRefreshListener {
            binding.svHome.setQuery(null, false)
            binding.svHome.clearFocus()
            binding.refreshHome.isRefreshing = false
            adapter?.refresh()
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            adapter = ProductPagingAdapter()
            rvHome.adapter = adapter!!
            rvHome.adapter = adapter!!.withLoadStateFooter(
                footer = ItemLoadAdapter { adapter!!.retry() }
            )
            adapter!!.addLoadStateListener { loadState ->
                shimmerHome.root.isVisible = loadState.source.refresh is LoadState.Loading
                rvHome.isVisible = loadState.source.refresh is LoadState.NotLoading
            }
            rvHome.setHasFixedSize(true)
        }
    }

    private fun setupListener() {
        binding.apply {
            svHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    initObserver(newText)
                    return true
                }
            })
        }
    }

    private fun initObserver(query: String?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getListProductPaging(query).collectLatest { adapter?.submitData(it) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }
}