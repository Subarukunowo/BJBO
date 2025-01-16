package com.example.bjbo.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bjbo.R
import com.example.bjbo.model.Laporan

class LaporanAdapter(
    private val context: Context,
    private val laporanList: List<Laporan>
) : RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_laporan, parent, false)
        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        val laporan = laporanList[position]

        holder.tvJenisLaporan.text = laporan.jenis_laporan
        holder.tvTeksLaporan.text = laporan.teks_laporan
        holder.tvStatus.text = laporan.status
        holder.tvTanggal.text = laporan.created_at
    }

    override fun getItemCount(): Int {
        return laporanList.size
    }

    class LaporanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJenisLaporan: TextView = itemView.findViewById(R.id.tvJenisLaporan)
        val tvTeksLaporan: TextView = itemView.findViewById(R.id.tvTeksLaporan)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
    }
}
