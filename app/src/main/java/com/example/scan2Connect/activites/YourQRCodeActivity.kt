package com.example.scan2Connect.activites

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.scan2Connect.R
import com.example.scan2Connect.domain.qr.ContactData
import com.example.scan2Connect.domain.qr.exception.NoUserDataException
import qrcode.QRCode
import qrcode.render.QRCodeGraphics

class YourQRCodeActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_qr_code)

        val btnBack : ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        renderQrCode()
    }

    /**
     * Render contact data as QR code and display it
     */
    private fun renderQrCode() {
        val qrImgView: ImageView = findViewById(R.id.qr_code)

        val sharedPref = this.getSharedPreferences("com.example.scan2Connect.shared_prefs", Context.MODE_PRIVATE)
        var renderedQr: QRCodeGraphics? = null
        try {
            renderedQr = ContactData.generateContactQr(sharedPref)
        } catch (e: NoUserDataException) {
            Log.d("qr", "Not generating QR on your QR activity because no user data exists")
        }

        if (renderedQr === null) {
            return
        }

        qrImgView.setImageBitmap(renderedQr.nativeImage() as Bitmap?)
    }
}
