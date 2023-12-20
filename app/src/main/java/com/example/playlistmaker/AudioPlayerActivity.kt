package com.example.playlistmaker

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.provider.MediaStore.Audio
import android.provider.MediaStore.Images
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.util.*

class AudioPlayerActivity: AppCompatActivity() {
    lateinit var buttonBackAudioplayer: ImageView
    lateinit var imageAlbum: ImageView
    lateinit var nameTrack: TextView
    lateinit var nameGroup: TextView
    lateinit var buttonAdd: ImageButton
    lateinit var buttonPlay: ImageButton
    lateinit var buttonLike: ImageButton
    lateinit var timeToEnd: TextView
    lateinit var timeNumber: TextView
    lateinit var nameAlbum: TextView
    lateinit var year: TextView
    lateinit var zhanr: TextView
    lateinit var country:TextView
    lateinit var track: String
    lateinit var musicTrack: MusicTrack

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)
        buttonBackAudioplayer = findViewById(R.id.buttonBackAudioplayer)
        timeNumber=findViewById(R.id.timeNumber)
        nameTrack=findViewById(R.id.trackName)
        nameGroup=findViewById(R.id.artistName)
        imageAlbum=findViewById(R.id.imageAlbum)
        nameAlbum=findViewById(R.id.albumName)
        year=findViewById(R.id.yearNumber)
        zhanr=findViewById(R.id.zhanr)
        country=findViewById(R.id.country)

        buttonBackAudioplayer.setOnClickListener {
            this.finish()
        }
        track = intent.getStringExtra("track").toString()
        musicTrack = createTrackFromJson(track)
        initialization()

        }


    private fun createTrackFromJson(json:String):MusicTrack{
        val track = Gson().fromJson(json, MusicTrack::class.java)
        return track
    }
    private fun initialization(){
        timeNumber.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(musicTrack.trackTimeMillis).toString()
        nameTrack.text=musicTrack.trackName
        nameGroup.text=musicTrack.artistName
        val radius:Int = this.resources.getInteger(R.integer.size_8)
        Glide.with(this)
            .load(musicTrack.getCoverArtwork())
            .placeholder(R.drawable.placeholder_max)
            .centerCrop()
            .transform(RoundedCorners(radius))
            .into(imageAlbum)
        nameAlbum.text=musicTrack.collectionName
        year.text=musicTrack.releaseDate.substringBefore('-')
        zhanr.text=musicTrack.primaryGenreName
       country.text=musicTrack.country
    }

}