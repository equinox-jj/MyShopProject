package com.myshopproject.presentation.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myshopproject.R
import com.myshopproject.databinding.FragmentFavoriteBinding
import com.myshopproject.domain.repository.FirebaseAnalyticsRepository
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.detail.DetailActivity
import com.myshopproject.presentation.favorite.adapter.ProductFavoriteAdapter
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var adapter: ProductFavoriteAdapter? = null
    private val viewModel by viewModels<FavoriteViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    @Inject
    lateinit var analyticRepository: FirebaseAnalyticsRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)

        initObserver(SortedBy.DefaultSort)
        launchCoroutines()
        initRecyclerView()
        setupListener()
    }

    override fun onResume() {
        super.onResume()
        analyticRepository.onFavoriteLoadScreen(requireContext().javaClass.simpleName)
    }

    private fun launchCoroutines() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val userId = prefViewModel.getUserId.first()
                viewModel.getProductListFav(null, userId)
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            adapter = ProductFavoriteAdapter(
                type = ItemType.IS_FAVORITE_PRODUCT,
                onClick = {
                    analyticRepository.onProductFavoriteClick(it.nameProduct, it.harga?.replace(Regex("\\D"), "")?.toDouble(), it.rate, it.id)

                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra(Constants.PRODUCT_ID_INTENT, it.id)
                    startActivity(intent)
                }
            )
            rvFavorite.adapter = adapter
            rvFavorite.setHasFixedSize(true)
        }
    }

    private fun setupListener() {
        binding.apply {
            svFavorite.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    analyticRepository.onSearchFavorite(newText)
                    viewModel.onSearch(newText)
                    return true
                }
            })

            refreshFavorite.setOnRefreshListener {
                refreshFavorite.isRefreshing = false
                svFavorite.setQuery("", false)
                svFavorite.clearFocus()
                viewModel.onRefresh()
            }

            fabSortedByFav.setOnClickListener {
                dialogSortedBy()
            }
        }
    }

    private fun dialogSortedBy() {
        val items = arrayOf("From A to Z", "From Z to A")
        var selectedItem = ""
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sort By")
            .setSingleChoiceItems(items, -1){_, position ->
                selectedItem = items[position]
                analyticRepository.onSortByName(selectedItem)
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
                    binding.shimmerFavorite.root.show()
                    binding.rvFavorite.hide()
                    isEmptyState(false)
                }
                is Resource.Success -> {
                    if (response.data?.success?.data?.isNotEmpty() == true) {
                        binding.shimmerFavorite.root.stopShimmer()
                        binding.shimmerFavorite.root.hide()
                        binding.rvFavorite.show()
                        binding.fabSortedByFav.show()
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
                        binding.shimmerFavorite.root.hide()
                        binding.rvFavorite.hide()
                        binding.fabSortedByFav.hide()
                        isEmptyState(true)
                    }
                }
                is Resource.Error -> {
                    binding.shimmerFavorite.root.stopShimmer()
                    binding.shimmerFavorite.root.hide()
                    binding.rvFavorite.hide()
                    isEmptyState(false)
                }
            }
        }
    }

    private fun isEmptyState(empty: Boolean) {
        if (empty) {
            binding.emptyFavorite.root.show()
        } else {
            binding.emptyFavorite.root.hide()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }
}