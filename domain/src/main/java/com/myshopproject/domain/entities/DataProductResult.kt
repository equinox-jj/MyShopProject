package com.myshopproject.domain.entities

data class DataProductResult(
    val data: List<DataProduct>,
    val message: String,
    val status: Int
)
