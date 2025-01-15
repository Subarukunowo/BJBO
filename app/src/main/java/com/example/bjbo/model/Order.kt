package com.example.bjbo.model

import Postingan

data class Order(
    val id: Int,
    val user_id: User,
    val postingan_id: Postingan,
    val total_harga: Double,
    val harga_tawaran: Double? = null,
    val status: String,
    val snaptoken: String? = null,
    val redirectUrl: String? = null
)
