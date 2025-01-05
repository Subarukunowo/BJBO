package com.example.bjbo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClientNominatim {
    private const val BASE_URL = "https://nominatim.openstreetmap.org/"

    val instance: NominatimApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NominatimApi::class.java)
    }
}
