package com.csc780fall21.soulnareapplication.view.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csc780fall21.soulnareapplication.domain.model.Response
import com.csc780fall21.soulnareapplication.domain.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(val usersRepository: UsersRepository) : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    val usersStateFlow = MutableStateFlow<Response?>(null)

    init {
        viewModelScope.launch {
            usersRepository.getUserProfiles(auth.currentUser?.uid).collect {
                usersStateFlow.value = it
            }
        }
    }
}