package com.example.bjbo.network

import com.example.bjbo.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://6767c982560fbd14f18e63c6.mockapi.io/api/bjbo/"

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
