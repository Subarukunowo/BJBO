package com.example.bjbo.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Barang(
    @SerializedName("id_barang") val id_barang: String,
    @SerializedName("nama_barang") val nama_barang: String,
    @SerializedName("kategori") val kategori: String,
    @SerializedName("harga") val harga: Int,
    @SerializedName("deskripsi_barang") val deskripsi_barang: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("gambar") val gambar: String,
    @SerializedName("lokasi") val lokasi: String,

) : Parcelable
