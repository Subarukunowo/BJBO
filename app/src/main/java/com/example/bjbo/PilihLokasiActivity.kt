package com.example.bjbo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bjbo.databinding.ActivityPilihLokasiBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class PilihLokasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPilihLokasiBinding
    private lateinit var mapView: MapView
    private var selectedMarker: Marker? = null // Simpan referensi marker yang dipilih

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Konfigurasi OSMDroid
        Configuration.getInstance().load(this, this.getPreferences(MODE_PRIVATE))

        binding = ActivityPilihLokasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi MapView
        mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Atur lokasi default (misal Jakarta)
        val startPoint = GeoPoint(-6.200000, 106.816666)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(startPoint)

        // Tambahkan listener untuk memilih lokasi
        mapView.setOnClickListener {
            // Hapus marker sebelumnya jika ada
            selectedMarker?.let { marker -> mapView.overlays.remove(marker) }

            // Buat marker baru
            val geoPoint = GeoPoint(mapView.mapCenter.latitude, mapView.mapCenter.longitude)
            val marker = Marker(mapView)
            marker.position = geoPoint
            marker.title = "Lokasi Terpilih"
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)

            // Simpan referensi marker baru
            selectedMarker = marker
        }

        // Tombol Pilih Lokasi
        binding.btnPilihLokasi.setOnClickListener {
            if (selectedMarker != null) {
                val resultIntent = Intent().apply {
                    putExtra("LOCATION", "${selectedMarker!!.position.latitude},${selectedMarker!!.position.longitude}")
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Silakan pilih lokasi terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
