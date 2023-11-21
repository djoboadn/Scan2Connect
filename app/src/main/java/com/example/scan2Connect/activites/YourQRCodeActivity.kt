package com.example.scan2Connect.activites

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.scan2Connect.R
import qrcode.QRCode

class YourQRCodeActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_qr_code)

        val btnBack : ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        renderQrCode()
    }

    /**
     * Render contact data as QR code and display it
     */
    private fun renderQrCode() {
        val qrImgView : ImageView = findViewById(R.id.qr_code)

        val contactQr = QRCode.ofSquares()
            .build("Hello world, 123!") // TODO: change to encoded contact data
        val renderedQr = contactQr.render()

        qrImgView.setImageBitmap(renderedQr.nativeImage() as Bitmap?)
    }
}
