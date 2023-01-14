package com.myshopproject.presentation.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.myshopproject.R
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

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data as Uri

            val data: Intent? = result.data

        }
    }

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
                    binding.registerCardLoading.root.setVisibilityVisible()
                }
                is Resource.Success -> {
                    binding.registerCardLoading.root.setVisibilityGone()
                    alertDialogRegisSuccess()
                }
                is Resource.Error -> {
                    binding.registerCardLoading.root.setVisibilityGone()
                    Toast.makeText(this@RegisterActivity, "${response.errorCode}", Toast.LENGTH_SHORT).show()
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
            btnToLogin.setOnClickListener {
                finish()
            }
            ivButtonProfile.setOnClickListener {
                alertDialogSelectImage()
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

        when {
            email.isEmpty() -> {
                binding.etEmailRegister.error = "Please fill your email address."
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.etEmailRegister.error = "Please enter a valid email address."
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

    private fun alertDialogRegisSuccess() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Register Success")
        builder.setMessage("Register Successfully.")
        builder.setPositiveButton("Ok") { _, _ ->
            finish()
        }
        builder.create().show()
    }

    private fun alertDialogSelectImage() {
        val view = layoutInflater.inflate(R.layout.custom_dialog_select_image, null)
        val builder = AlertDialog.Builder(this, R.style.Ctm_AlertDialog)

        val fromCamera = view.findViewById<TextView>(R.id.tvSelectCamera)
        val fromGallery = view.findViewById<TextView>(R.id.tvSelectGallery)

        builder.setView(view).show()
        fromCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(cameraIntent)
        }
        fromGallery.setOnClickListener { }
    }

}