package com.myshopproject.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.myshopproject.R
import com.myshopproject.databinding.FragmentProfileBinding
import com.myshopproject.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupListener()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getNameUser.collect() {
                        binding.tvUserName.text = it
                    }
                }
                launch {
                    viewModel.getEmailUser.collect() {
                        binding.tvUserEmail.text = it
                    }
                }
            }
        }
    }

    private fun setupListener() {
        binding.apply {
            cvLogout.setOnClickListener {
                viewModel.removeSession()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                activity?.finish()
            }
            cvChangePassword.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangePassFragment())
            }
            ivSelectPhotoProfile.setOnClickListener {
                alertDialogSelectImage()
            }
        }
    }

    private fun alertDialogSelectImage() {
        val view = layoutInflater.inflate(R.layout.custom_dialog_select_image, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.Ctm_AlertDialog)

        val fromCamera = view.findViewById<TextView>(R.id.tvSelectCamera)
        val fromGallery = view.findViewById<TextView>(R.id.tvSelectGallery)

        builder.setView(view).show()
        fromCamera.setOnClickListener {

        }
        fromGallery.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}