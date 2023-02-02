package com.myshopproject.data.source.remote

//class RemotePagingSource @Inject constructor(private val apiProduct: ApiProduct) :
//    PagingSource<Int, DataProduct>() {
//    override fun getRefreshKey(state: PagingState<Int, DataProduct>): Int? {
//        return state.anchorPosition
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataProduct> {
//        return try {
//            val productOffset = params.key ?: 0
//
//            val response = apiProduct.getListProduct("", productOffset)
//            val productResult = response.success.data.map { it.toDomain() }
//
//            val prevKey = if (productOffset == 0) null else productOffset - 4
//            val nextKey = if (productResult.isEmpty()) null else productOffset + 4
//
//            LoadResult.Page(
//                data = productResult,
//                prevKey = prevKey,
//                nextKey = nextKey
//            )
//
//        } catch (exception: IOException) {
//            LoadResult.Error(exception)
//        } catch (exception: HttpException) {
//            LoadResult.Error(exception)
//        }
//    }
//}