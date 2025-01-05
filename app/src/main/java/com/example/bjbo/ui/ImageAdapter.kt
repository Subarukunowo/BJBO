package com.example.bjbo.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bjbo.R

class ImageAdapter(
    private val images: List<Uri>, // Daftar gambar yang dipilih
    private val onRemoveImage: ((Uri) -> Unit)? = null // Callback untuk menghapus gambar
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    // ViewHolder untuk item gambar
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val removeButton: ImageView = itemView.findViewById(R.id.btnRemoveImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUri = images[position]

        // Tampilkan gambar menggunakan Glide
        Glide.with(holder.itemView.context)
            .load(imageUri)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image) // Placeholder jika gambar sedang dimuat
            .into(holder.imageView)

        // Hapus gambar ketika tombol remove ditekan
        holder.removeButton.setOnClickListener {
            onRemoveImage?.invoke(imageUri)
        }
    }

    override fun getItemCount(): Int = images.size
}
