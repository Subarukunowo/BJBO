package com.example.bjbo.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://192.168.237.127:8000/api/"
    private const val IMAGE_URL = "http://192.168.237.127:8000/images/"
    const val PEMBAYARAN_URL = "http://192.168.237.127:8000/api/orders"

    // Konfigurasi HttpLoggingInterceptor untuk debugging HTTP request/response
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Konfigurasi OkHttpClient untuk menambahkan logging interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Konfigurasi Gson dengan setLenient untuk menangani JSON tidak standar
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    // Konfigurasi Retrofit dengan OkHttpClient dan Gson
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Tambahkan OkHttpClient dengan logging
            .addConverterFactory(GsonConverterFactory.create(gson)) // Gunakan Gson custom
            .build()
    }

    //val apiService: ApiService = retrofit.create(ApiService::class.java)

    // Instance ApiService untuk memanggil endpoint API
    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Fungsi untuk mendapatkan URL gambar lengkap
    fun getFullimageUrl(imagePath: String?): String {
        return if (!imagePath.isNullOrEmpty()) {
            "$IMAGE_URL$imagePath"
        } else {
            "$IMAGE_URL/default_image.jpg" // Gambar default jika imagePath kosong
        }
    }
}
