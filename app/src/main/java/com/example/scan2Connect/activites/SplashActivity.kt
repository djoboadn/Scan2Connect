package com.example.scan2Connect.activites

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.scan2Connect.R

class SplashActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val textView = findViewById<TextView>(R.id.tv_splash_loading)
        val context = this

        val runnable: Runnable = object : Runnable {
            var count = 0

            override fun run() {
                count++
                var updateText = ""
                when (count) {
                    1 -> {
                        updateText = resources.getString(R.string.updating_data) + "."
                    }
                    2 -> {
                        updateText = resources.getString(R.string.updating_data) + ".."
                    }
                    3 -> {
                        updateText = resources.getString(R.string.updating_data) + "..."
                    }
                    4 -> {
                        updateText = resources.getString(R.string.updating_data)
                    }
                }
                textView.text = updateText
                if(count == 4){
                    textView.visibility = View.GONE
                    val intent = Intent(context, YourBusinessCardActivity::class.java)
                    startActivity(intent)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(this, 700)
                }
            }
        }

        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }
}
