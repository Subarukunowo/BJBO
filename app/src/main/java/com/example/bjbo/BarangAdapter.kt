package com.example.bjbo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bjbo.model.Barang

class BarangAdapter(
    private var barangList: List<Barang>,
    private val onItemClick: (Barang) -> Unit // Listener untuk klik item
) : RecyclerView.Adapter<BarangAdapter.BarangViewHolder>() {

    // ViewHolder untuk memegang referensi ke item layout
    class BarangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
        val harga: TextView = itemView.findViewById(R.id.tvHarga)
        val gambar: ImageView = itemView.findViewById(R.id.ivGambar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barang, parent, false)
        return BarangViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val barang = barangList[position]

        // Set data ke ViewHolder
        holder.namaBarang.text = barang.nama_barang
        holder.harga.text = "Rp ${barang.harga}"

        // Menggunakan Glide untuk memuat gambar
        Glide.with(holder.itemView.context)
            .load(barang.gambar)
            .placeholder(R.drawable.baseline_image_24) // Placeholder selama gambar dimuat
            .error(R.drawable.baseline_broken_image_24) // Gambar error jika gagal memuat
            .into(holder.gambar)

        // Menangani klik item
        holder.itemView.setOnClickListener {
            onItemClick(barang) // Memanggil listener dengan data item yang diklik
        }
    }

    override fun getItemCount(): Int = barangList.size

    // Fungsi untuk memperbarui data di adapter
    fun updateData(newData: List<Barang>) {
        barangList = newData
        notifyDataSetChanged()
    }
}
