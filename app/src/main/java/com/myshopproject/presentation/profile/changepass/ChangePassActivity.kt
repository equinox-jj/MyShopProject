package com.myshopproject.presentation.profile.changepass

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.data.remote.dto.ErrorResponseDTO
import com.myshopproject.databinding.ActivityChangePassBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class ChangePassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePassBinding

    private val viewModel by viewModels<ChangePassViewModel>()

    private var userId: Int? = null
    private var authorization: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarChangePass)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupListener()
        initObserver()
        initDataStore()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initDataStore() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authorization = viewModel.getAuthorization.first()
                userId = viewModel.getUserId.first()
            }
        }
    }

    private fun initObserver() {
        viewModel.state.observe(this@ChangePassActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.changePassCardLoading.root.setVisibilityVisible()
                }
                is Resource.Success -> {
                    binding.changePassCardLoading.root.setVisibilityGone()

                    Toast.makeText(this@ChangePassActivity, "${response.data!!.success.status} \n Login Successfully", Toast.LENGTH_SHORT).show()

                    finish()
                }
                is Resource.Error -> {
                    try {
                        binding.changePassCardLoading.root.setVisibilityGone()
                        val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                        val gson = Gson()
                        val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                        val errorResponse = gson.fromJson(jsonObject, ErrorResponseDTO::class.java)

                        Toast.makeText(this@ChangePassActivity, "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@ChangePassActivity, "Token Has Expired", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupListener() {
        binding.apply {
            btnSaveNewPass.setOnClickListener {
                if (validation()) {
                    viewModel.changePassword(
                        id = userId!!,
                        password = binding.etOldPass.text.toString(),
                        newPassword = binding.etNewPass.text.toString(),
                        confirmPassword = binding.etConfirmNewPass.text.toString()
                    )
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = false

        val oldPassword = binding.etOldPass.text.toString()
        val newPassword = binding.etNewPass.text.toString()
        val confirmNewPass = binding.etConfirmNewPass.text.toString()

        when {
            oldPassword.isEmpty() -> {
                binding.tilOldPass.error = "Please enter your old password."
            }
            newPassword.isEmpty() -> {
                binding.tilNewPass.error = "Please enter your new password."
            }
            confirmNewPass != newPassword -> {
                binding.tilConfirmNewPass.error = "Password not match."
            }
            else -> isValid = true
        }

        return isValid
    }

}