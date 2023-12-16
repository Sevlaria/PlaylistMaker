package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources.Theme
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R.*
import com.example.playlistmaker.R.integer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*
import java.util.prefs.Preferences
import kotlin.collections.ArrayList
import com.example.playlistmaker.R.integer.size_8 as size_8



class SearchingActivity: AppCompatActivity() {
    private var searchText: String = ""
    private val musicBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(musicBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val musicService = retrofit.create(MusicApi::class.java)
    private var tracks: ArrayList<MusicTrack> = ArrayList()
    private val trackAdapter = TrackAdapter()

     lateinit var inputEditText: EditText
    lateinit var buttonX: ImageView
    lateinit var backToMain: ImageView
    lateinit var recyclerViewTrack: RecyclerView
    lateinit var placeholder: ImageView
    lateinit var placeholderText: TextView
    lateinit var buttonUpdate: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_searching)
        inputEditText = findViewById(id.input_search)
        buttonX = findViewById(id.button_x)
        inputEditText.requestFocus()
        inputEditText.setText(searchText)
        backToMain = findViewById(id.back_main2)
        recyclerViewTrack = findViewById(id.recycler_view)
        placeholder = findViewById(R.id.placeholder)
        placeholderText = findViewById(id.placeholder_text)
        trackAdapter.tracks_music = tracks
        buttonUpdate = findViewById(id.button_update)
       /* val music_composition_list: List<Track> = listOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        ) */
        backToMain.setOnClickListener {
            super.finish()
        }
        val simpleTextWatcher = object : TextWatcher {
            @SuppressLint("SuspiciousIndentation")
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty())
                    buttonX.visibility = ImageView.INVISIBLE

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonX.visibility = ImageView.VISIBLE
                searchText = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.isNullOrEmpty())
                    buttonX.visibility = ImageView.INVISIBLE
                else {
                    buttonX.visibility = ImageView.VISIBLE

                }
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        buttonX.setOnClickListener {
            inputEditText.text.clear()
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE)
                    as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(buttonX.windowToken, 0)
        }
        recyclerViewTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        recyclerViewTrack.adapter=trackAdapter
        var currentNightMode = findViewById<FrameLayout>(R.id.frame_background)
        var colorBackground = getColor(color.YPBlack)

         fun showMessage (text: String, additionalText:String) =
             if ((text.isNotEmpty()) and (additionalText ==="0")){
                 placeholder.visibility = View.VISIBLE
                 placeholderText.visibility=View.VISIBLE
                 tracks.clear()
                 trackAdapter.notifyDataSetChanged()
                 if (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)=== 32) {
                 placeholder.setImageDrawable(getDrawable(drawable.no_search_dark))}
                 else {placeholder.setImageDrawable(getDrawable(drawable.no_search))}

                 placeholderText.text = text
             } else if (text.isEmpty()){
                 buttonUpdate.visibility = View.INVISIBLE
                 placeholder.visibility=View.INVISIBLE
                 placeholderText.text = text
             }else {
                 placeholder.visibility = View.VISIBLE
                 recyclerViewTrack.visibility = View.INVISIBLE
                 placeholderText.visibility=View.VISIBLE
                 if(this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)=== 32){
                 placeholder.setImageDrawable(getDrawable(drawable.no_internet_dark))}
                 else {
                     placeholder.setImageDrawable(getDrawable(drawable.no_internet))
                 }
                 placeholderText.text = text
                 buttonUpdate.visibility = View.VISIBLE

             }


        fun search() {
            musicService.searchMusic(inputEditText.text.toString())
                .enqueue(object: Callback<MusicResponse> {
                    override fun onResponse(
                        call: Call<MusicResponse>,
                        response: Response<MusicResponse>
                    ) {
                        Log.d("TRANSLATION_LOG", "text: ${response.body()?.resultCount.toString()}")
                        Log.d("CODE", response.code().toString())
                        tracks.clear()
                        when (response.code()){
                            200 -> if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                showMessage("", "0")

                            } else {
                            showMessage(getString(string.no_search), "0")

                            }
                            else -> {showMessage(getString(R.string.no_internet),
                                "1")}
                        }

                        trackAdapter.notifyDataSetChanged()

                        }


                    override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                       Log.d("NO INTERNET", "no int")

                        showMessage(getString(R.string.no_internet),
                        "1")

                    }
                })
        }
        buttonUpdate.setOnClickListener{
            if(tracks.isNotEmpty()){
                buttonUpdate.visibility = View.INVISIBLE
                recyclerViewTrack.visibility = View.VISIBLE
                trackAdapter.tracks_music = tracks
                trackAdapter.notifyDataSetChanged()
                placeholder.visibility = View.INVISIBLE
                placeholderText.visibility = View.INVISIBLE}
            else {
                recyclerViewTrack.visibility = View.VISIBLE
                search()
            }

        }

        inputEditText.setOnEditorActionListener{_, actionId,_ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    search()
                    true
                }

            }
            false
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, searchText)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(EDIT_TEXT, "")
        }
    }




    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
    }
}

class TrackViewHolder(var parentView:View):RecyclerView.ViewHolder(parentView){
         var trackNameView: TextView
         var artistNameView: TextView
        var trackTimeView: TextView
        var artImageView: ImageView
        init{
            trackNameView = parentView.findViewById(id.track_name)
            artistNameView = parentView.findViewById(id.artist_name)
            trackTimeView = parentView.findViewById(id.time_track)
            artImageView = parentView.findViewById(id.image_track)
        }
        fun bind (model: MusicTrack) {
            trackNameView.text = model.trackName
            artistNameView.text = model.artistName
            trackTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(model.trackTimeMillis).toString()
            val radius:Int = parentView.resources.getInteger(R.integer.size_8)
            Glide.with(parentView)
                 .load(model.artworkUrl100)
                 .placeholder(drawable.placeholder)
                 .centerCrop()
                 .transform(RoundedCorners(radius))
                 .into(artImageView)
        }
    }

  class TrackAdapter():RecyclerView.Adapter<TrackViewHolder>(){
      var tracks_music = ArrayList<MusicTrack>()
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
         var view = LayoutInflater.from(parent.context).inflate(layout.track,parent,false)
         return TrackViewHolder(view)
     }

     override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
         holder.bind(tracks_music[position])
     }

     override fun getItemCount(): Int {
         return tracks_music.size
     }
 }
 data class MusicTrack (
     val trackName:String,
     val artistName:String,
      val trackTimeMillis: Int,
     val artworkUrl100: String
 )

class MusicResponse (var resultCount: Int, var results: ArrayList<MusicTrack>)

interface MusicApi {
    @GET("/search?entity=song")
    fun searchMusic(@Query("term") text: String): Call<MusicResponse>

}


