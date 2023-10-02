package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        val buttonMedia = findViewById<Button>(R.id.button_media)

        buttonSearch.setOnClickListener{
            val intentDisplay = Intent(this, SearchingActivity::class.java)
            startActivity(intentDisplay)
        }
        buttonSettings.setOnClickListener{
            val intentDisplay = Intent(this, SettingsActivity::class.java)
            startActivity(intentDisplay)
        }
        buttonMedia.setOnClickListener{
            val intentDisplay = Intent(this, MediaActivity::class.java)
            startActivity(intentDisplay)
        }
    }
}