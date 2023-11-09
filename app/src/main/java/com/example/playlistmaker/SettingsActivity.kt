package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val imageBack = findViewById<ImageView>(R.id.back_main)
        imageBack.setOnClickListener {
            super.finish()
        }
        val imageGive = findViewById<ImageView>(R.id.image_share_application)
        imageGive.setOnClickListener{
            val reference = getString(R.string.ref)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type="plain/text"
            shareIntent.putExtra(Intent.EXTRA_TEXT, reference)
            startActivity(shareIntent)
        }

        val imageWrite = findViewById<ImageView>(R.id.write_support)
        imageWrite.setOnClickListener{
            val mail: Array<String> = arrayOf(getString(R.string.mail))
            val theme = getString(R.string.theme)
            val message = getString(R.string.message)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type="text/html"
            shareIntent.putExtra(Intent.EXTRA_EMAIL, mail)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, theme)
            startActivity(shareIntent)
        }

        val imageOferta = findViewById<ImageView>(R.id.oferta)
        imageOferta.setOnClickListener{
            val oferta = Uri.parse(getString(R.string.oferta))

            val shareIntent = Intent(Intent.ACTION_VIEW, oferta)
            startActivity(shareIntent)
        }

    }
    fun OnBackPressed(){
        super.onBackPressed()
    }

}