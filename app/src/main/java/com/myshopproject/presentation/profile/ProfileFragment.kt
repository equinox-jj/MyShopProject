package com.myshopproject.presentation.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.R
import com.myshopproject.databinding.FragmentProfileBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.DataStoreViewModel
import com.myshopproject.presentation.camera.CameraActivity
import com.myshopproject.presentation.login.LoginActivity
import com.myshopproject.presentation.profile.adapter.CustomSpinnerAdapter
import com.myshopproject.utils.*
import com.myshopproject.utils.Constants.IS_BACK_CAMERA_INTENT
import com.myshopproject.utils.Constants.PICTURE_INTENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    private var listLanguage = arrayOf("EN", "IN")
    private var listFlag = intArrayOf(R.drawable.ic_us_flag, R.drawable.ic_indonesia_flag)

    private lateinit var resultCamera: Bitmap
    private var getFile: File? = null

    private var userId: Int? = null

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val myFile = it.data?.getSerializableExtra(PICTURE_INTENT) as File
            val isBackCamera = it.data?.getBooleanExtra(IS_BACK_CAMERA_INTENT, true) as Boolean

            getFile = myFile
            resultCamera = rotateBitmap(
                BitmapFactory.decodeFile(myFile.absolutePath),
                isBackCamera
            )

            binding.apply {
                ivProfile.visibility = View.VISIBLE
                ivProfile.setImageBitmap(resultCamera)
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        postChangeImage()
                    }
                }
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = it.data?.data as Uri
            val file = uriToFile(uri, requireContext())
            getFile = file

            binding.apply {
                ivProfile.visibility = View.VISIBLE
                ivProfile.setImageURI(uri)
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        postChangeImage()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupListener()
        spinnerAdapter()
        initDataStore()
    }

    private fun initDataStore() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val idUser = prefViewModel.getUserId.first()
                val nameUser = prefViewModel.getNameUser.first()
                val emailUser = prefViewModel.getEmailUser.first()
                val languagePref = prefViewModel.getLanguage.first()
                val imageUser = prefViewModel.getImageUser.first()

                userId = idUser
                binding.tvUserName.text = nameUser
                binding.tvUserEmail.text = emailUser
                binding.ivProfile.load(imageUser)
                when(languagePref) {
                    0 -> binding.sSelectLanguage.setSelection(0)
                    1 -> binding.sSelectLanguage.setSelection(1)
                }

                Log.d("ProfilePref", "UserId = $idUser")
                Log.d("ProfilePref", "Name = $nameUser")
                Log.d("ProfilePref", "Email = $emailUser")
                Log.d("ProfilePref", "Language = $languagePref")
                Log.d("ProfilePref", "Image = $imageUser")
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun spinnerAdapter() {
        val customSpinnerAdapter = CustomSpinnerAdapter(requireContext(), listFlag, listLanguage)
        var isSpinnerTouched = false

        binding.apply {
            sSelectLanguage.adapter = customSpinnerAdapter
            sSelectLanguage.setOnTouchListener { _, _ ->
                isSpinnerTouched = true
                false
            }

            sSelectLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isSpinnerTouched) {
                        when (position) {
                            0 -> {
                                setLanguage("en")
                                setDialogChangeLanguage()
                                prefViewModel.saveLanguage(position)
                            }
                            1 -> {
                                setLanguage("in")
                                setDialogChangeLanguage()
                                prefViewModel.saveLanguage(position)
                            }
                        }
                    } else {
                        isSpinnerTouched = false
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun setLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireActivity().resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun postChangeImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestBodyImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multiPartImage: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestBodyImage
            )
            viewModel.changeImage(userId!!, multiPartImage)
            viewModel.state.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        binding.profileCardLoading.root.setVisibilityVisible()
                    }
                    is Resource.Success -> {
                        val imageUser = response.data?.success?.path
                        binding.profileCardLoading.root.setVisibilityGone()
                        prefViewModel.saveImageUser(imageUser!!)
                        Toast.makeText(requireContext(), "Change image success ${response.data!!.success.status}", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        binding.profileCardLoading.root.setVisibilityGone()
                        try {
                            val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                            val gson = Gson()
                            val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                            val errorResponse = gson.fromJson(jsonObject, com.myshopproject.data.source.remote.dto.ErrorResponseDTO::class.java)

                            Toast.makeText(requireContext(), "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Token Has Expired", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupListener() {
        binding.apply {
            cvLogout.setOnClickListener {
                prefViewModel.clearSession()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                activity?.finish()
            }
            cvChangePassword.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangePassActivity())
            }
            fabSelectPhotoProfile.setOnClickListener {
                alertDialogSelectImage()
            }
        }
    }

    private fun setDialogChangeLanguage() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.change_language)
            .setMessage(R.string.change_language)
            .setPositiveButton("Ok") { _, _ ->
                activity?.recreate()
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun alertDialogSelectImage() {
        val view = layoutInflater.inflate(R.layout.custom_dialog_select_image, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.Ctm_AlertDialog)
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
        val cameraIntent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(cameraIntent)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val chooser = Intent.createChooser(intent, "Select Image")
        launcherIntentGallery.launch(chooser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}