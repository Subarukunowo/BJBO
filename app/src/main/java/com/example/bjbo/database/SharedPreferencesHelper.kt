package com.example.bjbo.database

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPreferencesHelper {
    private const val PREFS_NAME = "bjbo_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val AUTH_TOKEN = "token"
    private const val KEY_POSTINGAN_ID = "postingan_id"
    private const val KEY_FAVORITE_IDS = "favorite_ids"

    fun saveUserPreferences(context: Context, userId: Int, userName: String, token: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(AUTH_TOKEN, token) // Simpan token di SharedPreferences
            apply()
        }
    }

    fun isUserLoggedIn(context: Context): Boolean {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.contains(KEY_USER_ID)
    }

    fun getAuthToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString("auth_token", null)
    }

    fun getUserId(context: Context): Int {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun getUserName(context: Context): String {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_NAME, "Pengguna") ?: "Pengguna"
    }


    fun clearUserPreferences(context: Context) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun savePostinganId(context: Context, postinganId: Int) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt(KEY_POSTINGAN_ID, postinganId) // Simpan postingan_id
            apply()
        }
        Log.d("SharedPreferencesHelper", "postingan_id yang disimpan: $postinganId")
    }

    fun getPostinganId(context: Context): Int {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var postinganId = prefs.getInt(KEY_POSTINGAN_ID, -1) // Default -1 jika tidak ada

        if (postinganId == -1) {
            // Jika postingan_id belum disimpan, gunakan nilai default atau tangani sesuai kebutuhan
            val defaultPostinganId = 0 // Ganti dengan nilai default yang Anda inginkan
            savePostinganId(
                context,
                defaultPostinganId
            ) // Simpan nilai default ke SharedPreferences
            postinganId = defaultPostinganId
            Log.d(
                "SharedPreferencesHelper",
                "postingan_id tidak ditemukan, menyimpan default: $postinganId"
            )
        } else {
            Log.d("SharedPreferencesHelper", "postingan_id yang diambil: $postinganId")
        }

        return postinganId
    }
    // Simpan set ID favorit
    fun saveFavoriteIds(context: Context, favoriteIds: Set<String>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putStringSet(KEY_FAVORITE_IDS, favoriteIds)
            apply()
        }
    }

    // Ambil set ID favorit
    fun getFavoriteIds(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_FAVORITE_IDS, emptySet()) ?: emptySet()
    }

    // Tambahkan ID favorit ke set
    fun addFavoriteId(context: Context, favoriteId: Int) {
        val favoriteIds = getFavoriteIds(context).toMutableSet()
        favoriteIds.add(favoriteId.toString())
        saveFavoriteIds(context, favoriteIds)
    }

    // Hapus ID favorit dari set
    fun removeFavoriteId(context: Context, favoriteId: Int) {
        val favoriteIds = getFavoriteIds(context).toMutableSet()
        favoriteIds.remove(favoriteId.toString())
        saveFavoriteIds(context, favoriteIds)
    }

    // Periksa apakah ID favorit ada di set
    fun isFavoriteId(context: Context, favoriteId: Int): Boolean {
        val favoriteIds = getFavoriteIds(context)
        return favoriteIds.contains(favoriteId.toString())
    }
}