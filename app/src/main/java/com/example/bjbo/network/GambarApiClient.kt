package com.example.bjbo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GambarApiClient {

    private const val BASE_URL = "http://192.168.237.127:8000/images/"

    val postinganretrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: ApiService by lazy {
        postinganretrofit.create(ApiService::class.java)
    }

}
