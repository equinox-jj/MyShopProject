package com.myshopproject.domain.usecase

import com.myshopproject.domain.entities.*
import com.myshopproject.domain.repository.AuthRepository
import com.myshopproject.domain.repository.ProductRepository
import com.myshopproject.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RemoteInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository
): RemoteUseCase {
//    override fun getListProductPaging(): Flow<PagingData<DataProduct>> = productRepository.getListProductPaging()
    override fun loginAccount(email: String, password: String): Flow<Resource<LoginResult>> = authRepository.loginAccount(email, password)
    override fun registerAccount(image: MultipartBody.Part, email: RequestBody, password: RequestBody, name: RequestBody, phone: RequestBody, gender: Int): Flow<Resource<SuccessResponseStatus>> = authRepository.registerAccount(image, email, password, name, phone, gender)
    override fun changePassword(id: Int, password: String, newPassword: String, confirmPassword: String): Flow<Resource<SuccessResponseStatus>> = authRepository.changePassword(id, password, newPassword, confirmPassword)
    override fun changeImage(id: Int, image: MultipartBody.Part): Flow<Resource<ChangeImageResponse>> = authRepository.changeImage(id, image)
    override fun getListProduct(query: String?): Flow<Resource<DataProductResponse>> = productRepository.getListProduct(query)
    override fun getListProductFavorite(query: String?, userId: Int): Flow<Resource<DataProductResponse>> = productRepository.getListProductFavorite(query, userId)
    override fun getProductDetail(productId: Int, userId: Int): Flow<Resource<DetailProductResponse>> = productRepository.getProductDetail(productId, userId)
    override fun addProductFavorite(productId: Int, userId: Int): Flow<Resource<SuccessResponseStatus>> = productRepository.addProductFavorite(productId, userId)
    override fun removeProductFavorite(productId: Int, userId: Int): Flow<Resource<SuccessResponseStatus>> = productRepository.removeProductFavorite(productId, userId)
    override fun updateStock(updateStock: UpdateStockProduct): Flow<Resource<SuccessResponseStatus>> = productRepository.updateStock(updateStock)
    override fun updateRate(id: Int, updateRate: UpdateRate): Flow<Resource<SuccessResponseStatus>> = productRepository.updateRate(id, updateRate)
}