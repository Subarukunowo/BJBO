package com.example.bjbo

import ApiResponse
import Postingan
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bjbo.database.SharedPreferencesHelper
import com.example.bjbo.databinding.ActivityJualBinding
import com.example.bjbo.model.NominatimResponse
import com.example.bjbo.network.ApiClient
import com.example.bjbo.network.ApiClientNominatim
import com.example.bjbo.ui.ImageAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class JualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJualBinding
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var imageAdapter: ImageAdapter
    private val MAX_IMAGES = 8
    private val PICK_IMAGES_REQUEST = 101
    private val MAX_IMAGE_SIZE = 2 * 1024 * 1024 // 2MB in bytes
    private var selectedLocation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        imageAdapter = ImageAdapter(selectedImages)
        binding.rvGambar.apply {
            layoutManager = LinearLayoutManager(this@JualActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener { onBackPressed() }

        binding.btnTambahGambar.setOnClickListener {
            if (selectedImages.size >= MAX_IMAGES) {
                showToast("Maksimal 8 gambar dapat dipilih.")
            } else {
                pickImage()
            }
        }

        binding.etLokasiProduk.apply {
            setOnItemClickListener { _, _, position, _ ->
                val location = adapter.getItem(position) as String
                selectedLocation = location
                setText(location)
            }
            addTextChangedListener { text ->
                if (!text.isNullOrEmpty()) searchLocation(text.toString())
            }
        }

        binding.btnJualSekarang.setOnClickListener {
            if (validateInput()) {
                postPostingan()
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
                    if (binding.etLokasiProduk.hasFocus()) {
                        binding.etLokasiProduk.showDropDown()
                    }
                } else {
                    Log.e("Search Location", "Error: ${response.code()}, ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<NominatimResponse>>, t: Throwable) {
                Log.e("Search Location", "Failure: ${t.localizedMessage}", t)
                showToast("Gagal memuat lokasi: ${t.localizedMessage}")
            }
        })
    }


    private fun validateInput(): Boolean {
        val name = binding.etName.text.toString()
        val price = binding.etPrice.text.toString().toLongOrNull()
        val category = binding.etCategory.text.toString()
        val description = binding.etDescription.text.toString()

        return when {
            name.isEmpty() -> {
                showToast("Nama produk tidak boleh kosong")
                false
            }
            price == null -> {
                showToast("Harga tidak valid")
                false
            }
            category.isEmpty() -> {
                showToast("Kategori tidak boleh kosong")
                false
            }
            description.isEmpty() -> {
                showToast("Deskripsi tidak boleh kosong")
                false
            }
            selectedLocation.isNullOrEmpty() -> {
                showToast("Lokasi tidak boleh kosong")
                false
            }
            selectedImages.isEmpty() -> {
                showToast("Mohon tambahkan setidaknya satu gambar")
                false
            }
            else -> true
        }
    }

    private fun postPostingan() {
        val name = binding.etName.text.toString()
        val price = binding.etPrice.text.toString()
        val category = binding.etCategory.text.toString()
        val description = binding.etDescription.text.toString()
        val lokasi = selectedLocation.orEmpty()
        val status = "belum disetujui"
        val userId = SharedPreferencesHelper.getUserId(this).toString()
        val username = SharedPreferencesHelper.getUserName(this)

        // Prepare image
        val imageUri = selectedImages.firstOrNull()
        val imageFile = imageUri?.let { getFileFromUri(it) }

        if (imageFile == null || imageFile.length() > MAX_IMAGE_SIZE) {
            showToast("Gagal memproses gambar atau ukuran terlalu besar")
            return
        }

        // Prepare multipart data
        val imagePart = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        )

        val requestBody = mapOf(
            "name" to name.toRequestBody("text/plain".toMediaTypeOrNull()),
            "price" to price.toRequestBody("text/plain".toMediaTypeOrNull()),
            "category" to category.toRequestBody("text/plain".toMediaTypeOrNull()),
            "description" to description.toRequestBody("text/plain".toMediaTypeOrNull()),
            "lokasi" to lokasi.toRequestBody("text/plain".toMediaTypeOrNull()),
            "status" to status.toRequestBody("text/plain".toMediaTypeOrNull()),
            "user_id" to userId.toRequestBody("text/plain".toMediaTypeOrNull()),
            "username" to username.toRequestBody("text/plain".toMediaTypeOrNull())
        )

        // API Call
        ApiClient.instance.addPostingan(
            requestBody["name"]!!,
            requestBody["price"]!!,
            requestBody["category"]!!,
            requestBody["description"]!!,
            imagePart,
            requestBody["lokasi"]!!,
            requestBody["status"]!!,
            requestBody["user_id"]!!,
            requestBody["username"]!!
        ).enqueue(object : Callback<ApiResponse<Postingan>> {
            override fun onResponse(call: Call<ApiResponse<Postingan>>, response: Response<ApiResponse<Postingan>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        showToast("Postingan berhasil ditambahkan!")

                        // Navigasi ke BerandaActivity
                        val intent = Intent(this@JualActivity, BerandaActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish() // Menutup JualActivity
                    } else {
                        Log.e("API Error", "Gagal: ${response.errorBody()?.string()}")
                        showToast("Gagal menambahkan postingan.")
                    }
                } else {
                    Log.e("API Error", "Error: ${response.errorBody()?.string()}")
                    showToast("Gagal menambahkan postingan.")
                }
            }


            override fun onFailure(call: Call<ApiResponse<Postingan>>, t: Throwable) {
                Log.e("API Failure", "Error: ${t.localizedMessage}")
                showToast("Terjadi kesalahan saat menghubungi server.")
            }
        })
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGES_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let {
                if (it.clipData != null) {
                    val count = it.clipData!!.itemCount.coerceAtMost(MAX_IMAGES - selectedImages.size)
                    for (i in 0 until count) {
                        val imageUri = it.clipData!!.getItemAt(i).uri
                        if (!selectedImages.contains(imageUri)) {
                            selectedImages.add(imageUri)
                        }
                    }
                } else if (it.data != null) {
                    val imageUri = it.data!!
                    if (!selectedImages.contains(imageUri)) {
                        selectedImages.add(imageUri)
                    }
                }
                updateRecyclerView()
            }
        }
    }

    private fun updateRecyclerView() {
        imageAdapter.notifyDataSetChanged()
    }

    private fun getFileFromUri(uri: Uri): File? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                file
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
