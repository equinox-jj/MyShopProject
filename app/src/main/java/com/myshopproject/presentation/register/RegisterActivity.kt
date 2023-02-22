package com.myshopproject.presentation.register

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.R
import com.myshopproject.databinding.ActivityRegisterBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.CustomLoadingDialog
import com.myshopproject.presentation.camera.CameraActivity
import com.myshopproject.utils.Constants.IS_BACK_CAMERA_INTENT
import com.myshopproject.utils.Constants.PICTURE_INTENT
import com.myshopproject.utils.reduceFileImage
import com.myshopproject.utils.rotateBitmap
import com.myshopproject.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    private lateinit var loadingDialog: CustomLoadingDialog
    private lateinit var resultCamera: Bitmap
    private var getFile: File? = null

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = it.data?.getSerializableExtra(PICTURE_INTENT) as File
            val isBackCamera = it.data?.getBooleanExtra(IS_BACK_CAMERA_INTENT, true) as Boolean

            getFile = myFile
            resultCamera = rotateBitmap(
                BitmapFactory.decodeFile(myFile.absolutePath),
                isBackCamera
            )
            binding.apply {
                ivPhotoProfile.visibility = View.VISIBLE
                ivPhotoProfile.setImageBitmap(resultCamera)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val uri = it.data?.data as Uri
            val file = uriToFile(uri, this@RegisterActivity)
            getFile = file

            binding.apply {
                ivPhotoProfile.visibility = View.VISIBLE
                ivPhotoProfile.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = CustomLoadingDialog(this@RegisterActivity)
        setupListener()
        initObserver()
    }

    private fun initObserver() {
        viewModel.state.observe(this@RegisterActivity) { response ->
            when (response) {
                is Resource.Loading -> {
                    loadingDialog.showDialog()
                }
                is Resource.Success -> {
                    loadingDialog.hideDialog()
                    alertDialogRegisSuccess()
                    Toast.makeText(this@RegisterActivity, "Register Successfully.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    loadingDialog.hideDialog()
                    Toast.makeText(this@RegisterActivity, response.message, Toast.LENGTH_SHORT).show()
                    try {
                        val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                        val gson = Gson()
                        val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                        val errorResponse = gson.fromJson(jsonObject, com.myshopproject.data.source.remote.dto.ErrorResponseDTO::class.java)

                        Toast.makeText(this@RegisterActivity, "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                    } catch (_: Exception) { }
                }
            }
        }
    }

    private fun setupListener() {
        binding.apply {
            btnSignUp.setOnClickListener {
                if (validation() && getFile != null) {
                    val file = reduceFileImage(getFile as File)
                    val requestBodyImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val multiPartImage: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestBodyImage)
                    viewModel.registerAccount(
                        image = multiPartImage,
                        email = binding.etEmailRegister.text.toString().toRequestBody("text/plain".toMediaType()),
                        name = binding.etNameRegister.text.toString().toRequestBody("text/plain".toMediaType()),
                        phone = binding.etPhoneRegister.text.toString().toRequestBody("text/plain".toMediaType()),
                        password = binding.etPasswordRegister.text.toString().toRequestBody("text/plain".toMediaType()),
                        gender = isGender()
                    )
                }
            }
            btnToLogin.setOnClickListener {
                finish()
            }
            fabButtonProfile.setOnClickListener {
                alertDialogSelectImage()
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = false

        val email = binding.etEmailRegister.text.toString().trim()
        val password = binding.etPasswordRegister.text.toString().trim()
        val confirmPass = binding.etConfPasswordRegister.text.toString().trim()
        val name = binding.etNameRegister.text.toString().trim()
        val phone = binding.etPhoneRegister.text.toString().trim()
        val male = binding.rbMale
        val female = binding.rbFemale
        val genderList = listOf(male, female).firstOrNull { it.isChecked }

        when {
            email.isEmpty() -> {
                binding.tilEmailRegister.error = "Please fill your email address."
                binding.tilEmailRegister.requestFocus()
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmailRegister.error = "Please enter a valid email address."
                binding.tilEmailRegister.requestFocus()
            }
            password.isEmpty() -> {
                binding.tilPasswordRegister.error = "Please enter your password."
                binding.tilPasswordRegister.requestFocus()
            }
            password.length < 6 ->{
                binding.tilPasswordRegister.error = "Password length must be 6 characters."
                binding.tilPasswordRegister.requestFocus()
            }
            confirmPass != password -> {
                binding.tilConfPasswordRegister.error = "Password not match."
                binding.tilConfPasswordRegister.requestFocus()
            }
            name.isEmpty() -> {
                binding.tilNameRegister.error = "Please enter your name."
                binding.tilNameRegister.requestFocus()
            }
            phone.isEmpty() -> {
                binding.tilPhoneRegister.error = "Please enter your phone number."
                binding.tilPhoneRegister.requestFocus()
            }
            genderList == null -> {
                Toast.makeText(this@RegisterActivity, "Please select your gender.", Toast.LENGTH_SHORT).show()
            }
            getFile == null -> {
                Toast.makeText(this@RegisterActivity, "Please upload your profile photo.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                isValid = true
            }
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

    private fun alertDialogSelectImage() {
        val view = layoutInflater.inflate(R.layout.custom_dialog_select_image, null)
        val builder = AlertDialog.Builder(this, R.style.Ctm_AlertDialog)
        val showDialog = builder.setView(view).show()

        val fromCamera = view.findViewById<TextView>(R.id.tvSelectCamera)
        val fromGallery = view.findViewById<TextView>(R.id.tvSelectGallery)

        fromCamera.setOnClickListener {
            openCamera()
            showDialog.dismiss()
        }
        fromGallery.setOnClickListener {
            openGallery()
            showDialog.dismiss()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(this@RegisterActivity, CameraActivity::class.java)
        launcherIntentCameraX.launch(cameraIntent)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val chooser = Intent.createChooser(intent, "Select Image")
        launcherIntentGallery.launch(chooser)
    }

}