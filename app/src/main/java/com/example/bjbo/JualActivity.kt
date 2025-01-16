package com.example.bjbo

import ApiResponse
import Postingan
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.databinding.ActivityJualBinding
import com.example.bjbo.model.NominatimResponse
import com.example.bjbo.network.ApiClient
import com.example.bjbo.network.ApiClientNominatim
import com.example.bjbo.ui.ImageAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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

        // AutoCompleteTextView dan lokasi yang dipilih
        binding.etLokasiProduk.setOnItemClickListener { _, _, position, _ ->
            val location = binding.etLokasiProduk.adapter.getItem(position) as String
            selectedLocation = location
            binding.etLokasiProduk.setText(location)
        }

        binding.etLokasiProduk.addTextChangedListener { text ->
            if (!text.isNullOrEmpty()) searchLocation(text.toString())
        }

        // Tombol jual sekarang
        binding.btnJualSekarang.setOnClickListener {
            val name = binding.etName.text.toString()
            val price = binding.etPrice.text.toString().toLongOrNull()
            val category = binding.etCategory.text.toString()
            val description = binding.etDescription.text.toString()
            val lokasi = selectedLocation

            if (name.isEmpty() || price == null || category.isEmpty() || description.isEmpty() || lokasi.isNullOrEmpty()) {
                showToast("Mohon lengkapi semua data sebelum memposting produk.")
            } else if (selectedImages.isEmpty()) {
                showToast("Mohon tambahkan setidaknya satu gambar.")
            } else {
                postPostingan(name, price, category, description, lokasi)
            }
        }
    }

    private fun searchLocation(query: String) {
        ApiClientNominatim.instance.searchLocations(query).enqueue(object : Callback<List<NominatimResponse>> {
            override fun onResponse(call: Call<List<NominatimResponse>>, response: Response<List<NominatimResponse>>) {
                if (response.isSuccessful) {
                    val locations = response.body()?.map { it.display_name } ?: emptyList()
                    val adapter = ArrayAdapter(
                        this@JualActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        locations
                    )
                    binding.etLokasiProduk.setAdapter(adapter)
                    binding.etLokasiProduk.showDropDown()
                } else {
                    showToast("Gagal memuat lokasi.")
                }
            }

            override fun onFailure(call: Call<List<NominatimResponse>>, t: Throwable) {
                showToast("Gagal memuat lokasi: ${t.localizedMessage}")
            }
        })
    }

    private fun postPostingan(
        name: String,
        price: Long,
        category: String,
        description: String,
        lokasi: String
    ) {
        val imagePart = selectedImages.firstOrNull()?.let { uri ->
            getFileFromUri(uri)?.let { file ->
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
        }

        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val lokasiPart = lokasi.toRequestBody("text/plain".toMediaTypeOrNull())
        val statusPart = "belum disetujui".toRequestBody("text/plain".toMediaTypeOrNull())
        val userIdPart = "1".toRequestBody("text/plain".toMediaTypeOrNull())
        val usernamePart = "sandy".toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.instance.addPostingan(
            namePart,
            pricePart,
            categoryPart,
            descriptionPart,
            imagePart,
            lokasiPart,
            statusPart,
            userIdPart,
            usernamePart
        ).enqueue(object : Callback<ApiResponse<Postingan>> {
            override fun onResponse(
                call: Call<ApiResponse<Postingan>>,
                response: Response<ApiResponse<Postingan>>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    showToast("Postingan berhasil ditambahkan!")
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    showToast("Gagal menambahkan postingan.")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Postingan>>, t: Throwable) {
                showToast("Terjadi kesalahan: ${t.localizedMessage}")
            }
        })
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            file
        } catch (e: Exception) {
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
