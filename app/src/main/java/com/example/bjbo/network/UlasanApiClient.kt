package com.example.bjbo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UlasanApiClient {

    private const val BASE_URL = "http://127.0.0.1:8000/api/ulasans/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}
