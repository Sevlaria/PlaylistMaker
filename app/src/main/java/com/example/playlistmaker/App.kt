package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson

class App (): Application() {
    val THEME_PREFERENCES= "theme_preferences_now"
    val HISTORY = "history_searching"
    val THEME = "theme_now"
    var darkTheme = false
    lateinit var sharedPrefs: SharedPreferences
    lateinit var themeSwitcher: Switch
    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(THEME, false))
        setHistory()

    }
    fun switchTheme (darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPrefs.edit()
            .putBoolean(THEME, darkTheme)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if(darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun setHistory(){
        val tracksHistoryString = sharedPrefs.getString(HISTORY, null)
        if(tracksHistoryString != null){
            TrackHistory.trackMusicHistory = createHistoryFromJson(tracksHistoryString)
        }
    }
    private fun createHistoryFromJson(json:String):ArrayList<MusicTrack>{
        val tracks = Gson().fromJson(json, Array<MusicTrack>::class.java)
        val array = ArrayList<MusicTrack>()
        array.addAll(tracks)
        return array
    }

}