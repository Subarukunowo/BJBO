package com.example.bjbo.network


import Postingan
import com.example.bjbo.model.ApiResponse
import com.example.bjbo.model.Favorit
import com.example.bjbo.model.FavoritResponse
import com.example.bjbo.model.Laporan
import com.example.bjbo.model.LaporanRequest
import com.example.bjbo.model.LoginRequest
import com.example.bjbo.model.LogoutResponse

import com.example.bjbo.model.NominatimResponse
import com.example.bjbo.model.Order
import com.example.bjbo.model.OrderRequest
import com.example.bjbo.model.ProfileRequest
import com.example.bjbo.model.RegisterRequest
import com.example.bjbo.model.Ulasan
import com.example.bjbo.model.UlasanRequest
import com.example.bjbo.model.User
import com.example.bjbo.model.UserRespons
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

//favorit
@GET("favorits")
fun getAllFavorit(): Call<List<Favorit>>

    @GET("favorits/{id}")
    fun getFavoritById(@Path("id") id: Long): Call<Favorit>

    @POST("favorits/")
    fun createFavorit(@Body favorit: Favorit): Call<Favorit>

    @PUT("favorits/{id}")
    fun updateFavorit(@Path("id") id: Long, @Body favorit: Favorit): Call<Favorit>

    @DELETE("favorits/{id}")
    fun deleteFavorit(@Path("id") id: Long): Call<Void>

    @GET("favorits/user/{userId}")
    fun getFavoritsByUserId(
        @Path("userId") userId: Int
    ): Call<ApiResponse<List<Favorit>>>

    @GET("favorits/postingan/{postinganId}")
    fun getFavoritsByPostinganId(
        @Path("postinganId") postinganId: Int
    ): Call<FavoritResponse>


    //order
    @GET("orders/{id}")
    fun getOrderById(
        @Path("id") id: Int
    ): Call<Order>

    @POST("orders")
    fun createOrder(@Body orderRequest: OrderRequest): Call<Order>



    //postingan
    @GET("postingan/search")
    fun searchPostingan(@Query("keyword") keyword: String): Call<List<Postingan>>

    @GET("postingan")
    fun getAllPostingan(): Call<ApiResponse<List<Postingan>>>

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
    @GET("postingan/{id}")
    fun getPostinganById(
        @Path("id") id: Int // Parameter ID untuk mencari postingan
    ): Call<ApiResponse<Postingan>>

    @DELETE("postingan/{id}")
    fun deletePostingan(@Path("id") id: Int): Call<Unit>


    //Ulasan

    @GET("ulasans")
    fun getAllUlasans(): Call<ApiResponse<List<Ulasan>>>

    @GET("ulasans/{id}")
    fun getUlasanById(
        @Path("id") ulasanId: Int
    ): Call<ApiResponse<Ulasan>> // Respons akan berisi satu ulasan, bukan daftar

    @POST("ulasans")
    fun addUlasan(@Body ulasanRequest: UlasanRequest): Call<ApiResponse<Any>>

    @DELETE("ulasans/{id}")
    fun deleteUlasan(
        @Path("id") id: Int
    ): Call<ApiResponse<Any>>


    //user
    @POST("pengguna/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<UserRespons>

    @GET("pengguna/{id}")
    fun getUserById(@Path("id") userId: Int): Call<UserRespons>

    @POST("pengguna")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<User>

    @PUT("pengguna/{id}")
    fun updateUserProfile(
        @Path("id") userId: Int,
        @Body profileRequest: ProfileRequest
    ): Call<UserRespons>




    //laporan
    @GET("laporans") // Mendapatkan semua laporan
    fun getAllLaporans(): Call<List<Laporan>>

    @POST("laporans")
    fun addLaporan(@Body laporan: LaporanRequest): Call<Void>

    @GET("laporans/{id}") // Mendapatkan detail laporan
    fun getLaporan(@Path("id") id: Int): Call<Laporan>

    @GET("ulasans/postingan/{postinganId}")
    fun getUlasansByPostinganId(
        @Path("postinganId") postinganId: Int
    ): Call<ApiResponse<List<Ulasan>>>
    //lokasi
        @GET("search")
        fun searchLocations(
            @Query("q") query: String,          // Nama lokasi yang dicari
            @Query("format") format: String = "json", // Format hasil JSON
            @Query("limit") limit: Int = 5      // Batas jumlah hasil
        ): Call<List<NominatimResponse>>

//logout
    @POST("logout")
    fun logoutUser(
        @Header("Authorization") token: String
    ): Call<LogoutResponse>
}


