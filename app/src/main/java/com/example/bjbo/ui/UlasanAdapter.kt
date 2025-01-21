package com.example.bjbo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bjbo.R
import com.example.bjbo.model.Ulasan

class UlasanAdapter(
    private val ulasanList: List<Ulasan>
) : RecyclerView.Adapter<UlasanAdapter.UlasanViewHolder>() {

    inner class UlasanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUser: TextView = itemView.findViewById(R.id.tvUser)
        private val rbRating: RatingBar = itemView.findViewById(R.id.rbRating)
        private val tvKomentar: TextView = itemView.findViewById(R.id.tvKomentar)
        private val tvCreatedAt: TextView = itemView.findViewById(R.id.tvTanggal)

        fun bind(ulasan: Ulasan) {
            // Set nama user atau default jika null
            tvUser.text = ulasan.user?.name ?: "Unknown User"

            // Set nilai rating
            rbRating.rating = ulasan.rating.toFloat()

            // Set komentar
            tvKomentar.text = ulasan.komentar

            // Set tanggal pembuatan atau default jika null
            tvCreatedAt.text = ulasan.created_at ?: "Unknown"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UlasanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ulasan, parent, false)
        return UlasanViewHolder(view)
    }

    override fun onBindViewHolder(holder: UlasanViewHolder, position: Int) {
        holder.bind(ulasanList[position])
    }

    override fun getItemCount(): Int = ulasanList.size
}
