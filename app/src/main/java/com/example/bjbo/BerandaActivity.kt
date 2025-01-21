package com.example.bjbo

import Postingan
import PostinganListFragment
import android.Manifest
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

        val userId = SharedPreferencesHelper.getUserId(this)
        val userName = SharedPreferencesHelper.getUserName(this)

        if (userId == -1) {
            Toast.makeText(this, "Silakan login kembali.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.tvWelcome.text = "Hey, $userName!"

        binding.ivCamera.setOnClickListener { checkCameraPermission() }

        binding.btnJual.setOnClickListener {
            startActivity(Intent(this, JualActivity::class.java))
        }

        binding.searchBar.setOnEditorActionListener { textView, _, _ ->
            val keyword = textView.text.toString().trim()
            if (keyword.isNotEmpty()) {
                searchPostingan(keyword)
            } else {
                Toast.makeText(this, "Masukkan kata kunci untuk pencarian", Toast.LENGTH_SHORT).show()
            }
            true
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.postinganFragmentContainer,
                    PostinganListFragment(),
                    PostinganListFragment::class.java.simpleName
                )
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_explore -> {
                    startActivity(Intent(this, ExploreActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.menu_camera -> {
                    checkCameraPermission()
                    true
                }
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                else -> false
            }
        }
    }

    private fun searchPostingan(keyword: String) {
        ApiClient.instance.searchPostingan(keyword).enqueue(object : Callback<List<Postingan>> {
            override fun onResponse(call: Call<List<Postingan>>, response: Response<List<Postingan>>) {
                if (response.isSuccessful) {
                    val results = response.body()
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
                        Toast.makeText(this@BerandaActivity, "Tidak ada hasil ditemukan.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@BerandaActivity, "Gagal memuat hasil pencarian", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Postingan>>, t: Throwable) {
                Toast.makeText(this@BerandaActivity, "Kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
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
