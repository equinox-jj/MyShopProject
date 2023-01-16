package com.myshopproject.presentation.profile.changepass

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myshopproject.R
import com.myshopproject.data.remote.dto.ErrorResponseDTO
import com.myshopproject.databinding.FragmentChangePassBinding
import com.myshopproject.domain.utils.Resource
import com.myshopproject.utils.setVisibilityGone
import com.myshopproject.utils.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class ChangePassFragment : Fragment(R.layout.fragment_change_pass) {

    private var _binding: FragmentChangePassBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ChangePassViewModel>()

    private var userId: Int? = null
    private var authorization: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChangePassBinding.bind(view)

        setupListener()
        initObserver()
    }

    private fun initObserver() {
        viewModel.state.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.changePassCardLoading.root.setVisibilityVisible()
                }
                is Resource.Success -> {
                    binding.changePassCardLoading.root.setVisibilityGone()
                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), "${response.data!!.success.status} \n Login Successfully", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    binding.changePassCardLoading.root.setVisibilityGone()
                    val errors = response.errorBody?.string()?.let { JSONObject(it).toString() }
                    val gson = Gson()
                    val jsonObject = gson.fromJson(errors, JsonObject::class.java)
                    val errorResponse = gson.fromJson(jsonObject, ErrorResponseDTO::class.java)

                    Toast.makeText(requireContext(), "${errorResponse.error.message} ${errorResponse.error.status}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getAuthorization.collect {
                        authorization = it
                    }
                }
                launch {
                    viewModel.getUserId.collect {
                        userId = it
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
                        authorization = authorization!!,
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}