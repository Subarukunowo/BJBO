package com.example.bjbo.network

import com.example.bjbo.model.Barang
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("barang") // Endpoint untuk produk baru
    fun getBarang(): Call<List<Barang>>

    @GET("baru-dilihat") // Endpoint untuk baru saja dilihat
    fun getBaruDilihat(): Call<List<Barang>>
    @GET("barang/{id}")
    fun getBarangDetail(@Path("id") id: String): Call<Barang>

}
