package com.myshopproject.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.myshopproject.data.mapper.toDomain
import com.myshopproject.data.source.remote.network.ApiProduct
import com.myshopproject.domain.entities.DataProduct
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RemotePagingSource @Inject constructor(
    private val query: String?,
    private val apiProduct: ApiProduct
) : PagingSource<Int, DataProduct>() {
    override fun getRefreshKey(state: PagingState<Int, DataProduct>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataProduct> {
        return try {
            val productOffset = params.key ?: 0

            val response = apiProduct.getListProductPaging(query, productOffset)
            val productResult = response.success.data.map { it.toDomain() }

            val nextKey = if (productResult.isEmpty()) null else productOffset + 5

            LoadResult.Page(
                data = productResult,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}