package com.csc780fall21.soulnareapplication.view.profile

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

/**
 * References: https://medium.com/firebase-developers/firebase-with-jetpack-compose-cloud-firestore-39d8d139536a
 */
@ExperimentalCoroutinesApi
class ProfileViewModel(val usersRepository: UsersRepository) : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    val userStateFlow = MutableStateFlow<Response?>(null)

    init {
        viewModelScope.launch {
            usersRepository.getUserProfile(auth.currentUser?.uid).collect {
                userStateFlow.value = it
            }
        }
    }
}