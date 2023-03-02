package com.myshopproject.data.repository

import app.cash.turbine.test
import com.myshopproject.data.mapper.toDomain
import com.myshopproject.data.source.remote.dto.*
import com.myshopproject.data.source.remote.network.ApiAuth
import com.myshopproject.data.utils.dummyChangeImageResponse
import com.myshopproject.data.utils.dummyLoginResponse
import com.myshopproject.data.utils.dummySuccessResponse
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryImplTest {

    @Mock
    private lateinit var apiAuth: ApiAuth
    @Mock
    private var file = File("")
    private lateinit var authRepositoryImpl: AuthRepositoryImpl

    @Before
    fun setup() {
        authRepositoryImpl = AuthRepositoryImpl(apiAuth)
    }

    @Test
    fun `test login when state is success`() = runTest {
        val dataDummy = dummyLoginResponse()
        // Arrange
        `when`(
            apiAuth.loginAccount(
                email = ArgumentMatchers.anyString(),
                password = ArgumentMatchers.anyString(),
                firebaseToken = ArgumentMatchers.anyString()
            )
        ).thenReturn(dataDummy)

        // Act
        val resultFlow = authRepositoryImpl.loginAccount(
            email = ArgumentMatchers.anyString(),
            password = ArgumentMatchers.anyString(),
            firebaseToken = ArgumentMatchers.anyString()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }
    }

    @Test
    fun `test login when state is error 400`() = runTest {
        val response = Response.error<LoginResponseDTO>(400, "".toResponseBody(null))

        // Arrange
        `when`(
            apiAuth.loginAccount(
                email = ArgumentMatchers.anyString(),
                password = ArgumentMatchers.anyString(),
                firebaseToken = ArgumentMatchers.anyString()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = authRepositoryImpl.loginAccount(
            email = ArgumentMatchers.anyString(),
            password = ArgumentMatchers.anyString(),
            firebaseToken = ArgumentMatchers.anyString()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test login when state is error 401`() = runTest {
        val response = Response.error<LoginResponseDTO>(401, "".toResponseBody(null))

        // Arrange
        `when`(
            apiAuth.loginAccount(
                email = ArgumentMatchers.anyString(),
                password = ArgumentMatchers.anyString(),
                firebaseToken = ArgumentMatchers.anyString()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = authRepositoryImpl.loginAccount(
            email = ArgumentMatchers.anyString(),
            password = ArgumentMatchers.anyString(),
            firebaseToken = ArgumentMatchers.anyString()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test login when state is error Exception`() = runTest {
        // Arrange
        `when`(
            apiAuth.loginAccount(
                email = ArgumentMatchers.anyString(),
                password = ArgumentMatchers.anyString(),
                firebaseToken = ArgumentMatchers.anyString()
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = authRepositoryImpl.loginAccount(
            email = ArgumentMatchers.anyString(),
            password = ArgumentMatchers.anyString(),
            firebaseToken = ArgumentMatchers.anyString()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test register when state is success`() = runTest {
        val dummyData = dummySuccessResponse()

        val mockFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageTest: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            mockFile
        )

        val email = "".toRequestBody()
        val password = "".toRequestBody()
        val name = "".toRequestBody()
        val phone = "".toRequestBody()

        // Arrange
        `when`(
            apiAuth.registerAccount(
                email = email,
                image = imageTest,
                password = password,
                name = name,
                phone = phone,
                gender = 0
            )
        ).thenReturn(dummyData)

        // Act
        val resultFlow = authRepositoryImpl.registerAccount(
            email = email,
            image = imageTest,
            password = password,
            name = name,
            phone = phone,
            gender = 0
        )

        // Assert
        resultFlow.test {
            assertEquals(awaitItem(), Resource.Loading)
            val actualData = awaitItem() as Resource.Success
            assertNotNull(actualData)
            assertEquals(dummyData.toDomain(), actualData.data)
            awaitComplete()
        }
    }

    @Test
    fun `test register when state is error 400`() = runTest {
        val response = Response.error<LoginResponseDTO>(400, "".toResponseBody(null))
        val mockFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageTest: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            mockFile
        )

        val email = "".toRequestBody()
        val password = "".toRequestBody()
        val name = "".toRequestBody()
        val phone = "".toRequestBody()

        // Arrange
        `when`(
            apiAuth.registerAccount(
                email = email,
                image = imageTest,
                password = password,
                name = name,
                phone = phone,
                gender = 0
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = authRepositoryImpl.registerAccount(
            email = email,
            image = imageTest,
            password = password,
            name = name,
            phone = phone,
            gender = 0
        )

        // Assert
        resultFlow.test {
            assertEquals(awaitItem(), Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test register when state is error 401`() = runTest {
        val response = Response.error<LoginResponseDTO>(401, "".toResponseBody(null))
        val mockFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageTest: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            mockFile
        )

        val email = "".toRequestBody()
        val password = "".toRequestBody()
        val name = "".toRequestBody()
        val phone = "".toRequestBody()

        // Arrange
        `when`(
            apiAuth.registerAccount(
                email = email,
                image = imageTest,
                password = password,
                name = name,
                phone = phone,
                gender = 0
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = authRepositoryImpl.registerAccount(
            email = email,
            image = imageTest,
            password = password,
            name = name,
            phone = phone,
            gender = 0
        )

        // Assert
        resultFlow.test {
            assertEquals(awaitItem(), Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test register when state is error Exception`() = runTest {
        val mockFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageTest: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            mockFile
        )

        val email = "".toRequestBody()
        val password = "".toRequestBody()
        val name = "".toRequestBody()
        val phone = "".toRequestBody()

        // Arrange
        `when`(
            apiAuth.registerAccount(
                email = email,
                image = imageTest,
                password = password,
                name = name,
                phone = phone,
                gender = 0
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = authRepositoryImpl.registerAccount(
            email = email,
            image = imageTest,
            password = password,
            name = name,
            phone = phone,
            gender = 0
        )

        // Assert
        resultFlow.test {
            assertEquals(awaitItem(), Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test change password when state is success`() = runTest {
        val dummyData = dummySuccessResponse()

        // Arrange
        `when`(
            apiAuth.changePassword(
                id = ArgumentMatchers.anyInt(),
                password = ArgumentMatchers.anyString(),
                newPassword = ArgumentMatchers.anyString(),
                confirmPassword = ArgumentMatchers.anyString()
            )
        ).thenReturn(dummySuccessResponse())

        // Act
        val resultFlow = authRepositoryImpl.changePassword(
            id = ArgumentMatchers.anyInt(),
            password = ArgumentMatchers.anyString(),
            newPassword = ArgumentMatchers.anyString(),
            confirmPassword = ArgumentMatchers.anyString()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }
    }

    @Test
    fun `test change password when state is error 400`() = runTest {
        val response = Response.error<LoginResponseDTO>(401, "".toResponseBody(null))
        // Arrange
        `when`(
            apiAuth.changePassword(
                id = ArgumentMatchers.anyInt(),
                password = ArgumentMatchers.anyString(),
                newPassword = ArgumentMatchers.anyString(),
                confirmPassword = ArgumentMatchers.anyString()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = authRepositoryImpl.changePassword(
            id = ArgumentMatchers.anyInt(),
            password = ArgumentMatchers.anyString(),
            newPassword = ArgumentMatchers.anyString(),
            confirmPassword = ArgumentMatchers.anyString()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test change password when state is error 401`() = runTest {
        val response = Response.error<LoginResponseDTO>(401, "".toResponseBody(null))
        // Arrange
        `when`(
            apiAuth.changePassword(
                id = ArgumentMatchers.anyInt(),
                password = ArgumentMatchers.anyString(),
                newPassword = ArgumentMatchers.anyString(),
                confirmPassword = ArgumentMatchers.anyString()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = authRepositoryImpl.changePassword(
            id = ArgumentMatchers.anyInt(),
            password = ArgumentMatchers.anyString(),
            newPassword = ArgumentMatchers.anyString(),
            confirmPassword = ArgumentMatchers.anyString()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test change image when state is success`() = runTest {
        val dummyData = dummyChangeImageResponse()
        val imageTest: MultipartBody.Part = MultipartBody.Part.create("text".toRequestBody())

        // Arrange
        `when`(
            apiAuth.changeImage(
                id = 0,
                image = imageTest
            )
        ).thenReturn(dummyData)

        // Act
        val resultFlow = authRepositoryImpl.changeImage(
            id = 0,
            image = imageTest
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Success)
            awaitComplete()
        }
    }

    @Test
    fun `test change image when state is error 400`() = runTest {
        val response = Response.error<LoginResponseDTO>(400, "".toResponseBody(null))
        val imageTest: MultipartBody.Part = MultipartBody.Part.create("text".toRequestBody())
        // Arrange
        `when`(
            apiAuth.changeImage(
                id = 0,
                image = imageTest
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = authRepositoryImpl.changeImage(
            id = 0,
            image = imageTest
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test change image when state is error 401`() = runTest {
        val response = Response.error<LoginResponseDTO>(401, "".toResponseBody(null))
        val imageTest: MultipartBody.Part = MultipartBody.Part.create("text".toRequestBody())

        // Arrange
        `when`(
            apiAuth.changeImage(
                id = 0,
                image = imageTest
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = authRepositoryImpl.changeImage(
            id = 0,
            image = imageTest
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test change image when state is error Exception`() = runTest {
        val imageTest: MultipartBody.Part = MultipartBody.Part.create("text".toRequestBody())

        // Arrange
        `when`(
            apiAuth.changeImage(
                id = 0,
                image = imageTest
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = authRepositoryImpl.changeImage(
            id = 0,
            image = imageTest
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }
}