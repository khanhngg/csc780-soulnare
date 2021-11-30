package com.csc780fall21.soulnareapplication.view.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditProfileViewModel :ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _firstName = MutableLiveData("")
    val firstName: LiveData<String> = _firstName

    // TODO - make list?
    private val _genres = MutableLiveData("")
    val genres: LiveData<String> = _genres

    fun updateGenres(newGenres: String) {
        _genres.value = newGenres
    }

}