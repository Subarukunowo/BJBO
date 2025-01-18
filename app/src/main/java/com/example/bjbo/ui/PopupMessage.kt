package com.example.bjbo.ui



import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.example.bjbo.R

class PopupMessage(context: Context, private val message: String) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_message)

        // Set pesan di TextView
        val tvMessage = findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = message

        // Tutup popup setelah beberapa waktu (opsional)
        setCancelable(true) // Dapat ditutup dengan back
    }
}
