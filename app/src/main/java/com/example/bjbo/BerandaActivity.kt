package com.example.bjbo

import Postingan
import PostinganListFragment
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.example.bjbo.databinding.ActivityBerandaBinding
import com.example.bjbo.network.ApiClient
import com.example.bjbo.database.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerandaBinding
    private val postinganList = mutableListOf<Postingan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil userId dan userName dari SharedPreferences
        val userId = SharedPreferencesHelper.getUserId(this)
        val userName = SharedPreferencesHelper.getUserName(this)

        // Logging untuk debugging
        Log.d("BerandaActivity", "User ID: $userId")
        Log.d("BerandaActivity", "User Name: $userName")

        if (userId == -1) {
            Log.e("BerandaActivity", "User ID tidak valid. Mengarahkan ke LoginActivity.")
            Toast.makeText(this, "Silakan login kembali.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            binding.tvWelcome.text = "Hey, $userName!"
        }

        // Listener lainnya

        // Listener untuk ikon kamera
        binding.ivCamera.setOnClickListener {
            checkCameraPermission()
        }

        // Listener untuk tombol JUAL
        binding.btnJual.setOnClickListener {
            Toast.makeText(this, "Tombol JUAL diklik!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, JualActivity::class.java)
            startActivity(intent)
        }

        // Listener untuk pencarian
        binding.searchBar.setOnEditorActionListener { textView, actionId, keyEvent ->
            val keyword = textView.text.toString().trim()
            if (keyword.isNotEmpty()) {
                searchPostingan(keyword)
            } else {
                Toast.makeText(this, "Masukkan kata kunci untuk pencarian", Toast.LENGTH_SHORT).show()
            }
            true
        }

        // Tambahkan fragment PostinganListFragment
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.postinganFragmentContainer,
                    PostinganListFragment(),
                    PostinganListFragment::class.java.simpleName
                )
            }
        }

        // Listener untuk bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_explore -> {
                    val intent = Intent(this, ExploreActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_camera -> {
                    checkCameraPermission()
                    true
                }
                R.id.nav_favorite -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun searchPostingan(keyword: String) {
        ApiClient.instance.searchPostingan(keyword).enqueue(object : Callback<List<Postingan>> {
            override fun onResponse(
                call: Call<List<Postingan>>,
                response: Response<List<Postingan>>
            ) {
                if (response.isSuccessful) {
                    val results = response.body()
                    Log.d("BerandaActivity", "Hasil pencarian: ${results?.size} postingan ditemukan.")
                    if (!results.isNullOrEmpty()) {
                        postinganList.clear()
                        postinganList.addAll(results)
                        supportFragmentManager.commit {
                            replace(
                                R.id.postinganFragmentContainer,
                                PostinganListFragment.newInstance(postinganList)
                            )
                        }
                    } else {
                        Log.d("BerandaActivity", "Tidak ada hasil ditemukan untuk keyword: $keyword")
                        Toast.makeText(this@BerandaActivity, "Tidak ada hasil ditemukan.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("BerandaActivity", "Gagal memuat hasil pencarian: ${response.code()}")
                    Toast.makeText(this@BerandaActivity, "Gagal memuat hasil pencarian", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Postingan>>, t: Throwable) {
                Log.e("BerandaActivity", "Kesalahan jaringan: ${t.message}")
                Toast.makeText(this@BerandaActivity, "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 100
        private const val CAMERA_PERMISSION_CODE = 101
    }
}
