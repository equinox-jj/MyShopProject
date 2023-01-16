package com.myshopproject.presentation.register

import android.Manifest
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
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.R
import com.myshopproject.data.remote.dto.ErrorResponseDTO
import com.myshopproject.databinding.ActivityRegisterBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import com.myshopproject.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import org.json.JSONObject
import java.io.File

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    private var getFile: File? = null
    private var imageMultipart : MultipartBody.Part? = null
    private lateinit var currentPhotoPath: String
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        initObserver()
    }

    private fun initObserver() {
        viewModel.state.observe(this@RegisterActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.registerCardLoading.root.setVisibilityVisible()
                }
                is Resource.Success -> {
                    binding.registerCardLoading.root.setVisibilityGone()
                    Toast.makeText(this@RegisterActivity, "${response.data!!.success.status} \n Login Successfully", Toast.LENGTH_SHORT).show()
                    alertDialogRegisSuccess()
                }
                is Resource.Error -> {
                    binding.registerCardLoading.root.setVisibilityGone()
                    val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                    val gson = Gson()
                    val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                    val errorResponse = gson.fromJson(jsonObject, ErrorResponseDTO::class.java)

                    Toast.makeText(this@RegisterActivity, "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupListener() {
//        val file = reduceFileImage(getFile as File)
//        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
//        imageMultipart = MultipartBody.Part.createFormData(
//            "image",
//            file.name,
//            requestImageFile
//        )

        binding.apply {
            btnSignUp.setOnClickListener {
                if (validation()) {
                    viewModel.registerAccount(
//                        image = ,
                        email = binding.etEmailRegister.text.toString(),
                        name = binding.etNameRegister.text.toString(),
                        phone = binding.etPhoneRegister.text.toString(),
                        password = binding.etPasswordRegister.text.toString(),
                        gender = isGender()
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
        val male = binding.rbMale
        val female = binding. rbFemale
        val genderList = listOf(male, female).firstOrNull() { it.isChecked }

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
            genderList == null -> {
                Toast.makeText(this@RegisterActivity, "Please select your gender.", Toast.LENGTH_SHORT).show()
            }
            else -> isValid = true
        }

        return isValid
    }

    private fun isGender(): Int {
        val male = binding.rbMale.isChecked
        return if (male) {
            0
        } else {
            1
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

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@RegisterActivity)
            getFile = myFile
            binding.ivPhotoProfile.setImageURI(selectedImg)
        }
    }


    private fun alertDialogSelectImage() {
        val view = layoutInflater.inflate(R.layout.custom_dialog_select_image, null)
        val builder = AlertDialog.Builder(this, R.style.Ctm_AlertDialog)

        val fromCamera = view.findViewById<TextView>(R.id.tvSelectCamera)
        val fromGallery = view.findViewById<TextView>(R.id.tvSelectGallery)

        builder.setView(view).show()
        fromCamera.setOnClickListener { openCamera() }
        fromGallery.setOnClickListener { openGallery() }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(cameraIntent)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherIntentGallery.launch(chooser)
    }

}