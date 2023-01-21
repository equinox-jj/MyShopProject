package com.myshopproject.presentation.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myshopproject.R
import com.myshopproject.databinding.FragmentHomeBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.home.adapter.ProductListAdapter
import com.myshopproject.utils.enumhelper.ProductType
import com.myshopproject.utils.enumhelper.SortedBy
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ProductListAdapter(ProductType.PRODUCT_LIST) }
    private val viewModel by viewModels<HomeViewModel>()

    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecyclerView()
        setupListener()

        initObserver(SortedBy.DefaultSort)
    }

    private fun setupListener() {
        binding.apply {
            svHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText?.length == 0 || newText.toString() == "") {
                        performSearch(null)
                    } else {
                        performSearch(newText)
                    }
                    return true
                }
            })
            fabSortedBy.setOnClickListener {
                dialogSortedBy()
            }
        }
    }

    private fun performSearch(query: String?) {
        job?.run {
            if (this.isActive) {
                this.cancel()
            }
        }
        job = viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            viewModel.getProductList(query)
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            rvHome.adapter = adapter
            rvHome.setHasFixedSize(true)
        }
    }

    private fun dialogSortedBy() {
        val items = arrayOf("From A to Z", "From Z to A")
        var selectedItem = ""
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sort By")
            .setSingleChoiceItems(items, -1){_, position ->
                selectedItem = items[position]
            }
            .setPositiveButton("OK") {_,_ ->
                when(selectedItem) {
                    "From A to Z" -> initObserver(SortedBy.SortAtoZ)
                    "From Z to A" -> initObserver(SortedBy.SortZtoA)
                }
            }
            .setNegativeButton("Cancel"){_,_ -> }
            .show()
    }

    private fun initObserver(sortedBy: SortedBy) {
        viewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.shimmerHome.root.startShimmer()
                    binding.shimmerHome.root.setVisibilityVisible()
                    binding.rvHome.setVisibilityGone()
                    isEmptyState(false)
                }
                is Resource.Success -> {
                    if (response.data?.success?.data?.isNotEmpty() == true) {
                        binding.shimmerHome.root.stopShimmer()
                        binding.shimmerHome.root.setVisibilityGone()
                        binding.rvHome.setVisibilityVisible()
                        isEmptyState(false)

                        when(sortedBy) {
                            SortedBy.SortAtoZ -> {
                                response.data?.success?.data?.let { listData ->
                                    adapter.submitData(listData.sortedBy { it.nameProduct })
                                }
                            }
                            SortedBy.SortZtoA -> {
                                response.data?.success?.data?.let { listData ->
                                    adapter.submitData(listData.sortedByDescending { it.nameProduct })
                                }
                            }
                            SortedBy.DefaultSort -> {
                                response.data?.success?.data?.let { listData ->
                                    adapter.submitData(listData)
                                }
                            }
                        }
                    } else {
                        isEmptyState(true)
                        binding.shimmerHome.root.stopShimmer()
                        binding.shimmerHome.root.setVisibilityGone()
                        binding.rvHome.setVisibilityGone()
                    }
                }
                is Resource.Error -> {
                    binding.shimmerHome.root.stopShimmer()
                    binding.shimmerHome.root.setVisibilityGone()
                    binding.rvHome.setVisibilityGone()
                    isEmptyState(false)
                }
            }
        }
    }

    private fun isEmptyState(empty: Boolean) {
        if (empty) {
            binding.emptyHome.root.setVisibilityVisible()
        } else {
            binding.emptyHome.root.setVisibilityGone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}