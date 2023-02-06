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
import com.myshopproject.databinding.ActivityChangePassBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import com.myshopproject.utils.hide
import com.myshopproject.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class ChangePassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePassBinding

    private val viewModel by viewModels<ChangePassViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    private var userId: Int? = null
    private var authorization: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        initObserver()
        initDataStore()
        launchCoroutines()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarChangePass)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun launchCoroutines() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setupListener()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initDataStore() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authorization = prefViewModel.getRefreshToken.first()
                userId = prefViewModel.getUserId.first()
            }
        }
    }

    private fun initObserver() {
        viewModel.state.observe(this@ChangePassActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.changePassCardLoading.root.show()
                }
                is Resource.Success -> {
                    binding.changePassCardLoading.root.hide()

                    Toast.makeText(this@ChangePassActivity, "Change password success ${response.data!!.success.status}", Toast.LENGTH_SHORT).show()

                    finish()
                }
                is Resource.Error -> {
                    try {
                        binding.changePassCardLoading.root.hide()
                        val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                        val gson = Gson()
                        val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                        val errorResponse = gson.fromJson(jsonObject, com.myshopproject.data.source.remote.dto.ErrorResponseDTO::class.java)

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