package com.example.bjbo.network

import com.example.bjbo.model.Barang
import com.example.bjbo.model.FlashSale
import com.example.bjbo.model.NominatimResponse
import com.example.bjbo.model.Pembayaran
import com.example.bjbo.model.PembayaranResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    // API Barang
    @GET("barang") // Endpoint untuk produk baru
    fun getBarang(): Call<List<Barang>>

    @GET("baru-dilihat") // Endpoint untuk baru saja dilihat
    fun getBaruDilihat(): Call<List<Barang>>

    @GET("barang/{id}") // Endpoint untuk detail barang
    fun getBarangDetail(@Path("id") id: String): Call<Barang>

    @POST("barang") // Endpoint untuk menambahkan barang baru
    fun postBarang(@Body barang: Barang): Call<Barang>
    @POST("pembayaran") // Sesuaikan endpoint dengan MockAPI Anda
    fun postPembayaran(@Body pembayaran: Pembayaran): Call<PembayaranResponse>
    // API Pencarian Lokasi (OpenStreetMap Nominatim)
    @GET("search")
    fun searchLocations(
        @Query("q") query: String,          // Nama lokasi yang dicari
        @Query("format") format: String = "json", // Format hasil JSON
        @Query("limit") limit: Int = 5      // Batas jumlah hasil
    ): Call<List<NominatimResponse>>

    @GET("flashsale")
    suspend fun getFlashSales(): List<FlashSale>
}


