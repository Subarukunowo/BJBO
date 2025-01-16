package com.example.bjbo.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private const val BASE_URL = "http://192.168.237.127:8000/api/"
    private const val IMAGE_URL = "http://192.168.237.127:8000/images/"
    const val PEMBAYARAN_URL = "http://192.168.237.127:8000/api/orders"

    val postinganretrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: ApiService by lazy {
        postinganretrofit.create(ApiService::class.java)
    }
    fun getFullimageUrl(imagePath: String?): String {
        return imagePath.let { "$IMAGE_URL$it" }
    }

}
