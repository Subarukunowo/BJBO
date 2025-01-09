package com.example.bjbo.network



import com.example.bjbo.model.NominatimResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NominatimApi {


        @GET("search")
        @Headers("User-Agent: BJBOApp/1.0 (risethunder7@gmail.com)")
        fun searchLocations(
            @Query("q") query: String,
            @Query("format") format: String = "json",
            @Query("limit") limit: Int = 5
        ): Call<List<NominatimResponse>>
    }


