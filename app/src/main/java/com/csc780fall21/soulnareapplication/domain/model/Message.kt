package com.csc780fall21.soulnareapplication.domain.model

data class Message(
    val id: String,
    val messageThreadId: String,
    val text: String,
    val fromUserId: String,
    val toUserId: String,
    val sentOn: Long,
)
