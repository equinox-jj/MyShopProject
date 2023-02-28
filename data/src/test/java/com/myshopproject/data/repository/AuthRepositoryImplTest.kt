package com.myshopproject.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.myshopproject.data.source.remote.dto.LoginResponseDTO
import com.myshopproject.data.source.remote.network.ApiAuth
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryImplTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiAuth: ApiAuth
    private lateinit var authRepositoryImpl: AuthRepositoryImpl

    @Before
    fun setup() {
        authRepositoryImpl = AuthRepositoryImpl(apiAuth)
    }

    @Test
    fun verifySuccessResultLogin() = runTest {
        // Arrange
        Mockito
            .`when`(
                apiAuth.loginAccount(
                    email = ArgumentMatchers.anyString(),
                    password = ArgumentMatchers.anyString(),
                    firebaseToken = ArgumentMatchers.anyString()
                )
            )
            .thenReturn(LoginResponseDTO())

        // Act
        val resultFlow = authRepositoryImpl.loginAccount(
            email = "",
            password = "",
            firebaseToken = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }
    }

    @Test
    fun `test login when state is loading`() = runTest {
        // Arrange
        Mockito
            .`when`(
                apiAuth.loginAccount(
                    email = ArgumentMatchers.anyString(),
                    password = ArgumentMatchers.anyString(),
                    firebaseToken = ArgumentMatchers.anyString()
                )
            )
            .thenReturn(LoginResponseDTO())

        // Act
        val resultFlow = authRepositoryImpl.loginAccount(
            email = "",
            password = "",
            firebaseToken = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            awaitComplete()
        }
    }

    @Test
    fun `test login when state is success`() = runTest {
// Arrange
        Mockito
            .`when`(
                apiAuth.loginAccount(
                    email = ArgumentMatchers.anyString(),
                    password = ArgumentMatchers.anyString(),
                    firebaseToken = ArgumentMatchers.anyString()
                )
            )
            .thenReturn(LoginResponseDTO())

        // Act
        val resultFlow = authRepositoryImpl.loginAccount(
            email = "",
            password = "",
            firebaseToken = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }
    }

    @Test
    fun `test login when state is error`() = runTest {

    }
}