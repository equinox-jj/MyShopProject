package com.myshopproject.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.myshopproject.data.source.remote.dto.DataProductResponseDTO
import com.myshopproject.data.source.remote.network.ApiProduct
import javax.inject.Inject

class RemotePagingSource @Inject constructor(private val apiProduct: ApiProduct) :
    PagingSource<Int, DataProductResponseDTO>() {
    override fun getRefreshKey(state: PagingState<Int, DataProductResponseDTO>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataProductResponseDTO> {
        TODO("Not yet implemented")
    }

}