package com.myshopproject.presentation.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
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
import com.myshopproject.data.utils.Constants.CAMERA
import com.myshopproject.data.utils.Constants.ENGLISH
import com.myshopproject.data.utils.Constants.GALLERY
import com.myshopproject.data.utils.Constants.INDO
import com.myshopproject.databinding.FragmentProfileBinding
import com.myshopproject.domain.repository.FirebaseAnalyticsRepository
import com.myshopproject.domain.utils.Resource
import com.myshopproject.presentation.camera.CameraActivity
import com.myshopproject.presentation.login.LoginActivity
import com.myshopproject.presentation.profile.adapter.CustomSpinnerAdapter
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
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
import javax.inject.Inject

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

    private var userId = 0

    @Inject
    lateinit var analyticRepository: FirebaseAnalyticsRepository

    private val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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
                postChangeImage()
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = it.data?.data as Uri
            val file = uriToFile(uri, requireContext())
            getFile = file

            binding.apply {
                ivProfile.visibility = View.VISIBLE
                ivProfile.setImageURI(uri)
                postChangeImage()
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

    override fun onResume() {
        super.onResume()
        analyticRepository.onProfileLoadScreen(requireContext().javaClass.simpleName)
    }

    private fun initDataStore() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val idUser = prefViewModel.getUserId.first()
                val nameUser = prefViewModel.getNameUser.first()
                val emailUser = prefViewModel.getEmailUser.first()
                val imageUser = prefViewModel.getImageUser.first()

                userId = idUser
                binding.tvUserName.text = nameUser
                binding.tvUserEmail.text = emailUser
                binding.ivProfile.load(imageUser)
                prefViewModel.getLanguage.collect {
                    when(it) {
                        0 -> {
                            binding.sSelectLanguage.setSelection(0)
                            setLanguage("en")
                        }
                        1 -> {
                            binding.sSelectLanguage.setSelection(1)
                            setLanguage("in")
                        }
                    }
                }
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
                                analyticRepository.onChangeLanguage(ENGLISH)
                                setLanguage("EN")
                                prefViewModel.saveLanguage(position)
                                requireActivity().recreate()
                            }
                            1 -> {
                                analyticRepository.onChangeLanguage(INDO)
                                setLanguage("ID")
                                prefViewModel.saveLanguage(position)
                                requireActivity().recreate()
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
            viewModel.changeImage(userId, multiPartImage)
            viewModel.state.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        binding.profileCardLoading.root.show()
                    }
                    is Resource.Success -> {
                        val imageUser = response.data?.success?.path
                        binding.profileCardLoading.root.hide()
                        prefViewModel.saveImageUser(imageUser.toString())
                        Toast.makeText(requireContext(), "Change image success", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        try {
                            binding.profileCardLoading.root.hide()

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
                analyticRepository.onClickLogout()
                prefViewModel.clearSession()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                activity?.finish()
            }
            cvChangePassword.setOnClickListener {
                analyticRepository.onClickChangePassword()
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangePassActivity())
            }
            fabSelectPhotoProfile.setOnClickListener {
                analyticRepository.onClickCameraIconProfile()
                alertDialogSelectImage()
            }
        }
    }

    private fun alertDialogSelectImage() {
        val view = layoutInflater.inflate(R.layout.custom_dialog_select_image, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.Ctm_AlertDialog)
        val showDialog = builder.setView(view).show()

        val fromCamera = view.findViewById<TextView>(R.id.tvSelectCamera)
        val fromGallery = view.findViewById<TextView>(R.id.tvSelectGallery)

        fromCamera.setOnClickListener {
            analyticRepository.onChangeImageProfile(CAMERA)
            openCamera()
            showDialog.dismiss()
        }
        fromGallery.setOnClickListener {
            analyticRepository.onChangeImageProfile(GALLERY)
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