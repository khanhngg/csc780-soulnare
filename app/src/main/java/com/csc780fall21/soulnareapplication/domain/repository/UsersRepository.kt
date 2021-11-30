package com.csc780fall21.soulnareapplication.domain.repository

import com.csc780fall21.soulnareapplication.domain.model.OnError
import com.csc780fall21.soulnareapplication.domain.model.OnSuccess
import com.csc780fall21.soulnareapplication.domain.model.User
import com.google.firebase.firestore.FieldValue
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

    fun getUserProfile(userUid: String?) = callbackFlow {
        val collection = firestore.collection("users").document(userUid!!)
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

    fun addUserGenre(userUid: String?, newGenre: String) {
        val collection = firestore.collection("users")
        userUid.let {
            if (it != null) {
                collection.document(it).update("genres", FieldValue.arrayUnion(newGenre))
            }
        }
    }

    fun removeUserGenre(userUid: String?, genre: String) {
        val collection = firestore.collection("users")
        userUid.let {
            if (it != null) {
                collection.document(it).update("genres", FieldValue.arrayRemove(genre))
            }
        }
    }

    fun addUserArtist(userUid: String?, newArtist: String, artistImageUrl: String?) {
        val collection = firestore.collection("users")
        userUid.let {
            if (it != null) {
                collection.document(it).update(
                    "artists",
                    FieldValue.arrayUnion(mapOf("name" to newArtist, "imageUrl" to artistImageUrl))
                )
            }
        }
    }

    fun removeUserArtist(userUid: String?, artist: String, imageUrl: String) {
        val collection = firestore.collection("users")
        userUid.let {
            if (it != null) {
                collection.document(it).update(
                    "artists",
                    FieldValue.arrayRemove(mapOf("name" to artist, "imageUrl" to imageUrl))
                )
            }
        }
    }

    fun addUserSong(userUid: String?, newSongName: String, newSongUri: String, newSongArtists: String, newSongImageUrl: String) {
        val collection = firestore.collection("users")
        userUid.let {
            if (it != null) {
                collection.document(it).update(
                    "songs",
                    FieldValue.arrayUnion(mapOf(
                        "name" to newSongName,
                        "imageUrl" to newSongImageUrl,
                        "artists" to newSongArtists,
                        "uri" to newSongUri))
                )
            }
        }
    }

    fun removeUserSong(userUid: String?, genre: String) {
        val collection = firestore.collection("users")
        userUid.let {
            if (it != null) {
                collection.document(it).update("genres", FieldValue.arrayRemove(genre))
            }
        }
    }
}