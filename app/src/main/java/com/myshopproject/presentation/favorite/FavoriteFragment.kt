package com.myshopproject.presentation.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myshopproject.R
import com.myshopproject.databinding.FragmentFavoriteBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.DataStoreViewModel
import com.myshopproject.presentation.home.adapter.ProductListAdapter
import com.myshopproject.utils.enumhelper.ProductType
import com.myshopproject.utils.enumhelper.SortedBy
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var adapter: ProductListAdapter? = null
    private val viewModel by viewModels<FavoriteViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    private var userId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)

        initRecyclerView()
        setupListener()
        setupToolbarMenu()

        initDataStore()
        initObserver(SortedBy.DefaultSort)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getProductListFav("", userId)
            }
        }
    }

    private fun initDataStore() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val id = prefViewModel.getUserId.first()
                userId = id
            }
        }
    }

    private fun setupToolbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }

        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupListener() {
        binding.apply {
            svFavorite.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText?.length == 0 || newText.toString() == "") {
                        performSearch("")
                    } else {
                        performSearch(newText)
                    }
                    return true
                }
            })
            fabSortedByFav.setOnClickListener {
                dialogSortedBy()
            }
        }
    }

    private fun performSearch(query: String?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(2000)
                viewModel.getProductListFav(query, userId)
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            adapter = ProductListAdapter(ProductType.PRODUCT_FAV_LIST)
            rvFavorite.adapter = adapter
            rvFavorite.setHasFixedSize(true)
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
                    binding.shimmerFavorite.root.startShimmer()
                    binding.shimmerFavorite.root.setVisibilityVisible()
                    binding.rvFavorite.setVisibilityGone()
                    isEmptyState(false)
                }
                is Resource.Success -> {
                    if (response.data?.success?.data?.isNotEmpty() == true) {
                        binding.shimmerFavorite.root.stopShimmer()
                        binding.shimmerFavorite.root.setVisibilityGone()
                        binding.rvFavorite.setVisibilityVisible()
                        isEmptyState(false)

                        when(sortedBy) {
                            SortedBy.SortAtoZ -> {
                                response.data?.success?.data?.let { listData ->
                                    adapter?.submitData(listData.sortedBy { it.nameProduct })
                                }
                            }
                            SortedBy.SortZtoA -> {
                                response.data?.success?.data?.let { listData ->
                                    adapter?.submitData(listData.sortedByDescending { it.nameProduct })
                                }
                            }
                            SortedBy.DefaultSort -> {
                                response.data?.success?.data?.let { listData ->
                                    adapter?.submitData(listData)
                                }
                            }
                        }
                    } else {
                        binding.shimmerFavorite.root.stopShimmer()
                        binding.shimmerFavorite.root.setVisibilityGone()
                        binding.rvFavorite.setVisibilityGone()
                        isEmptyState(true)
                    }
                }
                is Resource.Error -> {
                    binding.shimmerFavorite.root.stopShimmer()
                    binding.shimmerFavorite.root.setVisibilityGone()
                    binding.rvFavorite.setVisibilityGone()
                    isEmptyState(false)
                }
            }
        }
    }

    private fun isEmptyState(empty: Boolean) {
        if (empty) {
            binding.emptyFavorite.root.setVisibilityVisible()
        } else {
            binding.emptyFavorite.root.setVisibilityGone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }
}