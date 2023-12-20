package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R.*
import com.example.playlistmaker.R.integer.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import kotlin.collections.ArrayList


class SearchingActivity: AppCompatActivity(), TrackAdapter.HistoryClickListener {
    // объявление переменных
    private var searchText: String = ""
    private val musicBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(musicBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val musicService = retrofit.create(MusicApi::class.java)
    private var tracks: ArrayList<MusicTrack> = ArrayList()
    private var tracksHistory: ArrayList<MusicTrack> = ArrayList()
    private val trackAdapter = TrackAdapter(this)

    private val trackAdapterHistory = TrackAdapterHistory(this)
    lateinit var inputEditText: EditText
    lateinit var buttonX: ImageView
    lateinit var backToMain: ImageView
    lateinit var recyclerViewTrack: RecyclerView
    lateinit var placeholder: ImageView
    lateinit var placeholderText: TextView
    lateinit var buttonUpdate: Button
    lateinit var buttonCleanSearching: Button
    lateinit var textHistorySearching: TextView
    lateinit var recyclerViewHistory: RecyclerView
    private val THEME_PREFERENCES = "theme_preferences_now"
    private val HISTORY = "history_searching"
    lateinit var sharedPrefs: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //инициализация переменных
        setContentView(layout.activity_searching)
        inputEditText = findViewById(id.input_search)
        buttonX = findViewById(id.button_x)
        inputEditText.requestFocus()
        inputEditText.setText(searchText)
        backToMain = findViewById(id.back_main2)
        recyclerViewTrack = findViewById(id.recycler_view)
        placeholder = findViewById(R.id.placeholder)
        placeholderText = findViewById(id.placeholder_text)
        buttonCleanSearching = findViewById(id.clear_searching)
        textHistorySearching = findViewById(id.text_history_searching)
        trackAdapter.tracks_music = tracks
        trackAdapterHistory.tracksMusicHistory = TrackHistory.trackMusicHistory
        buttonUpdate = findViewById(id.button_update)
        recyclerViewHistory = findViewById(id.recycler_view_history)
        textHistorySearching.visibility = View.INVISIBLE
        inputEditText.clearFocus() //ощищаем фокус

        sharedPrefs = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)

        val tracksHistoryString = sharedPrefs.getString(HISTORY, null)
        if (tracksHistoryString != null) {
            TrackHistory.trackMusicHistory = createHistoryFromJson(tracksHistoryString)
            trackAdapterHistory.tracksMusicHistory = TrackHistory.trackMusicHistory
            trackAdapterHistory.notifyDataSetChanged()
        }


