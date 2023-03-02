package com.myshopproject.presentation.login

import com.myshopproject.domain.usecase.RemoteUseCase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @Mock
    private lateinit var remoteUseCase: RemoteUseCase
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(remoteUseCase)
    }

    @Test
    fun `test login function when state is success`() {

    }
}