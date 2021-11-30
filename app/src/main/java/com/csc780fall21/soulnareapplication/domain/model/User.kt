package com.csc780fall21.soulnareapplication.domain.model

data class User(
    val uid: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val avatar: Int? = 0,
    val genres: List<String> = mutableListOf(),
    val artists: List<Map<String, String>> = mutableListOf(),
    val songs: List<Map<String, String>> = mutableListOf(),
    val messageIds: MutableList<Message> = mutableListOf(),
) {
//    constructor() : this("", "", "", "", 0, mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
}
