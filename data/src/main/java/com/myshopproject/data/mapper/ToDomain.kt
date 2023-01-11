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
    phone = phone
//    id = id,
//    path = path,
//    image = image,
)

fun RegisterResponseDTO.toDomain() = RegisterResponse(
    success = success.toDomain()
)

fun RegisterResultDTO.toDomain() = RegisterResult(
    message = message,
    status = status
)