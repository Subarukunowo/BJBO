package com.example.bjbo

import android.app.Application
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inisialisasi SDK Midtrans
        SdkUIFlowBuilder.init()
            .setClientKey("Mid-client-TC-UNMd_L9PU08lx") // Ganti dengan Client Key Sandbox Anda
            .setContext(this)
            .setMerchantBaseUrl("C:\\xampp\\htdocs\\myapp\\generate_snap_token.php") // Ganti dengan URL server Anda
            .enableLog(true) // Aktifkan log untuk debugging
            .buildSDK()
    }
}