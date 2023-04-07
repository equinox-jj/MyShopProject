package com.myshopproject.presentation.auth.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.messaging.FirebaseMessaging
import com.myshopproject.R
import com.myshopproject.databinding.FragmentLoginBinding
import com.myshopproject.domain.repository.FirebaseAnalyticsRepository
import com.myshopproject.presentation.CustomLoadingDialog
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel by viewModels<LoginViewModel>()
    private val prefViewModel by viewModels<DataStoreViewModel>()

    private val loadingDialogs by lazy { CustomLoadingDialog(requireActivity()) }

    private lateinit var firebaseMessaging: FirebaseMessaging

    @Inject
    lateinit var analyticRepository: FirebaseAnalyticsRepository

    override fun onResume() {
        super.onResume()
        analyticRepository.onLoginLoadScreen(this@LoginFragment.javaClass.simpleName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}