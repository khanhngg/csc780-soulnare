package com.csc780fall21.soulnareapplication.view.edit_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csc780fall21.soulnareapplication.domain.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class EditProfileViewModel(val usersRepository: UsersRepository) :ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _firstName = MutableLiveData("")
    val firstName: LiveData<String> = _firstName

    // TODO - make list?
    // Genres
    private val _genres = MutableLiveData("")
    val genres: LiveData<String> = _genres

    private val _genreQuery = MutableLiveData("")
    val genreQuery: LiveData<String> = _genreQuery

    private val _genreSearchResults = MutableLiveData<List<String>>()
    val genreSearchResult: LiveData<List<String>> = _genreSearchResults

    // Artists



    // Songs

    // Search
    fun searchGenre(query: String) {
        Log.i("searchGenre", "query is: $query")
    }

    // Update methods
    fun updateGenres(newGenres: String) {
        _genres.value = newGenres
    }

    fun updateGenreQuery(newQuery: String) {
        _genreQuery.value = newQuery
    }

    // Firestore methods
    fun getGenres() {

    }
}