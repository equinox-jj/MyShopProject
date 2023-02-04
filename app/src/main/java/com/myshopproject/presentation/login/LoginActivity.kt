package com.myshopproject.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.data.source.remote.dto.ErrorResponseDTO
import com.myshopproject.databinding.ActivityLoginBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.main.MainActivity
import com.myshopproject.presentation.register.RegisterActivity
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

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
                    binding.loginCardLoading.root.show()
                }
                is Resource.Success -> {
                    val refreshToken = response.data?.refreshToken
                    val accessToken = response.data?.accessToken
                    val idUser = response.data?.dataUser?.id
                    val emailUser = response.data?.dataUser?.email
                    val nameUser = response.data?.dataUser?.name
                    val imageUser = response.data?.dataUser?.path

                    prefViewModel.saveAuthRefresh(
                        idUser!!,
                        accessToken!!,
                        refreshToken!!
                    )
                    prefViewModel.saveEmailUser(emailUser!!)
                    prefViewModel.saveNameUser(nameUser!!)
                    prefViewModel.saveImageUser(imageUser!!)
                    binding.loginCardLoading.root.hide()
                    Toast.makeText(this@LoginActivity, "Login Successfully ${response.data!!.status}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    binding.loginCardLoading.root.hide()
                    val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                    val gson = Gson()
                    val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                    val errorResponse = gson.fromJson(jsonObject, ErrorResponseDTO::class.java)

                    Toast.makeText(this@LoginActivity, "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
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