package com.example.bjbo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bjbo.databinding.ActivityExploreBinding
import com.example.bjbo.fragment.ProdukBaruFragment

class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi ViewBinding
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Memuat ProdukBaruFragment ke dalam FrameLayout
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.produkBaruFragmentContainer.id, ProdukBaruFragment())
                .commit()
        }

        // Menyetel item yang aktif di BottomNavigationView
        binding.bottomNavigation.selectedItemId = R.id.nav_explore

        // Menambahkan listener untuk bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Kembali ke Beranda
                    val intent = Intent(this, BerandaActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0) // Tanpa animasi
                    true
                }
                R.id.nav_explore -> {
                    // Tetap di Explore
                    true
                }
                R.id.menu_camera -> {
                    // Periksa izin kamera sebelum membuka kamera
                    checkCameraPermission()
                    true
                }
                R.id.nav_favorite -> {
                    // Navigasi ke FavoriteActivity
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profile -> {
                    // Navigasi ke ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
            // Lakukan sesuatu dengan foto (contoh: tampilkan di ImageView)
            Toast.makeText(this, "Foto berhasil diambil", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Tidak ada foto yang diambil", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 100
        private const val CAMERA_PERMISSION_CODE = 101
    }
}
