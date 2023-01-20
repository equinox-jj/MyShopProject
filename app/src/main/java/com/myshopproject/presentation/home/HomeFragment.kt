package com.myshopproject.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.myshopproject.R
import com.myshopproject.databinding.FragmentHomeBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.home.adapter.ProductListAdapter
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

    private val adapter by lazy { ProductListAdapter() }
    private val viewModel by viewModels<HomeViewModel>()

    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initRecyclerView()
        setupListener()

        initObserver()
    }

    private fun setupListener() {
        binding.apply {
            svHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isNotEmpty()) {
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

    private fun performSearch(query: String) {
            job?.run {
                if (this.isActive) {
                    this.cancel()
                }
            }
            job = viewLifecycleOwner.lifecycleScope.launch {
                if (query.isNotEmpty()) {
                    delay(2000)
                    viewModel.getProductList(query)
                    initObserver()
                }
            }
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

    private fun dialogSortedBy() {
        val view = layoutInflater.inflate(R.layout.custom_dialog_sorted_product, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.Ctm_AlertDialog)
        val showDialog = builder.setView(view).show()

        val sortAtoZ = view.findViewById<RadioButton>(R.id.rbDialogSortAtoZ)
        val sortZtoA = view.findViewById<RadioButton>(R.id.rbDialogSortZtoA)
        val okDialog = view.findViewById<TextView>(R.id.tvDialogSortOk)

        sortAtoZ.setOnClickListener {  }
        sortZtoA.setOnClickListener {  }
        okDialog.setOnClickListener { showDialog.dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}