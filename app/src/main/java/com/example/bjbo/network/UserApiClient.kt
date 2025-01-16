package com.example.bjbo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserApiClient {

    private val BASE_URL = "http://10.0.2.2:8000/api/pengguna/"

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
