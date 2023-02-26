package com.myshopproject.domain.entities

data class DataProductResult(
    val data: List<DataProduct>,
    val message: String? = null,
    val status: Int? = null,
    val totalRow: Int? = null
)
