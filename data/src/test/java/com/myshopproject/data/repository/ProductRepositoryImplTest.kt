package com.myshopproject.data.repository

import com.myshopproject.data.source.local.dao.FirebaseMessageDao
import com.myshopproject.data.source.local.dao.ProductCartDao
import com.myshopproject.data.source.remote.network.ApiProduct
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductRepositoryImplTest {

    @Mock
    private lateinit var apiProduct: ApiProduct
    @Mock
    private lateinit var cartDao: ProductCartDao
    @Mock
    private lateinit var fcmDao: FirebaseMessageDao
    private lateinit var productRepositoryImpl: ProductRepositoryImpl

    @Before
    fun setup() {
        productRepositoryImpl = ProductRepositoryImpl(apiProduct, cartDao, fcmDao)
    }

    @Test
    fun `test product favorite when state is success`() = runTest {
        // Arrange
//        Mockito.`when`(
//            apiProduct.getListProductFavorite(
//                query = ArgumentMatchers.anyString(),
//                userId = ArgumentMatchers.anyInt()
//            )
//        ).thenReturn(
//            DataProductResponseDTO(
//                DataProductResultDTO(
//                    data =
//                )
//            )
//        )

        // Act
        val resultFlow = apiProduct.getListProductFavorite(
            query = ArgumentMatchers.anyString(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
//        resultFlow.test {
//            assertTrue(awaitItem() is Resource.Loading)
//            assertTrue(awaitItem() is Resource.Success)
//            awaitComplete()
//        }
    }
}