package com.example.bjbo

/*
class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProdukBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil ID Barang dari intent
        val idBarang = intent.getStringExtra("ID_BARANG")
        if (idBarang != null) {
            Log.d("DetailProductActivity", "ID Barang: $idBarang")
            fetchProductDetail(idBarang)
        } else {
            Toast.makeText(this, "Produk tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Tombol kembali
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchProductDetail(idBarang: String) {
        ApiClient.instance.getBarangDetail(idBarang).enqueue(object : Callback<Barang> {
            override fun onResponse(call: Call<Barang>, response: Response<Barang>) {
                if (response.isSuccessful && response.body() != null) {
                    bindProductDetail(response.body()!!)
                } else {
                    Toast.makeText(
                        this@DetailProductActivity,
                        "Gagal memuat detail produk",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Barang>, t: Throwable) {
                Toast.makeText(
                    this@DetailProductActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        })
    }

    private fun bindProductDetail(barang: Barang) {
        binding.apply {
            // Format harga dengan pemisah ribuan
            val formattedPrice =
                NumberFormat.getNumberInstance(Locale("id", "ID")).format(barang.harga)

            tvProductTitle.text = barang.nama_barang
            tvSellerName.text = "Penjual: ${barang.kategori}"
            tvProductPrice.text = "Rp $formattedPrice"
            tvProductDescription.text = barang.deskripsi_barang
            tvProductCondition.text = "Stok: ${barang.stock}"

            // Load gambar produk menggunakan Glide
            Glide.with(this@DetailProductActivity)
                .load(barang.gambar)
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_broken_image_24)
                .into(ivProductImage)
        }

        // Event untuk tombol Simpan
        binding.btnSave.setOnClickListener {
            Toast.makeText(
                this@DetailProductActivity,
                "Produk berhasil disimpan ke daftar favorit",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Event untuk tombol Beli
        binding.btnBuy.setOnClickListener {
            val intent = Intent(this, PembayaranActivity::class.java)
            // Tambahkan data yang akan dikirim ke PembayaranActivity
            intent.putExtra("NAMA_BARANG", "Nama Produk Anda") // Ganti sesuai data barang
            intent.putExtra("HARGA_BARANG", 100000) // Ganti sesuai harga produk
            startActivity(intent) // Mulai aktivitas PembayaranActivity
        }
    }
}*/