package com.csc780fall21.soulnareapplication.data.services

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

/**
 * References:
 * - https://developer.spotify.com/documentation/android/quick-start/
 * - https://tolkiana.com/how-to-use-spotifys-sdk-in-kotlin/
 */
enum class PlayingState {
    PAUSED, PLAYING, STOPPED
}

/**
 * Singleton class to play music from Spotify
 */
object SpotifyService {
    private const val CLIENT_ID = "94696a0bca7a4cf1bfcd41f4e23fae37"
    private const val  REDIRECT_URI = "com.csc780fall21.soulnareapplication://callback"

    private const val TAG = "SpotifyService"

    private var spotifyAppRemote: SpotifyAppRemote? = null

    /**
     * Create and initialize the connection parameters
     */
    private var connectionParams: ConnectionParams = ConnectionParams.Builder(CLIENT_ID)
        .setRedirectUri(REDIRECT_URI)
        .showAuthView(true)
        .build()

    /**
     * Establish the connection to SpotifyAppRemote
     */
    fun connect(context: Context, handler: (connected: Boolean) -> Unit) {
        if (spotifyAppRemote?.isConnected == true) {
            handler(true)
            return
        }

        val connectionListener = object : Connector.ConnectionListener {
            override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                this@SpotifyService.spotifyAppRemote = spotifyAppRemote
                handler(true)
            }
            override fun onFailure(throwable: Throwable) {
                Log.e(TAG, throwable.message, throwable)
                handler(false)
            }
        }

        SpotifyAppRemote.connect(context, connectionParams, connectionListener)
    }

    /**
     * Disconnect from SpotifyAppRemote
     */
    fun disconnect() {
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    /**
     * Music player controls
     */
    fun play(uri: String) {
        spotifyAppRemote?.playerApi?.play(uri)
    }

    fun resume() {
        spotifyAppRemote?.playerApi?.resume()
    }

    fun pause() {
        spotifyAppRemote?.playerApi?.pause()
    }

    fun playingState(handler: (PlayingState) -> Unit) {
        spotifyAppRemote?.playerApi?.playerState?.setResultCallback { result ->
            if (result.track.uri == null) {
                handler(PlayingState.STOPPED)
            } else if (result.isPaused) {
                handler(PlayingState.PAUSED)
            } else {
                handler(PlayingState.PLAYING)
            }
        }
    }

}
