package com.myshopproject.presentation.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
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

        setupNavHostFragment()
        setupToolbarMenu()
        initDataStore()
    }

    private fun setupNavHostFragment() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavContainer) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> { showBottomNav() }
                R.id.favoriteFragment -> { showBottomNav() }
                R.id.profileFragment -> { showBottomNav() }
                else -> { hideBottomNav() }
            }
        }

        binding.bottomNavigation.setupWithNavController(navController)
    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    private fun setupToolbarMenu() {
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val badge = BadgeDrawable.create(this@MainActivity)
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        localViewModel.getAllProduct().collect { result ->
                            if (result.isNotEmpty()) {
                                badge.isVisible = true
                                badge.number = result.size
                            } else {
                                badge.isVisible = false
                            }
                        }
                    }
                }
                BadgeUtils.attachBadgeDrawable(badge, binding.toolbarMain, R.id.menu_cart)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_cart -> {
                        startActivity(Intent(this@MainActivity, TrolleyActivity::class.java))
                    }
                }
                return true
            }
        })
    }

    private fun initDataStore() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (prefViewModel.getLanguage.first()) {
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