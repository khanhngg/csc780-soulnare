package com.csc780fall21.soulnareapplication.view.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.utils.Market
import com.csc780fall21.soulnareapplication.data.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class EditProfileViewModel(val usersRepository: UsersRepository) :ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    // Genres
    private val _genreQuery = MutableLiveData("")
    val genreQuery: LiveData<String> = _genreQuery

    private var _genreSearchResults = MutableLiveData(listOf<String>())
    val genreSearchResults: LiveData<List<String>> = _genreSearchResults

    // Artists
    private val _artistQuery = MutableLiveData("")
    val artistQuery: LiveData<String> = _artistQuery

    private var _artistSearchResults = MutableLiveData(mapOf<String, String>())
    val artistSearchResults: LiveData<Map<String, String>> = _artistSearchResults

    // Songs
    private val _songQuery = MutableLiveData("")
    val songQuery: LiveData<String> = _songQuery

    private var _songSearchResults = MutableLiveData(mapOf<String, Map<String, String>>())
    val songSearchResults: LiveData<Map<String, Map<String, String>>> = _songSearchResults

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

            if (result.total > 0) {
                val artists = mutableMapOf<String, String>()
                result.forEach {
                    artists[it!!.name] = it.images[0].url
                }
                _artistSearchResults.value = artists
            } else {
                // TODO - test
                _artistSearchResults.value = mapOf("result" to "No results found")
            }
        }
    }

    fun searchSongs(query: String) {
        viewModelScope.launch {
            val api = spotifyAppApi(CLIENT_ID, CLIENT_SECRET).build()

            val result = api.search.searchTrack(
                query = query,
                limit = 2,
                market = Market.US
            )

            if (result.total > 0) {
                val songs = mutableMapOf<String, Map<String, String>>()
                result.forEach { song ->
                    val artistNames = mutableListOf<String>()
                    song!!.artists.forEach {
                        artistNames.add(it.name)
                    }

                    songs[song.uri.uri] = mapOf(
                        "uri" to song.uri.uri,
                        "name" to song.name,
                        "artists" to artistNames.joinToString(),
                        "imageUrl" to song.album.images[0].url,
                    )
                }
                _songSearchResults.value = songs
            } else {
                // TODO - test
//                _songSearchResults.value = mutableListOf("No results found")
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

        /**
         * References: https://github.com/googlecodelabs/android-compose-codelabs/blob/main/StateCodelab/start/src/main/java/com/codelabs/state/todo/TodoViewModel.kt
         */
        _artistSearchResults.value = _artistSearchResults.value!!.toMutableMap().also {
            it.remove(artist)
        }
    }

    fun addSong(uri: String) {
        val userUid = auth.currentUser?.uid
        val name = songSearchResults.value?.get(uri)?.get("name")
        val artists = songSearchResults.value?.get(uri)?.get("artists")
        val imgUrl = songSearchResults.value?.get(uri)?.get("imageUrl")

        usersRepository.addUserSong(userUid = userUid, newSongUri = uri, newSongName = name!!, newSongImageUrl = imgUrl!!, newSongArtists = artists!!)

        /**
         * References: https://github.com/googlecodelabs/android-compose-codelabs/blob/main/StateCodelab/start/src/main/java/com/codelabs/state/todo/TodoViewModel.kt
         */
        _songSearchResults.value = _songSearchResults.value!!.toMutableMap().also {
            it.remove(uri)
        }
    }

    fun deleteGenre(genre: String) {
        val userUid = auth.currentUser?.uid
        usersRepository.removeUserGenre(userUid = userUid, genre = genre)
    }

    fun deleteArtist(artist: String, imageUrl: String) {
        val userUid = auth.currentUser?.uid
        usersRepository.removeUserArtist(userUid = userUid, artist = artist, imageUrl = imageUrl)
    }

    fun deleteSong(uri: String, imageUrl: String, artists: String, name: String) {
        val userUid = auth.currentUser?.uid
        usersRepository.removeUserSong(userUid = userUid, artists = artists, imageUrl = imageUrl, uri = uri, name = name)
    }

    // Constants
    companion object {
        private const val CLIENT_ID = "94696a0bca7a4cf1bfcd41f4e23fae37"
        private const val CLIENT_SECRET = "3681684773fa43d281fe2cb0ef6af12e"
    }
}