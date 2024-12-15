package com.example.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    // Menggunakan LoginViewModel yang sudah dibuat
    private val viewModel: LoginViewModel by viewModels()

    // Membuat variabel binding untuk ViewBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menghubungkan layout dengan ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengikat ViewModel ke layout
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Mengamati status login
        viewModel.loginStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                // Login berhasil
                // Arahkan ke aktivitas berikutnya atau tampilkan UI sukses
            } else {
                // Login gagal
                // Tampilkan pesan kesalahan atau UI gagal login
            }
        }
    }
}
