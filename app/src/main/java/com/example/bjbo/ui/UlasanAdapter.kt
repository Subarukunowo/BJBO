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

class UlasanAdapter(
    private val context: Context,
    private val ulasanList: List<Ulasan>
) : RecyclerView.Adapter<UlasanAdapter.UlasanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UlasanViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ulasan, parent, false)
        return UlasanViewHolder(view)
    }

    override fun onBindViewHolder(holder: UlasanViewHolder, position: Int) {
        val ulasan = ulasanList[position]
        holder.tvUserName.text = "User ID: ${ulasan.user_id}" // Anda dapat mengganti dengan nama pengguna jika ada
        holder.tvComment.text = ulasan.komentar
        holder.tvCreatedAt.text = ulasan.created_at
        holder.ratingBar.rating = ulasan.rating.toFloat()
    }

    override fun getItemCount(): Int {
        return ulasanList.size
    }

    class UlasanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvComment: TextView = itemView.findViewById(R.id.tvComment)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }
}
