package com.example.bjbo.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.bjbo.R

class PopupSplashScreen(context: Context) : Dialog(context) {

    private var onCloseListener: (() -> Unit)? = null

    fun setOnCloseListener(listener: () -> Unit) {
        onCloseListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_splashscreen)

        // Setup animasi jika ada
        val splashImage = findViewById<ImageView>(R.id.ivSplashImage)
        val animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
        splashImage.startAnimation(animation)

        // Tombol Tutup
        val btnClose = findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            val closeAnimation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_out_top)
            splashImage.startAnimation(closeAnimation)

            // Setelah animasi keluar selesai, tutup dialog
            splashImage.postDelayed({
                dismiss()
                onCloseListener?.invoke()
            }, closeAnimation.duration)
        }
    }
}
