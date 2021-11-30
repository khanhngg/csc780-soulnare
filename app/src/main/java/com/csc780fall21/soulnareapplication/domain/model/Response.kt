package com.csc780fall21.soulnareapplication.domain.model

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException

/**
 * References: https://medium.com/firebase-developers/firebase-with-jetpack-compose-cloud-firestore-39d8d139536a
 */
sealed class Response
data class OnSuccess(val documentSnapshot: DocumentSnapshot?): Response()
data class OnError(val exception: FirebaseFirestoreException?): Response()