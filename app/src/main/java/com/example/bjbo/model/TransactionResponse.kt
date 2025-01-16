package com.example.bjbo.model

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("status") val status: String,
    @SerializedName("transaction_token") val transactionToken: String
)
