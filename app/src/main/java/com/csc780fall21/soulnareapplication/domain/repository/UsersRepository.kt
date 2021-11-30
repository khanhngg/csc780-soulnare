package com.csc780fall21.soulnareapplication.domain.repository

import android.util.Log
import com.csc780fall21.soulnareapplication.domain.model.OnError
import com.csc780fall21.soulnareapplication.domain.model.OnSuccess
import com.csc780fall21.soulnareapplication.domain.model.OnSuccessQuery
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

    fun getUserProfiles(userUid: String?) = callbackFlow {
        val currentUser = firestore.collection("users").document(userUid!!)
        val youLikeUserIds = mutableListOf<Any?>()
        val youRejectUserIds = mutableListOf<Any?>()

        currentUser.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    youLikeUserIds.add(document.data?.get("youLikeUserIds"))
                    youRejectUserIds.add(document.data?.get("youRejectUserIds"))
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        Log.d(TAG, "DocumentSnapshot youLikeUserIds: ${youLikeUserIds}")
        Log.d(TAG, "DocumentSnapshot youRejectUserIds: ${youRejectUserIds}")

        val collection = firestore
            .collection("users")
            .whereNotEqualTo("uid", userUid)
            .whereNotIn("youRejectUserIds", youRejectUserIds)
            .whereNotIn("youLikeUserIds", youLikeUserIds)

        val snapshotListener = collection.addSnapshotListener { snapshot, error ->
            val response = if (error == null) {
                OnSuccessQuery(snapshot)
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

    fun removeUserSong(userUid: String?, uri: String, imageUrl: String, artists: String, name: String) {
        val collection = firestore.collection("users")
        userUid.let {
            if (it != null) {
                collection.document(it).update(
                    "songs",
                    FieldValue.arrayRemove(mapOf(
                        "name" to name,
                        "imageUrl" to imageUrl,
                        "artists" to artists,
                        "uri" to uri))
                )
            }
        }
    }

    fun addUserToLikes(userUid: String?, otherUserUid: String?) {
        val collection = firestore.collection("users")
        userUid.let {
            if (it != null) {
                collection.document(it).update("youLikeUserIds", FieldValue.arrayUnion(otherUserUid))
            }
        }
    }

    fun addUserToReject(userUid: String?, otherUserUid: String?) {
        // TODO - edge case -- also need to remove from previously added to youLikeUserIds
        val collection = firestore.collection("users")
        userUid.let {
            if (it != null) {
                collection.document(it).update("youRejectUserIds", FieldValue.arrayUnion(otherUserUid))
            }
        }
    }

    companion object {
        const val TAG = "UsersRepository"
    }
}