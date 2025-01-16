package com.example.bjbo.network

import ApiResponse
import Postingan
import com.example.bjbo.model.Laporan
import com.example.bjbo.model.LaporanRequest
import com.example.bjbo.model.LoginRequest

import com.example.bjbo.model.NominatimResponse
import com.example.bjbo.model.Order
import com.example.bjbo.model.OrderRequest
import com.example.bjbo.model.Payment
import com.example.bjbo.model.ProfileRequest
import com.example.bjbo.model.RegisterRequest
import com.example.bjbo.model.Transaction
import com.example.bjbo.model.Ulasan
import com.example.bjbo.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // GET: Mendapatkan detail pesanan berdasarkan ID
    @GET("orders/{id}")
    fun getOrderById(
        @Path("id") id: Int
    ): Call<Order>

    @POST("orders")
    fun createOrder(@Body orderRequest: OrderRequest): Call<Order>


    @GET("postingan/search")
    fun searchPostingan(@Query("keyword") keyword: String): Call<List<Postingan>>


        @GET("postingan")
        fun getAllPostingan(): Call<ApiResponse<Postingan>>

    @Headers("Content-Type: application/json")
    @Multipart
    @POST("postingan")
    fun addPostingan(
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("lokasi") lokasi: RequestBody,
        @Part("status") status: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("username") username: RequestBody
    ): Call<ApiResponse<Postingan>>



    // PUT: Update postingan
        @PUT("postingan/{id}")

        fun updatePostingan(

            @Path("id") id: Int,
            @Body postingan: Postingan
        ): Call<Postingan>

        // DELETE: Hapus postingan

        @DELETE("postingan/{id}")

        fun deletePostingan(@Path("id") id: Int): Call<Unit>

        // GET: Semua ulasan

        @GET("ulasans")


        fun getAllUlasans(): Call<List<Ulasan>>

        // POST: Tambah ulasan baru

        @POST("ulasans")

        fun addUlasan(@Body ulasan: Ulasan): Call<Ulasan>

        // PUT: Update ulasan
        @PUT("ulasans/{id}")

        fun <Ulasan : Any?> updateUlasan(
            @Path("id") id: Int,
            @Body ulasan: Ulasan
        ): Call<Ulasan>

        // DELETE: Hapus ulasan

        @DELETE("ulasans/{id}")
        fun deleteUlasan(@Path("id") id: Int): Call<Unit>

    @POST("pengguna/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<User>

    @POST("pengguna/logout")
    fun logoutUser(): Call<Void>

    @GET("pengguna/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>


    @POST("pengguna")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<User>

    @PUT("pengguna/{id}")
    fun updateUserProfile(@Path("id") id: Int, @Body profileRequest: ProfileRequest): Call<User>




    // DELETE: Hapus pengguna

        @DELETE("pengguna/{id}")
        fun deleteUser(@Path("id") id: Int): Call<Unit>

        @GET("laporans") // Mendapatkan semua laporan
        fun getAllLaporans(): Call<List<Laporan>>

    @POST("laporans")
    fun addLaporan(@Body laporan: LaporanRequest): Call<Void>

        @GET("laporans/{id}") // Mendapatkan detail laporan
        fun getLaporan(@Path("id") id: Int): Call<Laporan>


        // API Pencarian Lokasi (OpenStreetMap Nominatim)

        @GET("search")
        fun searchLocations(
            @Query("q") query: String,          // Nama lokasi yang dicari
            @Query("format") format: String = "json", // Format hasil JSON
            @Query("limit") limit: Int = 5      // Batas jumlah hasil
        ): Call<List<NominatimResponse>>
    }

