package com.example.bjbo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bjbo.database.SharedPreferencesHelper
import com.example.bjbo.databinding.ActivityDetailPostinganBinding
import com.example.bjbo.fragment.UlasanListFragment
import com.example.bjbo.model.ApiResponse
import com.example.bjbo.model.Favorit
import com.example.bjbo.model.Ulasan
import com.example.bjbo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class DetailPostinganActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostinganBinding
    private var isLiked = false
    private val favoriteList = mutableListOf<Favorit>()
    private val apiService = ApiClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostinganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val name = intent.getStringExtra("name") ?: "Unknown"
        val priceString = intent.getStringExtra("price") ?: "0"
        val price = try {
            NumberFormat.getCurrencyInstance(Locale("id", "ID")).parse(priceString)?.toLong() ?: 0L
        } catch (e: Exception) {
            0L
        }
        val location = intent.getStringExtra("location") ?: "Unknown"
        val description = intent.getStringExtra("description") ?: "No description"
        val image = intent.getStringExtra("image") ?: ""

        // Set data ke UI
        binding.tvPostinganName.text = name
        binding.tvPostinganPrice.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(price)
        binding.tvPostinganLocation.text = location
        binding.tvPostinganDescription.text = description

        // Load image menggunakan Glide
        Glide.with(this)
            .load(image) // URL gambar dari API
            .into(binding.ivPostinganImage)
        binding.etUserReview.setOnClickListener {
            navigateToUlasanActivity()
        }
        // Tambahkan animasi pada ikon heart
        val favorit = Favorit(
            id = 1, // Sesuaikan ID favorit jika diperlukan
            user_id = SharedPreferencesHelper.getUserId(this), // User ID (contoh)
            postingan_id = SharedPreferencesHelper.getPostinganId(this),
            created_at = null,
            updated_at = null,
            user = null,
            postingan = null
        )
        setupHeartIcon(binding.ivHeartIcon, favorit)

        // Tombol Beli
        binding.btnBuy.setOnClickListener {
            if (price > 0L) {
                navigateToPembayaranActivity(name, price, location, description, image)
            } else {
                showToast("Harga tidak valid")
            }
        }
    }

    private fun setupHeartIcon(heartIcon: ImageView, favorit: Favorit) {
        // Cek status awal dari SharedPreferences
        isLiked = SharedPreferencesHelper.isFavoriteId(this, favorit.postingan_id)

        // Set ikon sesuai status awal
        heartIcon.setImageResource(if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart)

        heartIcon.setOnClickListener {
            // Muat animasi
            val animation = AnimationUtils.loadAnimation(this, R.anim.anim_heart_scale)
            heartIcon.startAnimation(animation)

            // Toggle status like
            if (isLiked) {
                heartIcon.setImageResource(R.drawable.ic_heart) // Ganti ikon jadi tidak di-like
                removeFromFavorites(favorit) // Panggil fungsi untuk menghapus dari favorit
            } else {
                heartIcon.setImageResource(R.drawable.ic_heart_filled) // Ganti ikon jadi di-like
                addToFavorites(favorit) // Panggil fungsi untuk menambahkan ke favorit
            }
            isLiked = !isLiked
        }
    }


    private fun removeFromFavorites(favorit: Favorit) {
        if (favorit.id <= 0) {
            Log.e("RemoveFromFavorites", "ID favorit tidak valid: ${favorit.id}")
            showToast("Gagal menghapus dari favorit. ID tidak valid.")
            return
        }

        apiService.deleteFavorit(favorit.id.toLong()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Hapus postingan_id dari SharedPreferences
                    SharedPreferencesHelper.removeFavoriteId(this@DetailPostinganActivity, favorit.postingan_id)

                    // Hapus dari daftar lokal
                    favoriteList.remove(favorit)

                    showToast("Berhasil dihapus dari favorit dengan ID: ${favorit.id}")
                    Log.d(
                        "RemoveFromFavorites",
                        "Berhasil menghapus favorit dengan ID: ${favorit.id}"
                    )
                } else {
                    showToast("Gagal menghapus dari favorit: ${response.code()}")
                    Log.e(
                        "RemoveFromFavorites",
                        "Gagal menghapus: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Kesalahan: ${t.message}")
                Log.e("RemoveFromFavorites", "Kesalahan: ${t.message}")
            }
        })
    }
    private fun loadUlasanList(postinganId: Int) {
        apiService.getAllUlasans().enqueue(object : Callback<ApiResponse<List<Ulasan>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Ulasan>>>,
                response: Response<ApiResponse<List<Ulasan>>>
            ) {
                if (response.isSuccessful) {
                    val ulasanResponse = response.body()
                    ulasanResponse?.data?.let { ulasans ->
                        if (ulasans is List<Ulasan>) {
                            // Log jumlah ulasan yang diterima
                            Log.d("UlasanResponse", "Jumlah ulasan yang diterima: ${ulasans.size}")

                            // Kirim ID postingan untuk memuat ulasan
                            loadUlasanFragment(postinganId)
                        } else {
                            showToast("Format data ulasan tidak valid.")
                            Log.e("UlasanResponse", "Data bukan List<Ulasan>")
                        }
                    } ?: run {
                        showToast("Tidak ada ulasan untuk postingan ini.")
                        Log.d("UlasanResponse", "Tidak ada ulasan yang tersedia untuk ID $postinganId")
                    }
                } else {
                    showToast("Gagal memuat ulasan: ${response.message()}")
                    Log.e("UlasanResponse", "Kesalahan memuat ulasan: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Ulasan>>>, t: Throwable) {
                showToast("Kesalahan: ${t.message}")
                Log.e("UlasanResponse", "Kesalahan jaringan: ${t.message}")
            }
        })
    }

    private fun loadUlasanFragment(postinganId: Int) {
        Log.d("UlasanFragment", "Memuat ulasan untuk ID postingan: $postinganId")
        val fragment = UlasanListFragment.newInstance(postinganId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.ulasanListFragmentContainer, fragment)
            .commit()
        loadUlasanList(postinganId) // Panggil metode untuk memuat ulasan
        Log.d("UlasanFragment", "Fragment ulasan berhasil dimuat.")
    }


    private fun addToFavorites(favorit: Favorit) {
        apiService.createFavorit(favorit).enqueue(object : Callback<Favorit> {
            override fun onResponse(call: Call<Favorit>, response: Response<Favorit>) {
                if (response.isSuccessful) {
                    response.body()?.let { favoritResponse ->
                        // Perbarui ID favorit
                        favorit.id = favoritResponse.id

                        // Tambahkan postingan_id ke SharedPreferences
                        SharedPreferencesHelper.addFavoriteId(this@DetailPostinganActivity, favoritResponse.postingan_id)

                        // Tambahkan ke daftar lokal (jika ada)
                        favoriteList.add(favoritResponse)

                        showToast("Berhasil ditambahkan ke favorit dengan ID: ${favoritResponse.id}")
                        Log.d("AddToFavorites", "Favorit berhasil ditambahkan: ${favoritResponse.id}")
                    }
                } else {
                    showToast("Gagal menambahkan ke favorit")
                    Log.e("AddToFavorites", "Respons gagal: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Favorit>, t: Throwable) {
                showToast("Kesalahan: ${t.message}")
                Log.e("AddToFavorites", "Kesalahan API: ${t.message}")
            }
        })
    }


    private fun navigateToPembayaranActivity(
        name: String,
        price: Long,
        location: String,
        description: String,
        image: String
    ) {
        val intent = Intent(this, PembayaranActivity::class.java).apply {
            putExtra("name", name)
            putExtra("price", price)
            putExtra("location", location)
            putExtra("description", description)
            putExtra("image", image)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToUlasanActivity() {
        val intent = Intent(this, UlasanActivity::class.java)
        startActivity(intent)
    }


}
