package com.myshopproject.domain.usecase

import androidx.paging.PagingData
import com.myshopproject.domain.entities.*
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RemoteUseCase {
    fun loginAccount(email: String, password: String): Flow<Resource<LoginResult>>
    fun registerAccount(image: MultipartBody.Part, email: RequestBody, password: RequestBody, name: RequestBody, phone: RequestBody, gender: Int): Flow<Resource<SuccessResponseStatus>>
    fun changePassword(id: Int, password: String, newPassword: String, confirmPassword: String): Flow<Resource<SuccessResponseStatus>>
    fun changeImage(id: Int, image: MultipartBody.Part): Flow<Resource<ChangeImageResponse>>
    fun getListProductPaging(query: String?) : Flow<PagingData<DataProduct>>
    fun getListProductFavorite(query: String?, userId: Int): Flow<Resource<DataProductResponse>>
    fun getProductDetail(productId: Int, userId: Int): Flow<Resource<DetailProductResponse>>
    fun addProductFavorite(productId: Int, userId: Int): Flow<Resource<SuccessResponseStatus>>
    fun removeProductFavorite(productId: Int, userId: Int): Flow<Resource<SuccessResponseStatus>>
    fun updateStock(updateStock: UpdateStockProduct): Flow<Resource<SuccessResponseStatus>>
    fun updateRate(id: Int, updateRate: UpdateRate): Flow<Resource<SuccessResponseStatus>>
}