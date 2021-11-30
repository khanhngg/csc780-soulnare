package com.csc780fall21.soulnareapplication.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csc780fall21.soulnareapplication.domain.model.User
import com.csc780fall21.soulnareapplication.domain.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * References: https://github.com/pradyotprksh/development_learning/tree/main/jetpack_compose/FlashChat
 */
@ExperimentalCoroutinesApi
class RegisterViewModel(val usersRepository: UsersRepository) : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _firstName = MutableLiveData("")
    val firstName: LiveData<String> = _firstName

    private val _lastName = MutableLiveData("")
    val lastName: LiveData<String> = _lastName

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    // Update first name
    fun updateFirstName(newFirstName: String) {
        _firstName.value = newFirstName
    }

    // Update last name
    fun updateLastName(newLastName: String) {
        _lastName.value = newLastName
    }

    // Update email
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    // Update password
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    // Register
    @ExperimentalCoroutinesApi
    fun registerUser(home: () -> Unit) {
        if (_loading.value == false) {
            val email: String = _email.value ?: throw IllegalArgumentException("email expected")
            val password: String =
                _password.value ?: throw IllegalArgumentException("password expected")

            _loading.value = true

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        home()
                    }
                    _loading.value = false

                    // TODO - create user to firestore
                    val uid = auth.currentUser?.uid
                    val user = User(uid = uid, firstName = _firstName.value, lastName = _lastName.value, email = email)
                    usersRepository.createUserProfile(user = user)
                }
        }
    }
}