package com.myshopproject.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.myshopproject.R
import com.myshopproject.databinding.ActivityMainBinding
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

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
                    showToolbar()
                }
                R.id.favoriteFragment -> {
                    showBottomNav()
                    showToolbar()
                }
                R.id.profileFragment -> {
                    showBottomNav()
                    hideToolbar()
                }
                else -> hideBottomNav()
            }
        }

        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun showBottomNav() {
        binding.bottomNavigation.setVisibilityVisible()
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.setVisibilityGone()
    }

    private fun showToolbar() {
        binding.toolbarMain.setVisibilityVisible()
    }

    private fun hideToolbar() {
        binding.toolbarMain.setVisibilityGone()
    }
}