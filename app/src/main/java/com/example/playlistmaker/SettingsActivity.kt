package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
        val imageGive = findViewById<ImageView>(R.id.image_settings)
        imageGive.setOnClickListener{
            val ref = getString(R.string.ref)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, ref)
            startActivity(shareIntent)
        }

        val imageWrite = findViewById<ImageView>(R.id.write)
        imageWrite.setOnClickListener{
            val mail = getString(R.string.mail)
            val theme = getString(R.string.theme)
            val message = getString(R.string.message)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, mail)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mail)
            startActivity(shareIntent)
        }

        val imageOferta = findViewById<ImageView>(R.id.oferta)
        imageWrite.setOnClickListener{
            val oferta = Uri.parse(getString(R.string.oferta))

            val shareIntent = Intent(Intent.ACTION_VIEW, oferta)
            startActivity(shareIntent)
        }

    }
}