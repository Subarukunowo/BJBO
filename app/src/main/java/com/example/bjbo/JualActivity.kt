package com.example.bjbo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.network.ApiClient
import com.example.bjbo.network.ApiClientNominatim

import com.example.bjbo.databinding.ActivityJualBinding
import com.example.bjbo.model.Barang
import com.example.bjbo.model.NominatimResponse
import com.example.bjbo.ui.ImageAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJualBinding
    private var selectedLocation: String? = null
    private val selectedImages = mutableListOf<Uri>()
    private val MAX_IMAGES = 8
    private val PICK_IMAGES_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol kembali
        binding.ivBack.setOnClickListener { onBackPressed() }

        // Tombol tambah gambar
        binding.btnTambahGambar.setOnClickListener {
            if (selectedImages.size >= MAX_IMAGES) {
                showToast("Maksimal 8 gambar dapat dipilih.")
            } else {
                val intent = Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGES_REQUEST)
            }
        }

        // RecyclerView untuk gambar
        binding.rvGambar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvGambar.adapter = ImageAdapter(selectedImages)

        // Input lokasi dengan pencarian
        binding.etLokasiProduk.setOnItemClickListener { _, _, position, _ ->
            val location = binding.etLokasiProduk.adapter.getItem(position) as String
            selectedLocation = location
            binding.tvLokasiProduk.text = "Lokasi: $location"
        }

        binding.etLokasiProduk.addTextChangedListener { text ->
            if (!text.isNullOrEmpty()) searchLocation(text.toString())
        }

        // Tombol jual sekarang
        binding.btnJualSekarang.setOnClickListener {
            val judul = binding.etJudulProduk.text.toString()
            val deskripsi = binding.etDeskripsiProduk.text.toString()
            val hargaText = binding.etHargaProduk.text.toString()

            if (judul.isEmpty() || deskripsi.isEmpty() || hargaText.isEmpty() || selectedLocation.isNullOrEmpty()) {
                showToast("Mohon lengkapi semua data sebelum memposting produk.")
            } else {
                try {
                    val harga = hargaText.toInt()
                    val barang = Barang(
                        id_barang = "",
                        nama_barang = judul,
                        deskripsi_barang = deskripsi,
                        harga = harga,
                        kategori = "Default",
                        stock = 1,
                        gambar = selectedImages.joinToString(",") { it.toString() },
                        lokasi = selectedLocation!!
                    )
                    postProduct(barang)
                } catch (e: NumberFormatException) {
                    showToast("Harga harus berupa angka.")
                }
            }
        }
    }

    // Fungsi pencarian lokasi
    private fun searchLocation(query: String) {
        ApiClientNominatim.instance.searchLocations(query).enqueue(object : Callback<List<NominatimResponse>> {
            override fun onResponse(call: Call<List<NominatimResponse>>, response: Response<List<NominatimResponse>>) {
                if (response.isSuccessful) {
                    val locations = response.body()?.map { it.display_name } ?: emptyList()
                    val adapter = ArrayAdapter(this@JualActivity, android.R.layout.simple_dropdown_item_1line, locations)
                    binding.etLokasiProduk.setAdapter(adapter)

                    // Periksa apakah aktivitas masih valid sebelum memanggil showDropDown
                    if (!isFinishing && !isDestroyed) {
                        binding.etLokasiProduk.showDropDown()
                    }
                }
            }

            override fun onFailure(call: Call<List<NominatimResponse>>, t: Throwable) {
                if (!isFinishing && !isDestroyed) {
                    showToast("Gagal memuat lokasi: ${t.message}")
                }
            }
        })
    }


    // Fungsi menampilkan Toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Fungsi memposting data ke API
    private fun postProduct(barang: Barang) {
        ApiClient.instance.postBarang(barang).enqueue(object : Callback<Barang> {
            override fun onResponse(call: Call<Barang>, response: Response<Barang>) {
                if (response.isSuccessful) {
                    showToast("Produk berhasil ditambahkan!")
                    finish()
                } else {
                    showToast("Gagal menambahkan produk.")
                }
            }

            override fun onFailure(call: Call<Barang>, t: Throwable) {
                showToast("Terjadi kesalahan: ${t.message}")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let {
                if (it.clipData != null) {
                    val count = it.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = it.clipData!!.getItemAt(i).uri
                        if (selectedImages.size < MAX_IMAGES) selectedImages.add(imageUri)
                    }
                } else if (it.data != null) {
                    val imageUri = it.data!!
                    if (selectedImages.size < MAX_IMAGES) selectedImages.add(imageUri)
                }
                updateRecyclerView()
            }
        }
    }

    private fun updateRecyclerView() {
        (binding.rvGambar.adapter as ImageAdapter).notifyDataSetChanged()
    }
}
