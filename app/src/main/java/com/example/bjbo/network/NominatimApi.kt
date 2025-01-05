package com.example.bjbo.network



import com.example.bjbo.model.NominatimResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApi {

    @GET("search")
    fun searchLocations(
        @Query("q") query: String,         // Query teks lokasi
        @Query("format") format: String = "json", // Format hasil
        @Query("limit") limit: Int = 5     // Batas jumlah hasil
    ): Call<List<NominatimResponse>>
}
