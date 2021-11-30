package com.csc780fall21.soulnareapplication.domain.model

data class User(
    val uid: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val avatar: String? = "https://firebasestorage.googleapis.com/v0/b/csc780-fall21-project.appspot.com/o/matthew-hamilton-tNCH0sKSZbA-unsplash.jpg?alt=media&token=3656bfa6-0047-4fd2-944b-ffdc4b44c7e0",
    val genres: List<String> = mutableListOf(),
    val artists: List<Map<String, String>> = mutableListOf(),
    val songs: List<Map<String, String>> = mutableListOf(),
    val messageIds: MutableList<Message> = mutableListOf(),
) {
//    constructor() : this("", "", "", "", 0, mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
}
