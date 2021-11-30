package com.csc780fall21.soulnareapplication.view.edit_profile

import android.util.Log
import androidx.compose.runtime.toMutableStateList
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

    private val _firstName = MutableLiveData("")
    val firstName: LiveData<String> = _firstName

    // Genres
    private val _genres = MutableLiveData<List<String>>()
    val genres: LiveData<List<String>> = _genres

    private val _genreQuery = MutableLiveData("")
    val genreQuery: LiveData<String> = _genreQuery

    private var _genreSearchResults = MutableLiveData(listOf<String>())
    val genreSearchResult: LiveData<List<String>> = _genreSearchResults

//    fun addItem(item: TodoItem) {
//        _todoItems.value = _todoItems.value!! + listOf(item)
//    }

    // Artists



    // Songs



    // Search
    fun searchGenre(query: String) {
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

    // Update methods
//    fun updateGenres(newGenres: String) {
//        _genres.value = newGenres
//    }

    fun updateGenreQuery(newQuery: String) {
        _genreQuery.value = newQuery
    }

    // Firestore methods
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

    fun deleteGenre(genre: String) {
        val userUid = auth.currentUser?.uid
        usersRepository.removeUserGenre(userUid = userUid, genre = genre)
    }

    companion object {
        private const val CLIENT_ID = "94696a0bca7a4cf1bfcd41f4e23fae37"
        private const val CLIENT_SECRET = "3681684773fa43d281fe2cb0ef6af12e"
    }
}