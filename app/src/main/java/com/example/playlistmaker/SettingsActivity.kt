package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imageBack = findViewById<ImageView>(R.id.back_main)
        imageBack.setOnClickListener {
            val intentDisplay = Intent(this, MainActivity::class.java)
            startActivity(intentDisplay)
        }
    }
}