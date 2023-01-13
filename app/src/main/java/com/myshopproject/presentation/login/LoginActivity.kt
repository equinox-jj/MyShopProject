package com.myshopproject.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.myshopproject.databinding.ActivityLoginBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.main.MainActivity
import com.myshopproject.presentation.register.RegisterActivity
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    private var userToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        initObserver()
    }

    private fun initObserver() {
        viewModel.state.observe(this@LoginActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.loginCardLoading.root.setVisibilityVisible()
                }
                is Resource.Success -> {
                    userToken = response.data!!.accessToken
                    binding.loginCardLoading.root.setVisibilityGone()
                    Toast.makeText(this@LoginActivity, "Login Successfully Response: ${response.data!!.status}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    when {
                        response.errorCode == 400 -> {}
                    }
                    binding.loginCardLoading.root.setVisibilityGone()
                    Toast.makeText(this@LoginActivity, "Email or password not match! ${response.errorCode}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupListener() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (validation()) {
                    viewModel.loginAccount(
                        email = etEmailLogin.text.toString(),
                        password = etPasswordLogin.text.toString()
                    )
                }
            }
            btnToSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = false

        val email = binding.etEmailLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()

        when {
            email.isEmpty() -> {
                binding.tilEmailLogin.error = "Please fill your email address."
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmailLogin.error = "Please enter a valid email address."
            }
            password.isEmpty() -> {
                binding.tilPasswordLogin.error = "Please enter your password."
            }
            password.length < 6 -> {
                binding.tilPasswordLogin.error = "Password length must be 6 character."
            }
            else -> isValid = true
        }

        return isValid
    }

}