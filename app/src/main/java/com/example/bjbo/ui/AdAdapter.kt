package com.example.bjbo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bjbo.R
import com.example.bjbo.model.AdItem

class AdAdapter(private val adList: List<AdItem>) :
    RecyclerView.Adapter<AdAdapter.AdViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_item, parent, false)
        return AdViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        val adItem = adList[position]
        holder.bind(adItem)
    }

    override fun getItemCount(): Int = adList.size

    class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val adImage: ImageView = itemView.findViewById(R.id.ivAdImage)
        private val adTitle: TextView = itemView.findViewById(R.id.tvAdTitle)
        private val adDescription: TextView = itemView.findViewById(R.id.tvAdDescription)

        fun bind(adItem: AdItem) {
            adImage.setImageResource(adItem.imageResId)
            adTitle.text = adItem.title
            adDescription.text = adItem.description
        }
    }
}
