package com.example.bjbo

import android.content.Context
import android.widget.Toast
import com.example.bjbo.model.Favorit
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteActivity(private val context: Context) {

    private val favoriteList = mutableListOf<Favorit>() // List untuk menyimpan data favorit

    private fun addToFavorites(favorit: Favorit) {
        val apiService = ApiClient.instance

        // Panggil API untuk menambahkan ke favorit
        apiService.createFavorit(favorit).enqueue(object : Callback<Favorit> {
            override fun onResponse(call: Call<Favorit>, response: Response<Favorit>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        favoriteList.add(it) // Tambahkan ke list favorit
                        Toast.makeText(context, "Berhasil ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Gagal menambahkan ke favorit", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Favorit>, t: Throwable) {
                Toast.makeText(context, "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeFromFavorites(favorit: Favorit) {
        val apiService = ApiClient.instance

        // Panggil API untuk menghapus dari favorit
        apiService.deleteFavorit(favorit.id.toLong()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    favoriteList.remove(favorit) // Hapus dari list favorit
                    Toast.makeText(context, "Berhasil dihapus dari favorit", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Gagal menghapus dari favorit", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk memuat semua data favorit dari API
    fun loadFavoritesFromApi() {
        val apiService = ApiClient.instance

        // Panggil API untuk mendapatkan semua data favorit
        apiService.getAllFavorit().enqueue(object : Callback<List<Favorit>> {
            override fun onResponse(call: Call<List<Favorit>>, response: Response<List<Favorit>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        favoriteList.clear() // Bersihkan list sebelum menambahkan data baru
                        favoriteList.addAll(it) // Tambahkan semua data favorit dari API
                        Toast.makeText(context, "Data favorit berhasil dimuat", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Gagal memuat data favorit", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Favorit>>, t: Throwable) {
                Toast.makeText(context, "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk mendapatkan semua item favorit
    fun getAllFavorites(): List<Favorit> {
        return favoriteList // Mengembalikan semua item favorit
    }

    // Fungsi untuk memeriksa apakah item ada di favorit
    fun isFavorite(favorit: Favorit): Boolean {
        return favoriteList.any { it.id == favorit.id } // Mengembalikan true jika item ada di favorit
    }
}
