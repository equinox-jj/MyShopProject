package com.myshopproject.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.myshopproject.databinding.ActivitySplashBinding
import com.myshopproject.domain.repository.FirebaseAnalyticsRepository
import com.myshopproject.presentation.login.LoginActivity
import com.myshopproject.presentation.main.MainActivity
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val prefViewModel by viewModels<DataStoreViewModel>()

    @Inject
    lateinit var analyticRepository: FirebaseAnalyticsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    prefViewModel.getAccessToken.collect {
                        if (it.isNotEmpty()) {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        } else {
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }, 3000)
    }

    override fun onResume() {
        super.onResume()
        analyticRepository.onSplashLoadScreen(this@SplashActivity.javaClass.simpleName)
    }
}