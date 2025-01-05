package com.example.bjbo.model

data class NominatimResponse(
    val display_name: String, // Nama lokasi yang lengkap
    val lat: String,          // Latitude lokasi
    val lon: String           // Longitude lokasi
)