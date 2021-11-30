package com.csc780fall21.soulnareapplication.view.edit_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.utils.Market
import com.csc780fall21.soulnareapplication.domain.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class EditProfileViewModel(val usersRepository: UsersRepository) :ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    // Genres
    private val _genres = MutableLiveData<List<String>>()
    val genres: LiveData<List<String>> = _genres

    private val _genreQuery = MutableLiveData("")
    val genreQuery: LiveData<String> = _genreQuery

    private var _genreSearchResults = MutableLiveData(listOf<String>())
    val genreSearchResults: LiveData<List<String>> = _genreSearchResults

    // Artists
    private val _artistQuery = MutableLiveData("")
    val artistQuery: LiveData<String> = _artistQuery

    private var _artistSearchResults = MutableLiveData(mapOf<String, String>())
    val artistSearchResults: LiveData<Map<String, String>> = _artistSearchResults

//    private var _artistSearchResults = MutableLiveData(listOf<String>())
//    val artistSearchResults: LiveData<List<String>> = _artistSearchResults
//
    // Songs
    private val _songQuery = MutableLiveData("")
    val songQuery: LiveData<String> = _songQuery

    private var _songSearchResults = MutableLiveData(listOf<String>())
    val songSearchResults: LiveData<List<String>> = _songSearchResults

    // Search
    fun searchGenres(query: String) {
        viewModelScope.launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()

            val result = api.search.searchArtist(
                query = "genre:\"${query}\"",
                limit = 5,
                market = Market.US
            )

            if (result.total > 0) {
                val genres = mutableListOf<String>()
                result.forEach {
                    genres.addAll(it!!.genres)
                }
                _genreSearchResults.value = genres.distinct().sorted().toMutableList()
            } else {
                _genreSearchResults.value = mutableListOf("No results found")
            }
        }
    }

    fun searchArtists(query: String) {
        viewModelScope.launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()

            val result = api.search.searchArtist(
                query = query,
                limit = 5,
                market = Market.US
            )
            Log.i("searchArtist....", "result: $result")
            Log.i("searchArtist....image...", "result: ${result[0].images[0].url}")

            if (result.total > 0) {
                val artists = mutableMapOf<String, String>()
                result.forEach {
                    artists[it!!.name] = it.images[0].url
                }
                _artistSearchResults.value = artists
            } else {
                _artistSearchResults.value = mapOf("result" to "No results found")
            }
        }
    }

    fun searchSongs(query: String) {
        viewModelScope.launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()

            val result = api.search.searchArtist(
                query = "genre:\"${query}\"",
                limit = 5,
                market = Market.US
            )

            if (result.total > 0) {
                val genres = mutableListOf<String>()
                result.forEach {
                    genres.addAll(it!!.genres)
                }
                _genreSearchResults.value = genres.distinct().sorted().toMutableList()
            } else {
                _genreSearchResults.value = mutableListOf("No results found")
            }
        }
    }

    // Update search query
    fun updateGenreQuery(newQuery: String) {
        _genreQuery.value = newQuery
    }

    fun updateArtistQuery(newQuery: String) {
        _artistQuery.value = newQuery
    }

    fun updateSongQuery(newQuery: String) {
        _songQuery.value = newQuery
    }

    // Call Firestore to update fields
    fun addGenre(genre: String) {
        val userUid = auth.currentUser?.uid
        usersRepository.addUserGenre(userUid = userUid, newGenre = genre)

        /**
         * References: https://github.com/googlecodelabs/android-compose-codelabs/blob/main/StateCodelab/start/src/main/java/com/codelabs/state/todo/TodoViewModel.kt
         */
        _genreSearchResults.value = _genreSearchResults.value!!.toMutableList().also {
            it.remove(genre)
        }
    }

    fun addArtist(artist: String) {
        val userUid = auth.currentUser?.uid
        val imgUrl = artistSearchResults.value?.get(artist)
        usersRepository.addUserArtist(userUid = userUid, newArtist = artist, artistImageUrl = imgUrl)
        Log.i("addartist...", "artist=${artist}, img = ${imgUrl}")

        /**
         * References: https://github.com/googlecodelabs/android-compose-codelabs/blob/main/StateCodelab/start/src/main/java/com/codelabs/state/todo/TodoViewModel.kt
         */
        _artistSearchResults.value = _artistSearchResults.value!!.toMutableMap().also {
            it.remove(artist)
        }
    }

    fun addSong(genre: String) {
        val userUid = auth.currentUser?.uid
        usersRepository.addUserGenre(userUid = userUid, newGenre = genre)

        /**
         * References: https://github.com/googlecodelabs/android-compose-codelabs/blob/main/StateCodelab/start/src/main/java/com/codelabs/state/todo/TodoViewModel.kt
         */
        _genreSearchResults.value = _genreSearchResults.value!!.toMutableList().also {
            it.remove(genre)
        }
    }

    fun deleteGenre(genre: String) {
        val userUid = auth.currentUser?.uid
        usersRepository.removeUserGenre(userUid = userUid, genre = genre)
    }

    // Constants
    companion object {
        private const val CLIENT_ID = "94696a0bca7a4cf1bfcd41f4e23fae37"
        private const val CLIENT_SECRET = "3681684773fa43d281fe2cb0ef6af12e"
    }
}