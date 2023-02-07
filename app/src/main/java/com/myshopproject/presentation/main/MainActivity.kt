package com.myshopproject.presentation.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.myshopproject.R
import com.myshopproject.databinding.ActivityMainBinding
import com.myshopproject.presentation.trolley.TrolleyActivity
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.presentation.viewmodel.LocalViewModel
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private val prefViewModel by viewModels<DataStoreViewModel>()
    private val localViewModel by viewModels<LocalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavContainer) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    showBottomNav()
                    binding.ivToolbarMainLogo.show()
                    binding.icCart.show()
//                    supportActionBar?.show()
                }
                R.id.favoriteFragment -> {
                    showBottomNav()
                    binding.ivToolbarMainLogo.show()
                    binding.icCart.show()
//                    supportActionBar?.show()
                }
                R.id.profileFragment -> {
                    showBottomNav()
                    binding.ivToolbarMainLogo.hide()
                    binding.icCart.hide()
//                    supportActionBar?.hide()
                }
                else -> {
                    hideBottomNav()
                }
            }
        }

        binding.bottomNavigation.setupWithNavController(navController)

        initDataStore()
        badgesCount()
        setupListener()
    }

    private fun setupListener() {
        binding.icCart.setOnClickListener {
            startActivity(Intent(this@MainActivity, TrolleyActivity::class.java))
        }
    }

    private fun badgesCount() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                localViewModel.getAllProduct().collect { result ->
                    if (result.isNotEmpty()) {
                        binding.imgBadges.show()
                        binding.tvBadgesMenu.show()
                        binding.tvBadgesMenu.text = result.size.toString()
                    } else {
                        binding.imgBadges.hide()
                        binding.tvBadgesMenu.hide()
                    }
                }
            }
        }
    }

    private fun initDataStore() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                when(prefViewModel.getLanguage.first()) {
                    0 -> setLanguage("en")
                    1 -> setLanguage("in")
                }
            }
        }
    }

    private fun setLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        this.resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun showBottomNav() {
        binding.bottomNavigation.show()
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.hide()
    }
}