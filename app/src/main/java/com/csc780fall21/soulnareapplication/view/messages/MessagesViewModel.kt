package com.csc780fall21.soulnareapplication.view.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csc780fall21.soulnareapplication.data.repository.MessagesRepository
import com.csc780fall21.soulnareapplication.data.repository.UsersRepository
import com.csc780fall21.soulnareapplication.domain.model.Response
import com.csc780fall21.soulnareapplication.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MessagesViewModel(
    val usersRepository: UsersRepository,
    val messagesRepository: MessagesRepository,
) : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    val usersStateFlow = MutableStateFlow<Response?>(null)

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    /**
     * Update the message value as user types
     */
    fun updateMessage(message: String) {
        _message.value = message
    }

    init {
        viewModelScope.launch {
            usersRepository.getUserProfilesThatLikeYours(auth.currentUser?.uid).collect {
                usersStateFlow.value = it
            }
        }

        getMessages()
    }

    fun getMessages() {

    }

    fun createRoom(otherUserId: String) {
        val roomId = messagesRepository.createRoom()
        usersRepository.addUserToRoom(auth.currentUser?.uid, roomId = roomId)
        usersRepository.addUserToRoom(otherUserId, roomId = roomId)
    }
}