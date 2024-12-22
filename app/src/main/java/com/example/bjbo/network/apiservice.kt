package com.example.bjbo.network

import com.example.bjbo.model.Barang
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("barang") // Endpoint untuk produk baru
    fun getBarang(): Call<List<Barang>>

    @GET("baru-dilihat") // Endpoint untuk baru saja dilihat
    fun getBaruDilihat(): Call<List<Barang>>
}