        //устанавливаем слушатель фокуса на поле ввода
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            textHistorySearching.visibility = if (hasFocus && inputEditText.text.isEmpty())
                View.VISIBLE else View.GONE
            buttonCleanSearching.visibility = if (hasFocus && inputEditText.text.isEmpty())
                View.VISIBLE else View.GONE
            recyclerViewHistory.visibility = if (hasFocus && inputEditText.text.isEmpty())
                View.VISIBLE else View.GONE
        }
        //устанавливаем фокус в поле ввода
        inputEditText.requestFocus()
        backToMain.setOnClickListener {
            super.finish()
        }
        // слушатель изменения работы с тестовым полем
        val simpleTextWatcher = object : TextWatcher {
            @SuppressLint("SuspiciousIndentation")
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty())
                    buttonX.visibility = ImageView.INVISIBLE
                if (inputEditText.hasFocus()) {
                    textHistorySearching.visibility = View.VISIBLE
                }

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonX.visibility = ImageView.VISIBLE
                searchText = p0.toString()
                if (inputEditText.hasFocus() && p0?.isEmpty() == true) {
                    textHistorySearching.visibility = View.VISIBLE
                    buttonCleanSearching.visibility = View.VISIBLE
                    recyclerViewHistory.visibility = View.VISIBLE
                } else {
                    textHistorySearching.visibility = View.INVISIBLE
                    buttonCleanSearching.visibility = View.INVISIBLE
                    recyclerViewHistory.visibility = View.INVISIBLE
                }

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

        //обработка нажатия на кнопку сброса текста
        buttonX.setOnClickListener {
            inputEditText.text.clear()
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            placeholderText.visibility = View.INVISIBLE
            placeholder.visibility = View.INVISIBLE
            buttonUpdate.visibility = View.INVISIBLE
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE)
                    as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(buttonX.windowToken, 0)
        }

        //установка менеджера
        recyclerViewTrack.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //установка адаптера
        recyclerViewTrack.adapter = trackAdapter
        recyclerViewHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewHistory.adapter = trackAdapterHistory
        //показ заглушек
        fun showMessage(text: String, additionalText: String) =
            if ((text.isNotEmpty()) and (additionalText === "0")) {
                recyclerViewTrack.visibility = View.VISIBLE
                placeholder.visibility = View.VISIBLE
                placeholderText.visibility = View.VISIBLE
                tracks.clear()
                trackAdapter.notifyDataSetChanged()
                if (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) === 32) {
                    placeholder.setImageDrawable(getDrawable(drawable.no_search_dark))
                } else {
                    placeholder.setImageDrawable(getDrawable(drawable.no_search))
                }

                placeholderText.text = text
            } else if (text.isEmpty()) {
                buttonUpdate.visibility = View.INVISIBLE
                placeholder.visibility = View.INVISIBLE
                placeholderText.text = text
            } else {
                placeholder.visibility = View.VISIBLE
                recyclerViewTrack.visibility = View.INVISIBLE
                placeholderText.visibility = View.VISIBLE
                if (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) === 32) {
                    placeholder.setImageDrawable(getDrawable(drawable.no_internet_dark))
                } else {
                    placeholder.setImageDrawable(getDrawable(drawable.no_internet))
                }
                placeholderText.text = text
                buttonUpdate.visibility = View.VISIBLE
            }



        //поиск треков
        fun search() {
            musicService.searchMusic(inputEditText.text.toString())
                .enqueue(object : Callback<MusicResponse> {
                    override fun onResponse(
                        call: Call<MusicResponse>,
                        response: Response<MusicResponse>
                    ) {
                        Log.d("TRANSLATION_LOG", "text: ${response.body()?.resultCount.toString()}")
                        Log.d("CODE", response.code().toString())
                        tracks.clear()
                        recyclerViewTrack.visibility = View.VISIBLE
                        when (response.code()) {
                            200 -> if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                showMessage("", "0")

                            } else {
                                showMessage(getString(string.no_search), "0")

                            }
                            else -> {
                                Log.d("TRANSLATION_LOG", "text: ${response.body()?.resultCount.toString()}")
                                Log.d("CODE", response.code().toString())
                                showMessage(

                                    getString(R.string.no_internet),
                                    "1"
                                )
                            }
                        }

                        trackAdapter.notifyDataSetChanged()

                    }


                    override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                        Log.d("NO INTERNET", "no int")

                        showMessage(
                            getString(R.string.no_internet),
                            "1"
                        )

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

        //обработка нажатия на кнопку обновить
        buttonUpdate.setOnClickListener {
            if (tracks.isNotEmpty()) {
                buttonUpdate.visibility = View.INVISIBLE
                recyclerViewTrack.visibility = View.VISIBLE
                trackAdapter.tracks_music = tracks
                trackAdapter.notifyDataSetChanged()
                placeholder.visibility = View.INVISIBLE
                placeholderText.visibility = View.INVISIBLE
            } else {
                recyclerViewTrack.visibility = View.VISIBLE
                search()
            }

        }

        buttonCleanSearching.setOnClickListener {
            TrackHistory.clearHistory()
            recyclerViewHistory.visibility=View.INVISIBLE
            buttonCleanSearching.visibility=View.INVISIBLE
            textHistorySearching.visibility=View.INVISIBLE
            trackAdapterHistory.notifyDataSetChanged()

        }

        //обработка нажатия на клавиатуре
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    search()
                    true
                }

            }
            false
        }

    }
    override fun onHistoryClick(track: MusicTrack) {
        TrackHistory.addTrackHistory(track)
        trackAdapterHistory.notifyDataSetChanged()
       val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra("track", createJsonFromTrack(track))
        startActivity(intent)

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

    override fun onStop() {
        sharedPrefs.edit()
            .putString(
                HISTORY,
                createJsonFromHistory(trackAdapterHistory.tracksMusicHistory)
            )
            .apply()
        super.onStop()
    }
    private fun createJsonFromHistory(tracks: ArrayList<MusicTrack>): String {
        return Gson().toJson(tracks)
    }
    private fun createJsonFromTrack(track: MusicTrack): String {
        return Gson().toJson(track)
    }

    private fun createHistoryFromJson(json:String):ArrayList<MusicTrack>{
        val tracks = Gson().fromJson(json, Array<MusicTrack>::class.java)
        val array = ArrayList<MusicTrack>()
        array.addAll(tracks)
        return array
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

  class TrackAdapter(private val clickListener: HistoryClickListener):RecyclerView.Adapter<TrackViewHolder>(){
      var tracks_music = ArrayList<MusicTrack>()

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
         var view = LayoutInflater.from(parent.context).inflate(layout.track,parent,false)
         return TrackViewHolder(view)
     }


     override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
         holder.bind(tracks_music[position])
         holder.itemView.setOnClickListener {
             clickListener.onHistoryClick(tracks_music[position])


         }
     }

      override fun getItemCount(): Int {
         return tracks_music.size
     }
      fun interface HistoryClickListener {
          fun onHistoryClick(track:MusicTrack)

      }


 }

  class TrackAdapterHistory(private val clickListener: TrackAdapter.HistoryClickListener):RecyclerView.Adapter<TrackViewHolder>(){
    var tracksMusicHistory = ArrayList<MusicTrack>()
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
          var view = LayoutInflater.from(parent.context).inflate(layout.track,parent,false)
          return TrackViewHolder(view)
      }

      override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
          holder.bind(tracksMusicHistory[position])
         holder.itemView.setOnClickListener{
          clickListener.onHistoryClick(tracksMusicHistory[position])
         }


      }

      override fun getItemCount(): Int {
         return tracksMusicHistory.size
      }
}
 data class MusicTrack (
     val trackId: Int,
     val trackName:String,
     val artistName:String,
      val trackTimeMillis: Int,
     val artworkUrl100: String,
     val collectionName:String,
     val releaseDate: String,
     val primaryGenreName: String,
     val country: String

 ){

     fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

 }

class MusicResponse (var resultCount: Int, var results: ArrayList<MusicTrack>)

interface MusicApi {
    @GET("/search?entity=song")
    fun searchMusic(@Query("term") text: String): Call<MusicResponse>

}


