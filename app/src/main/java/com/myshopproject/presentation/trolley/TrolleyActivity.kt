package com.myshopproject.presentation.trolley

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.myshopproject.databinding.ActivityTrolleyBinding
import com.myshopproject.presentation.trolley.adapter.TrolleyAdapter
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrolleyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrolleyBinding

    private var trolleyAdapter: TrolleyAdapter? = null
    private val viewModel by viewModels<TrolleyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrolleyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTrolley)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initObserver()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.apply {
            trolleyAdapter = TrolleyAdapter()
            rvTrolley.adapter = trolleyAdapter
            rvTrolley.setHasFixedSize(true)
        }
    }

    private fun initObserver() {
        viewModel.getAllCarts.observe(this) { entity ->
            if (entity.isNotEmpty()) {
                binding.rvTrolley.setVisibilityVisible()
                trolleyAdapter?.submitData(entity)
            } else {
                binding.rvTrolley.setVisibilityGone()
            }
        }
    }
}