package com.csc780fall21.soulnareapplication.view.likes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csc780fall21.soulnareapplication.domain.model.Response
import com.csc780fall21.soulnareapplication.domain.model.User
import com.csc780fall21.soulnareapplication.data.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LikesViewModel(val usersRepository: UsersRepository) : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private var _usersToShow = MutableLiveData(listOf<User>())
    val usersToShow: LiveData<List<User>> = _usersToShow

    val usersStateFlow = MutableStateFlow<Response?>(null)

    init {
        viewModelScope.launch {
            usersRepository.getUserProfilesThatLikeYours(auth.currentUser?.uid).collect {
                usersStateFlow.value = it
            }
        }
    }

}