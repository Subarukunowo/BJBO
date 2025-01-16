package com.example.bjbo.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {

    private const val PREF_NAME = "USER_PREFS"
    private const val KEY_USER_ID = "USER_ID"
    private const val KEY_USER_NAME = "USER_NAME"

    fun saveUserPreferences(context: Context, userId: Int, userName: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_USER_NAME, userName)
        editor.apply()
    }

    fun getUserId(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_USER_ID, -1)
    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USER_NAME, "Pengguna") ?: "Pengguna"
    }

    fun clearUserPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}
