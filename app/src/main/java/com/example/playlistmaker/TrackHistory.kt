package com.example.playlistmaker

import android.util.Log

public object TrackHistory{
    var trackMusicHistory = ArrayList<MusicTrack>()
    fun addTrackHistory(track:MusicTrack) {
        for (trackHistory in trackMusicHistory) {
            Log.d("ADD", "Добавление")
            if (track.trackId === trackHistory.trackId){
                trackMusicHistory.remove(track)
                Log.d("ADD", "такой же")
                break
            }
        }
        trackMusicHistory.add(0, track)
        if (trackMusicHistory.size>10){
            trackMusicHistory.removeAt(10)
        }
    }

    fun clearHistory(){
        trackMusicHistory.clear()

    }
}