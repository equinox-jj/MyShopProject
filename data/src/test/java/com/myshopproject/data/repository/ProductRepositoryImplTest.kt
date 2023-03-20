package com.myshopproject.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.myshopproject.data.mapper.toDomain
import com.myshopproject.data.source.local.dao.FirebaseMessageDao
import com.myshopproject.data.source.local.dao.ProductCartDao
import com.myshopproject.data.source.remote.dto.DataProductResponseDTO
import com.myshopproject.data.source.remote.dto.DetailProductResponseDTO
import com.myshopproject.data.source.remote.dto.SuccessResponseDTO
import com.myshopproject.data.source.remote.network.ApiProduct
import com.myshopproject.data.utils.*
import com.myshopproject.domain.entities.DataProduct
import com.myshopproject.domain.entities.UpdateStockItem
import com.myshopproject.domain.entities.UpdateStockProduct
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductRepositoryImplTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var apiProduct: ApiProduct
    @Mock private lateinit var cartDao: ProductCartDao
    @Mock private lateinit var fcmDao: FirebaseMessageDao
    private lateinit var productRepositoryImpl: ProductRepositoryImpl

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<DataProduct>() {
            override fun areItemsTheSame(oldItem: DataProduct, newItem: DataProduct) = oldItem.id == newItem.id && oldItem.nameProduct == newItem.nameProduct
            override fun areContentsTheSame(oldItem: DataProduct, newItem: DataProduct) = oldItem == newItem
        }
    }

    @Before
    fun setup() {
        productRepositoryImpl = ProductRepositoryImpl(apiProduct, cartDao, fcmDao)
    }

    @Test
    fun `test get list product paging when state is success`() = runTest {
        val data = ProductPagingSource.snapShot(listOf(dummyProductData()))
        val expectedData = MutableLiveData<PagingData<DataProduct>>()
        expectedData.value= data

        CoroutineScope(Dispatchers.IO).launch {
            val actualData: PagingData<DataProduct> = productRepositoryImpl.getListProductPaging(null).asLiveData().getOrAwaitValue()
            val differ = AsyncPagingDataDiffer(
                diffCallback = PRODUCT_COMPARATOR,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(actualData)

            assertEquals(dummyProductData().size, differ.snapshot().size)
            assertEquals(dummyProductData(), differ.snapshot())
        }
    }

    @Test
    fun `test list product favorite when state is success`() = runTest {
        val dummyData = dummyProductResponse()

        // Arrange
        `when`(
            apiProduct.getListProductFavorite(
                query = ArgumentMatchers.anyString(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenReturn(dummyData)

        // Act
        val resultFlow = productRepositoryImpl.getListProductFavorite(
            query = ArgumentMatchers.anyString(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            val actualData = awaitItem() as Resource.Success
            assertEquals(dummyData.toDomain(), actualData.data)
            awaitComplete()
        }
    }

    @Test
    fun `test list product favorite when state is error 400`() = runTest {
        val response = Response.error<DataProductResponseDTO>(400, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.getListProductFavorite(
                query = ArgumentMatchers.anyString(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.getListProductFavorite(
            query = ArgumentMatchers.anyString(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test list product favorite when state is error 401`() = runTest {
        val response = Response.error<DataProductResponseDTO>(401, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.getListProductFavorite(
                query = ArgumentMatchers.anyString(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.getListProductFavorite(
            query = ArgumentMatchers.anyString(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test list product favorite when state is error Exception`() = runTest {
        // Arrange
        `when`(
            apiProduct.getListProductFavorite(
                query = ArgumentMatchers.anyString(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = productRepositoryImpl.getListProductFavorite(
            query = ArgumentMatchers.anyString(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test get product detail when state is success`() = runTest {
        val dummyData = dummyDetailProductResponse()

        // Arrange
        `when`(
            apiProduct.getProductDetail(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenReturn(dummyData)

        // Act
        val resultFlow = productRepositoryImpl.getProductDetail(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            val actualData = awaitItem() as Resource.Success
            assertEquals(dummyData.toDomain(), actualData.data)
            awaitComplete()
        }
    }

    @Test
    fun `test get product detail when state is error 400`() = runTest {
        val response = Response.error<DetailProductResponseDTO>(400, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.getProductDetail(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.getProductDetail(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test get product detail when state is error 401`() = runTest {
        val response = Response.error<DetailProductResponseDTO>(401, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.getProductDetail(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.getProductDetail(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test get product detail when state is error Exception`() = runTest {
        // Arrange
        `when`(
            apiProduct.getProductDetail(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = productRepositoryImpl.getProductDetail(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test add product favorite when state is success`() = runTest {
        val dummyData = dummySuccessResponse()

        // Arrange
        `when`(
            apiProduct.addProductFavorite(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenReturn(dummyData)

        // Act
        val resultFlow = productRepositoryImpl.addProductFavorite(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            val actualData = awaitItem() as Resource.Success
            assertEquals(dummyData.toDomain(), actualData.data)
            awaitComplete()
        }
    }

    @Test
    fun `test add product favorite when state is error 400`() = runTest {
        val response = Response.error<SuccessResponseDTO>(400, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.addProductFavorite(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.addProductFavorite(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test add product favorite when state is error 401`() = runTest {
        val response = Response.error<SuccessResponseDTO>(401, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.addProductFavorite(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.addProductFavorite(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test add product favorite when state is error Exception`() = runTest {
        // Arrange
        `when`(
            apiProduct.addProductFavorite(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = productRepositoryImpl.addProductFavorite(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test remove product favorite when state is success`() = runTest {
        val dummyData = dummySuccessResponse()

        // Arrange
        `when`(
            apiProduct.removeProductFavorite(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenReturn(dummyData)

        // Act
        val resultFlow = productRepositoryImpl.removeProductFavorite(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            val actualData = awaitItem() as Resource.Success
            assertEquals(dummyData.toDomain(), actualData.data)
            awaitComplete()
        }
    }

    @Test
    fun `test remove product favorite when state is error 400`() = runTest {
        val response = Response.error<SuccessResponseDTO>(400, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.removeProductFavorite(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.removeProductFavorite(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test remove product favorite when state is error 401`() = runTest {
        val response = Response.error<SuccessResponseDTO>(401, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.removeProductFavorite(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.removeProductFavorite(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test remove product favorite when state is error Exception`() = runTest {
        // Arrange
        `when`(
            apiProduct.removeProductFavorite(
                productId = ArgumentMatchers.anyInt(),
                userId = ArgumentMatchers.anyInt()
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = productRepositoryImpl.removeProductFavorite(
            productId = ArgumentMatchers.anyInt(),
            userId = ArgumentMatchers.anyInt()
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test update stock when state is success`() = runTest {
        val dummyData = dummySuccessResponse()

        val updateStockItemData = UpdateStockItem(
            id_product = "",
            stock = 0
        )
        val updateStockData = UpdateStockProduct(
            id_user = "",
            data_stock = listOf(updateStockItemData)
        )

        // Arrange
        `when`(apiProduct.updateStock(updateStock = updateStockData)).thenReturn(dummyData)

        // Act
        val resultFlow = productRepositoryImpl.updateStock(updateStock = updateStockData)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            val actualData = awaitItem() as Resource.Success
            assertEquals(dummyData.toDomain(), actualData.data)
            awaitComplete()
        }
    }

    @Test
    fun `test update stock when state is error 400`() = runTest {
        val response = Response.error<SuccessResponseDTO>(400, "".toResponseBody(null))

        val updateStockItemData = UpdateStockItem(
            id_product = "",
            stock = 0
        )
        val updateStockData = UpdateStockProduct(
            id_user = "",
            data_stock = listOf(updateStockItemData)
        )

        // Arrange
        `when`(apiProduct.updateStock(updateStock = updateStockData)).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.updateStock(updateStock = updateStockData)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test update stock when state is error 401`() = runTest {
        val response = Response.error<SuccessResponseDTO>(401, "".toResponseBody(null))

        val updateStockItemData = UpdateStockItem(
            id_product = "",
            stock = 0
        )
        val updateStockData = UpdateStockProduct(
            id_user = "",
            data_stock = listOf(updateStockItemData)
        )

        // Arrange
        `when`(apiProduct.updateStock(updateStock = updateStockData)).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.updateStock(updateStock = updateStockData)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test update stock when state is error Exception`() = runTest {
        val updateStockItemData = UpdateStockItem(
            id_product = "",
            stock = 0
        )
        val updateStockData = UpdateStockProduct(
            id_user = "",
            data_stock = listOf(updateStockItemData)
        )

        // Arrange
        `when`(apiProduct.updateStock(updateStock = updateStockData)).thenThrow(RuntimeException())

        // Act
        val resultFlow = productRepositoryImpl.updateStock(updateStock = updateStockData)

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test update rate when state is success`() = runTest {
        val dummyData = dummySuccessResponse()

        // Arrange
        `when`(
            apiProduct.updateRate(
                id = 0,
                rate = ""
            )
        ).thenReturn(dummyData)

        // Act
        val resultFlow = productRepositoryImpl.updateRate(
            id = 0,
            updateRate = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            val actualData = awaitItem() as Resource.Success
            assertEquals(dummyData.toDomain(), actualData.data)
            awaitComplete()
        }
    }

    @Test
    fun `test update rate when state is error 400`() = runTest {
        val response = Response.error<SuccessResponseDTO>(400, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.updateRate(
                id = 0,
                rate = ""
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.updateRate(
            id = 0,
            updateRate = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test update rate when state is error 401`() = runTest {
        val response = Response.error<SuccessResponseDTO>(401, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.updateRate(
                id = 0,
                rate = ""
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.updateRate(
            id = 0,
            updateRate = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test update rate when state is error Exception`() = runTest {
        // Arrange
        `when`(
            apiProduct.updateRate(
                id = 0,
                rate = ""
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = productRepositoryImpl.updateRate(
            id = 0,
            updateRate = ""
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test get product other when state is success`() = runTest {
        val dummyData = dummyProductResponse()

        // Arrange
        `when`(
            apiProduct.getProductOther(
                userId = 0
            )
        ).thenReturn(dummyData)

        // Act
        val resultFlow = productRepositoryImpl.getProductOther(
            userId = 0
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            val actualData = awaitItem() as Resource.Success
            assertEquals(dummyData.toDomain(), actualData.data)
            awaitComplete()
        }
    }

    @Test
    fun `test get product other when state is error 400`() = runTest {
        val response = Response.error<DataProductResponseDTO>(400, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.getProductOther(
                userId = 0
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.getProductOther(
            userId = 0
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test get product other when state is error 401`() = runTest {
        val response = Response.error<DataProductResponseDTO>(401, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.getProductOther(
                userId = 0
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.getProductOther(
            userId = 0
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test get product other when state is error Exception`() = runTest {
        // Arrange
        `when`(
            apiProduct.getProductOther(
                userId = 0
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = productRepositoryImpl.getProductOther(
            userId = 0
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test get product history when state is success`() = runTest {
        val dummyData = dummyProductResponse()

        // Arrange
        `when`(
            apiProduct.getProductHistory(
                userId = 0
            )
        ).thenReturn(dummyData)

        // Act
        val resultFlow = productRepositoryImpl.getProductHistory(
            userId = 0
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            val actualData = awaitItem() as Resource.Success
            assertEquals(dummyData.toDomain(), actualData.data)
            awaitComplete()
        }
    }

    @Test
    fun `test get product history when state is error 400`() = runTest {
        val response = Response.error<DataProductResponseDTO>(400, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.getProductHistory(
                userId = 0
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.getProductHistory(
            userId = 0
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test get product history when state is error 401`() = runTest {
        val response = Response.error<DataProductResponseDTO>(401, "".toResponseBody(null))

        // Arrange
        `when`(
            apiProduct.getProductHistory(
                userId = 0
            )
        ).thenThrow(HttpException(response))

        // Act
        val resultFlow = productRepositoryImpl.getProductHistory(
            userId = 0
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

    @Test
    fun `test get product history when state is error Exception`() = runTest {
        // Arrange
        `when`(
            apiProduct.getProductHistory(
                userId = 0
            )
        ).thenThrow(RuntimeException())

        // Act
        val resultFlow = productRepositoryImpl.getProductHistory(
            userId = 0
        )

        // Assert
        resultFlow.test {
            assertTrue(awaitItem() is Resource.Loading)
            assertTrue(awaitItem() is Resource.Error)
            awaitComplete()
        }
    }

}

class ProductPagingSource: PagingSource<Int, LiveData<List<DataProduct>>>() {
    companion object {
        fun snapShot(items: List<DataProduct>): PagingData<DataProduct> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<DataProduct>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<DataProduct>>> {
        return LoadResult.Page(emptyList(),0 , 1)
    }
}