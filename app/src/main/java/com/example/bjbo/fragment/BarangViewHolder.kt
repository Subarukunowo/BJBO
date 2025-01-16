package com.example.bjbo.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bjbo.R
import com.example.bjbo.model.Barang
import com.squareup.picasso.Picasso

class BarangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val namaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
    private val hargaBarang: TextView = itemView.findViewById(R.id.tvHarga)
    private val gambarBarang: ImageView = itemView.findViewById(R.id.ivGambar)

    fun bind(barang: Barang) {
        namaBarang.text = barang.nama_barang
        hargaBarang.text = "Rp ${barang.harga}"
        // Load image using Picasso/Glide
        Picasso.get().load(barang.gambar).into(gambarBarang)
    }
}
