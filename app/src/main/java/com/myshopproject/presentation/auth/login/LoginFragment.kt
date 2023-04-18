package com.myshopproject.presentation.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.myshopproject.R
import com.myshopproject.databinding.FragmentLoginBinding
import com.myshopproject.domain.repository.FirebaseAnalyticsRepository
import com.myshopproject.presentation.CustomLoadingDialog
import com.myshopproject.presentation.mains.MainActivity
import com.myshopproject.presentation.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        firebaseMessaging = Firebase.messaging
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.state.collect() {
                    when {
                        it.isLoading -> {
                            loadingDialogs.showDialog()
                        }
                        it.isSuccess != null -> {
                            val refreshToken = it.isSuccess.refreshToken
                            val accessToken = it.isSuccess.accessToken
                            val idUser = it.isSuccess.dataUser.id
                            val emailUser = it.isSuccess.dataUser.email
                            val nameUser = it.isSuccess.dataUser.name
                            val imageUser = it.isSuccess.dataUser.path

                            loadingDialogs.hideDialog()

                            prefViewModel.saveAuthRefresh(
                                idUser!!,
                                accessToken!!,
                                refreshToken!!
                            )
                            prefViewModel.saveEmailUser(emailUser!!)
                            prefViewModel.saveNameUser(nameUser!!)
                            prefViewModel.saveImageUser(imageUser!!)

                            analyticRepository.onLoginButtonClick(emailUser)

                            Toast.makeText(requireContext(), getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                        }
                        it.isError != null -> {
                            loadingDialogs.hideDialog()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}