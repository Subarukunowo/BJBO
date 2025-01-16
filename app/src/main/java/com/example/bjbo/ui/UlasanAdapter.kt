package com.example.bjbo.ui


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bjbo.R
import com.example.bjbo.model.Ulasan

class UlasanAdapter(private val ulasanList: List<Ulasan>) :
    RecyclerView.Adapter<UlasanAdapter.UlasanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UlasanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ulasan, parent, false)
        return UlasanViewHolder(view)
    }

    override fun onBindViewHolder(holder: UlasanViewHolder, position: Int) {
        val ulasan = ulasanList[position]
        holder.bind(ulasan)
    }

    override fun getItemCount(): Int = ulasanList.size

    class UlasanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val reviewTextView: TextView = itemView.findViewById(R.id.reviewTextView)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rbUserRating)

        fun bind(ulasan: Ulasan) {
            nameTextView.text = "User ${ulasan.user_id}" // Contoh: sesuaikan dengan data user
            timeTextView.text = ulasan.created_at
            reviewTextView.text = ulasan.komentar

            // Set rating value
            ratingBar.rating = ulasan.rating.toFloat()
        }
    }
}
