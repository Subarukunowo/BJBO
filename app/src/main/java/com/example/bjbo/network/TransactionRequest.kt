package com.example.bjbo.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class TransactionRequest(
    val order_id: String,
    val gross_amount: Long,
    val name: String,
)

data class TransactionResponse(
    val status: String,
    val transaction_token: String?,
    val message: String?,
)

