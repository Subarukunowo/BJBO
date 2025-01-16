package com.example.bjbo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bjbo.R
import com.example.bjbo.model.Barang

class BarangbiasaAdapter(
    private var barangList: List<Barang>,
    private val onItemClick: (Barang) -> Unit
) : RecyclerView.Adapter<BarangbiasaAdapter.BarangViewHolder>() { // Perbaiki referensi ke BarangbiasaAdapter

    inner class BarangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaBarang: TextView = itemView.findViewById(R.id.tvNamaBarang)
        val hargaBarang: TextView = itemView.findViewById(R.id.tvHarga)
        val gambarBarang: ImageView = itemView.findViewById(R.id.ivGambar)

        fun bind(barang: Barang) {
            namaBarang.text = barang.nama_barang
            hargaBarang.text = "Rp ${barang.harga}"
            Glide.with(itemView.context)
                .load(barang.gambar)
                .placeholder(R.drawable.placeholder_image)
                .into(gambarBarang)

            itemView.setOnClickListener { onItemClick(barang) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barang, parent, false)
        return BarangViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        holder.bind(barangList[position])
    }

    override fun getItemCount(): Int = barangList.size

    fun updateData(newBarangList: List<Barang>) {
        barangList = newBarangList
        notifyDataSetChanged()
    }
}
