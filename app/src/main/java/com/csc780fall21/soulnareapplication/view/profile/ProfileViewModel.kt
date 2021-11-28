package com.csc780fall21.soulnareapplication.view.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csc780fall21.soulnareapplication.domain.model.Response
import com.csc780fall21.soulnareapplication.domain.repository.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * References: https://medium.com/firebase-developers/firebase-with-jetpack-compose-cloud-firestore-39d8d139536a
 */
@ExperimentalCoroutinesApi
class ProfileViewModel(val usersRepository: UsersRepository) : ViewModel() {

    val userStateFlow = MutableStateFlow<Response?>(null)

    init {
        viewModelScope.launch {
            usersRepository.getUserProfile().collect {
                userStateFlow.value = it
            }
        }
    }

    fun getUserProfile() = usersRepository.getUserProfile()
}