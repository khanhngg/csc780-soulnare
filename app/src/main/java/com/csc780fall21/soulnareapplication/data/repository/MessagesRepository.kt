package com.csc780fall21.soulnareapplication.data.repository

import android.util.Log
import com.csc780fall21.soulnareapplication.domain.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

/**
 * References: https://pradyotprksh4.medium.com/%EF%B8%8Fflashchat-jetpack-compose-firebase-bd16014b025d
 */
class MessagesRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    fun createRoom(): String {
        val newRoomRef = firestore.collection(ROOMS_COLLECTION).document()
        newRoomRef
            .collection(MESSAGES_COLLECTION)
            .add(Message(newRoomRef.id, "SKIP", "SKIP", "SKIP", FieldValue.serverTimestamp()))
        return newRoomRef.id
    }

    fun addMessage() {

    }

    /**
     * Get the messages
     */
    fun getMessages(roomId: String) {
        firestore.collection(ROOMS_COLLECTION)
            .document(roomId)
            .collection(MESSAGES_COLLECTION)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val list = emptyList<Map<String, Any>>().toMutableList()

                if (value != null) {
                    for (doc in value) {
                        val data = doc.data

                        list.add(data)
                    }
                }
            }
    }


    companion object {
        const val TAG = "MessagesRepository"
        const val ROOMS_COLLECTION = "rooms"
        const val MESSAGES_COLLECTION = "messages"
        const val TIMESTAMP = "timestamp"
    }
}