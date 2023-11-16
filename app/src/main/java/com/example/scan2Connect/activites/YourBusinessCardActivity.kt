package com.example.scan2Connect.activites

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.example.scan2Connect.R

class YourBusinessCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_business_card)

        val btnEditusinessCard : CardView = findViewById(R.id.btn_edit_business_card)
        btnEditusinessCard.setOnClickListener {
            val intent = Intent(this, EditBusinessCardActivity::class.java)
            startActivity(intent)
        }

        val btnShowQrCode : CardView = findViewById(R.id.btn_show_qr_code)
        btnShowQrCode.setOnClickListener {
            val intent = Intent(this, YourQRCodeActivity::class.java)
            startActivity(intent)
        }
    }
}
