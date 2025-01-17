package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.database.SharedPreferencesHelper
import com.example.bjbo.model.LogoutResponse
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performLogout()
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    private fun performLogout() {
        val token = SharedPreferencesHelper.getAuthToken(this)

        if (!token.isNullOrEmpty()) {
            ApiClient.instance.logoutUser("Bearer $token")
                .enqueue(object : Callback<LogoutResponse> {
                    override fun onResponse(
                        call: Call<LogoutResponse>,
                        response: Response<LogoutResponse>
                    ) {
                        if (response.isSuccessful) {
                            SharedPreferencesHelper.clearUserPreferences(this@LogoutActivity)
                            Toast.makeText(
                                this@LogoutActivity,
                                "Logout berhasil!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToLogin()
                        } else {
                            Toast.makeText(
                                this@LogoutActivity,
                                "Logout gagal: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                        Toast.makeText(
                            this@LogoutActivity,
                            "Kesalahan jaringan: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            SharedPreferencesHelper.clearUserPreferences(this)
            Toast.makeText(this, "Logout berhasil tanpa API!", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }



    }
}
