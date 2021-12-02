package com.csc780fall21.soulnareapplication.domain.model

import com.google.firebase.firestore.FieldValue

data class Message(
    val roomId: String,
    val id: String,
    val text: String,
    val fromUserId: String,
    val timestamp: FieldValue,
)
