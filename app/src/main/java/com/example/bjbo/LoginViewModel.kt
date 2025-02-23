package com.example.bjbo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bjbo.database.DBHelper

class LoginViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    fun validateCredentials(context: Context) {
        val inputEmail = email.value ?: ""
        val inputPassword = password.value ?: ""

        val dbHelper = DBHelper(context)
        if (dbHelper.checkLogin(inputEmail, inputPassword)) {
            _loginStatus.value = true
        } else {
            _loginStatus.value = false
        }
    }
}
