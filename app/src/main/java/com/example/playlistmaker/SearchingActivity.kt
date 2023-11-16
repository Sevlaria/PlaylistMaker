package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.security.PrivateKey


class SearchingActivity: AppCompatActivity() {
    private var searchText: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching)
        val inputEditText = findViewById<EditText>(R.id.input_search)
        val buttonX = findViewById<ImageView>(R.id.button_x)
        inputEditText.requestFocus()
        inputEditText.setText(searchText)
        val backToMain = findViewById<ImageView>(R.id.back_main2)
        val recyclerViewTrack: RecyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val music_composition_list: List<Track> = listOf(
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
        )
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
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE)
                    as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(buttonX.windowToken, 0)
        }
        recyclerViewTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        val TrackAdapter: TrackAdapter = TrackAdapter(music_composition_list)
        recyclerViewTrack.adapter=TrackAdapter

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
    class TrackViewHolder(val parentView:View):RecyclerView.ViewHolder(parentView){
        private val trackNameView: TextView
        private val artistNameView: TextView
        private val trackTimeView: TextView
        private val artImageView: ImageView
        init{
            trackNameView = parentView.findViewById(R.id.track_name)
            artistNameView = parentView.findViewById(R.id.artist_name)
            trackTimeView = parentView.findViewById(R.id.time_track)
            artImageView = parentView.findViewById(R.id.image_track)
        }
        fun bind (model: Track) {
            trackNameView.text = model.trackName
            artistNameView.text = model.artistName
            trackTimeView.text = model.trackTime
            Glide.with(parentView)
                 .load(model.artworkUrl100)
                 .placeholder(R.drawable.placeholder)
                 .centerCrop()
                 .into(artImageView)
        }
    }

  class TrackAdapter(private val tracks: List<Track>):RecyclerView.Adapter<TrackViewHolder>(){
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.track,parent,false)
         return TrackViewHolder(view)
     }

     override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
         holder.bind(tracks[position])
     }

     override fun getItemCount(): Int {
         return tracks.size
     }
 }