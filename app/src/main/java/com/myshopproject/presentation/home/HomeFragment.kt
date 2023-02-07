package com.myshopproject.presentation.home

import android.content.Intent
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
import com.myshopproject.presentation.detail.DetailActivity
import com.myshopproject.presentation.home.adapter.ItemLoadAdapter
import com.myshopproject.presentation.home.adapter.ProductPagingAdapter
import com.myshopproject.utils.Constants
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

        initObserver()
        initRecyclerView()
        setupListener()
    }

    private fun initRecyclerView() {
        binding.apply {
            adapter = ProductPagingAdapter(
                onClick = {
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra(Constants.PRODUCT_ID_INTENT, it)
                    startActivity(intent)
                }
            )
            rvHome.adapter = adapter!!
            rvHome.setHasFixedSize(true)

            rvHome.adapter = adapter!!.withLoadStateFooter(
                footer = ItemLoadAdapter { adapter!!.retry() }
            )

            adapter!!.addLoadStateListener { loadState ->
                shimmerHome.root.isVisible = loadState.source.refresh is LoadState.Loading
                rvHome.isVisible = loadState.source.refresh is LoadState.NotLoading
                /** SEARCH EMPTY STATE */
                if (loadState.source.refresh is LoadState.NotLoading && adapter!!.itemCount < 1) {
                    rvHome.isVisible = false
                    emptyHome.root.isVisible = true
                } else {
                    emptyHome.root.isVisible = false
                }
            }
        }
    }

    private fun setupListener() {
        binding.apply {
            svHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.onSearch(newText)
                    return true
                }
            })

            refreshHome.setOnRefreshListener {
                svHome.setQuery(null, false)
                svHome.clearFocus()
                refreshHome.isRefreshing = false
                adapter?.retry()
            }
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productList.collectLatest { adapter?.submitData(viewLifecycleOwner.lifecycle,it) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }
}