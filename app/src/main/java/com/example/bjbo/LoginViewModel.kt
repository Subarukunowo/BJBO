package com.example.app

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    // LiveData untuk email dan password
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // LiveData untuk status login
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    companion object {
        private const val PREF_NAME = "LoginPreferences"
        private const val IS_LOGGED_IN = "isLoggedIn"
    }

    // Validasi kredensial
    fun validateCredentials(context: Context) {
        val inputEmail = email.value ?: ""
        val inputPassword = password.value ?: ""

        if (inputEmail == "admin@example.com" && inputPassword == "password123") {
            _loginStatus.value = true
            saveLoginState(context, true)
        } else {
            _loginStatus.value = false
        }
    }

    // Menyimpan status login di SharedPreferences
    private fun saveLoginState(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    // Mengecek apakah user sudah login
    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    // Logout
    fun logout(context: Context) {
        saveLoginState(context, false) // Set status login menjadi false
    }
}
