package com.example.bjbo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bjbo.databinding.ItemFlashsaleBinding
import com.example.bjbo.model.FlashSale

class FlashSaleAdapter(
    private var flashSaleList: List<FlashSale>,
    private val onItemClick: (FlashSale) -> Unit
) : RecyclerView.Adapter<FlashSaleAdapter.FlashSaleViewHolder>() {

    inner class FlashSaleViewHolder(private val binding: ItemFlashsaleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flashSale: FlashSale) {
            binding.tvNamaBarang.text = flashSale.nama_barang
            binding.tvHargaOriginal.text = "Rp ${flashSale.harga_barang}"
            binding.tvHargaDiskon.text = "Rp ${flashSale.harga_flashsale}"

            Glide.with(binding.root.context)
                .load(flashSale.gambar)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .into(binding.ivGambar)

            binding.root.setOnClickListener {
                onItemClick(flashSale)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashSaleViewHolder {
        val binding = ItemFlashsaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlashSaleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlashSaleViewHolder, position: Int) {
        holder.bind(flashSaleList[position])
    }

    override fun getItemCount(): Int = flashSaleList.size

    fun updateData(newData: List<FlashSale>) {
        flashSaleList = newData
        notifyDataSetChanged()
    }
}
