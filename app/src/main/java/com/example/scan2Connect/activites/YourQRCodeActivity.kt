package com.example.scan2Connect.activites

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.scan2Connect.R

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
}
