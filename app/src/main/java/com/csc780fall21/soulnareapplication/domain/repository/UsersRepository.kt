package com.csc780fall21.soulnareapplication.domain.repository

import com.csc780fall21.soulnareapplication.domain.model.OnError
import com.csc780fall21.soulnareapplication.domain.model.OnSuccess
import com.csc780fall21.soulnareapplication.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * References: https://medium.com/firebase-developers/firebase-with-jetpack-compose-cloud-firestore-39d8d139536a
 */
@ExperimentalCoroutinesApi
class UsersRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getUserProfile() = callbackFlow {
        val collection = firestore.collection("users")
        val snapshotListener = collection.addSnapshotListener { snapshot, error ->
            val response = if (snapshot != null) {
                OnSuccess(snapshot)
            } else {
                OnError(error)
            }

            trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    fun createUserProfile(user: User) {
        val collection = firestore.collection("users")
        user.uid?.let { collection.document(it).set(user) }
    }
}