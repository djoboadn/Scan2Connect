package com.example.scan2Connect.activites

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.example.scan2Connect.R

class EditBusinessCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_business_card)

        val btnBack : ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener{
            finish()
        }

    }
}
