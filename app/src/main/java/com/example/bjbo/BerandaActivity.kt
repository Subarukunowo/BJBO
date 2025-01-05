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
import androidx.fragment.app.commit
import com.example.bjbo.databinding.ActivityBerandaBinding
import com.example.bjbo.fragment.ProdukBaruFragment

class BerandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerandaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tambahkan fragment ProdukBaruFragment
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.produkBaruFragmentContainer, // ID dari FrameLayout
                    ProdukBaruFragment(), // Fragment yang akan ditampilkan
                    ProdukBaruFragment::class.java.simpleName // Tag opsional
                )
            }
        }

        // Tambahkan listener untuk ikon kamera
        binding.ivCamera.setOnClickListener {
            checkCameraPermission()
        }

        // Listener untuk tombol JUAL
        binding.btnJual.setOnClickListener {
            Toast.makeText(this, "Tombol JUAL diklik!", Toast.LENGTH_SHORT).show()
            // Navigasi ke JualActivity
            val intent = Intent(this, JualActivity::class.java)
            startActivity(intent)
        }

        // Listener untuk bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Tetap di Beranda
                    true
                }
                R.id.nav_explore -> {
                    // Navigasi ke ExploreActivity
                    val intent = Intent(this, ExploreActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_camera -> {
                    // Buka kamera
                    checkCameraPermission()
                    true
                }
                R.id.nav_favorite -> {
                    // Navigasi ke FavoriteActivity
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Navigasi ke ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
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
            if (photo != null) {
                binding.profileImage.setImageBitmap(photo) // Menampilkan gambar di profileImage
            } else {
                Toast.makeText(this, "Gagal mengambil foto", Toast.LENGTH_SHORT).show()
            }
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
                Toast.makeText(this, "Izin kamera diperlukan untuk mengambil foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 100
        private const val CAMERA_PERMISSION_CODE = 101
    }
}
