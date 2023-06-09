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
import com.myshopproject.domain.repository.FirebaseAnalyticsRepository
import com.myshopproject.presentation.notification.NotificationActivity
import com.myshopproject.presentation.trolley.TrolleyActivity
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.presentation.viewmodel.LocalViewModel
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private val prefViewModel by viewModels<DataStoreViewModel>()
    private val localViewModel by viewModels<LocalViewModel>()

    @Inject
    lateinit var analyticRepository: FirebaseAnalyticsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavHostFragment()
        setupToolbar()
        initDataStore()
    }

    private fun setupNavHostFragment() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavContainer) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    showBottomNav()
                    binding.toolbarMain.show()
                }
                R.id.favoriteFragment -> {
                    showBottomNav()
                    binding.toolbarMain.show()
                }
                R.id.profileFragment -> {
                    showBottomNav()
                    binding.toolbarMain.hide()
                }
                else -> { hideBottomNav() }
            }
        }

        binding.bottomNavigation.setupWithNavController(navController)
    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val iconBadgeTrolley = BadgeDrawable.create(this@MainActivity)
                val iconBadgeNotification = BadgeDrawable.create(this@MainActivity)
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        launch {
                            localViewModel.getAllProduct().collect { result ->
                                if (result.isNotEmpty()) {
                                    iconBadgeTrolley.isVisible = true
                                    iconBadgeTrolley.number = result.size
                                    BadgeUtils.attachBadgeDrawable(iconBadgeTrolley, binding.toolbarMain, R.id.menu_cart)
                                } else {
                                    iconBadgeTrolley.isVisible = false
                                    BadgeUtils.detachBadgeDrawable(iconBadgeTrolley, binding.toolbarMain, R.id.menu_cart)
                                }
                            }
                        }
                        launch {
                            localViewModel.getAllNotification().collect { result ->
                                val unreadNotification = result.filter { !it.isRead }

                                if (unreadNotification.isNotEmpty()) {
                                    iconBadgeNotification.isVisible = true
                                    iconBadgeNotification.number = unreadNotification.size
                                    BadgeUtils.attachBadgeDrawable(iconBadgeNotification, binding.toolbarMain, R.id.menu_notification)
                                } else {
                                    iconBadgeNotification.isVisible = false
                                    BadgeUtils.detachBadgeDrawable(iconBadgeNotification, binding.toolbarMain, R.id.menu_notification)
                                }
                            }
                        }
                    }
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_cart -> {
                        analyticRepository.onClickTrolleyIcon()
                        startActivity(Intent(this@MainActivity, TrolleyActivity::class.java))
                    }
                    R.id.menu_notification -> {
                        analyticRepository.onClickNotificationIcon()
                        startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
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