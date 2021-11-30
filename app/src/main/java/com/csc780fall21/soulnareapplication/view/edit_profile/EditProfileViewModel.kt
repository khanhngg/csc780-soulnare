package com.csc780fall21.soulnareapplication.view.edit_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamratzman.spotify.endpoints.pub.SearchApi
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

    suspend fun buildSpotifyAppApi() {
    }

    private val auth: FirebaseAuth = Firebase.auth

    private val _firstName = MutableLiveData("")
    val firstName: LiveData<String> = _firstName

    // TODO - make list?
    // Genres
    private val _genres = MutableLiveData<List<String>>()
    val genres: LiveData<List<String>> = _genres

    private val _genreQuery = MutableLiveData("")
    val genreQuery: LiveData<String> = _genreQuery

    private val _genreSearchResults = MutableLiveData<List<String>>()
    val genreSearchResult: LiveData<List<String>> = _genreSearchResults

    // Artists



    // Songs



    // Search
    fun searchGenre(query: String) {
        viewModelScope.launch {
            val api = spotifyAppApi("94696a0bca7a4cf1bfcd41f4e23fae37", "3681684773fa43d281fe2cb0ef6af12e").build()
            val result = api.search.searchArtist("genre:\"${query}\"", limit = 5, market = Market.US)

            if (result.total > 0) {
                val genres = mutableListOf<String>()
                result.forEach {
                    genres.addAll(it!!.genres)
                }
                _genreSearchResults.value = genres.distinct().sorted()
            } else {
                _genreSearchResults.value = mutableListOf("No results found")
            }
        }
    }

    // Update methods
//    fun updateGenres(newGenres: String) {
//        _genres.value = newGenres
//    }

    fun updateGenreQuery(newQuery: String) {
        _genreQuery.value = newQuery
    }

    // Firestore methods
    fun getGenres() {

    }
}