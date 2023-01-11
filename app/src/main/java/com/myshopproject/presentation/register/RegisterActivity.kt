package com.myshopproject.presentation.register

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.myshopproject.databinding.ActivityRegisterBinding
import com.myshopproject.domain.entities.DataRegister
import com.myshopproject.domain.utils.Resource
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel>()
    private var gender: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()
        initListener()
    }

    private fun initObserver() {
        viewModel.state.observe(this@RegisterActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.pbRegister.setVisibilityVisible()
                }
                is Resource.Success -> {
                    binding.pbRegister.setVisibilityGone()
                    finish()
                    Toast.makeText(this@RegisterActivity, "Successfully Registered", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    binding.pbRegister.setVisibilityGone()
                    Toast.makeText(this@RegisterActivity, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun initListener() {
        binding.apply {
            btnSignUp.setOnClickListener {
                if (validation()) {
                    viewModel.registerAccount(
                        dataUser = DataRegister(
                            email = binding.etEmailRegister.text.toString(),
                            name = binding.etNameRegister.text.toString(),
                            phone = binding.etPhoneRegister.text.toString(),
                            password = binding.etPasswordRegister.text.toString(),
                            gender = isGender(true)
                        )
                    )
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = false

        val email = binding.etEmailRegister.text.toString()
        val password = binding.etPasswordRegister.text.toString()
        val confirmPass = binding.etConfPasswordRegister.text.toString()
        val name = binding.etNameRegister.text.toString()
        val phone = binding.etPhoneRegister.text.toString()
        val male = binding.rbMale
        val female = binding.rbFemale
        val checkGender = if (binding.rbFemale.isChecked) isGender(binding.rbFemale.isChecked) else isGender(binding.rbMale.isChecked)

        when {
            email.isEmpty() -> {
                binding.tilEmailRegister.error = "Please fill your email address."
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmailRegister.error = "Please enter a valid email address."
            }
            password.isEmpty() -> {
                binding.tilPasswordRegister.error = "Please enter your password."
            }
            password.length < 6 -> {
                binding.tilPasswordRegister.error = "Password length must be 6 character."
            }
            confirmPass != password -> {
                binding.tilConfPasswordRegister.error = "Password not match."
            }
            name.isEmpty() -> {
                binding.tilNameRegister.error = "Please enter your name."
            }
            phone.isEmpty() -> {
                binding.tilPhoneRegister.error = "Please enter your phone number."
            }
            else -> isValid = true
        }

        return isValid
    }

    private fun isGender(isChecked: Boolean): Int {
        val female = binding.rbFemale.isChecked
        return if (isChecked == female) {
            1
        } else {
            0
        }
    }

}