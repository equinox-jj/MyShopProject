package com.myshopproject.data.mapper

import com.myshopproject.data.remote.dto.*
import com.myshopproject.domain.entities.*

fun LoginResponseDTO.toDomain() = LoginResponse(
    success = success.toDomain()
)

fun LoginResultDTO.toDomain() = LoginResult(
    accessToken = accessToken,
    dataUser = dataUser.toDomain(),
    message = message,
    refreshToken = refreshToken,
    status = status
)

fun DataUserDTO.toDomain() = DataUserResponse(
    email = email,
    gender = gender,
    name = name,
    phone = phone,
    id = id,
    image = image,
    path = path
)

fun SuccessResponseDTO.toDomain() = SuccessResponseStatus(
    success = success.toDomain()
)

fun SuccessResultDTO.toDomain() = SuccessResultStatus(
    message = message,
    status = status
)

fun ErrorResponseDTO.toDomain() = ErrorResponseStatus(
    error = error.toDomain()
)

fun ErrorResultDTO.toDomain() = ErrorResultStatus(
    message = message,
    status = status
)

fun RefreshTokenResponseDTO.toDomain() = RefreshTokenResponse(
    success = success.toDomain()
)

fun RefreshTokenResultDTO.toDomain() = RefreshTokenResult(
    accessToken = accessToken,
    message = message,
    refreshToken = refreshToken,
    status = status
)

fun ChangeImageResponseDTO.toDomain() = ChangeImageResponse(
    success = success.toDomain()
)

fun ChangeImageResultDTO.toDomain() = ChangeImageResult(
    path = path,
    message = message,
    status = status
)