package com.csc780fall21.soulnareapplication.domain.model

data class User(
    val name: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val avatar: Int = 0,
    val genres: List<String> = mutableListOf(),
    val artists: List<String> = mutableListOf(),
    val songs: List<String> = mutableListOf(),
    val songIDs: List<String> = mutableListOf(),
    val messageIds: MutableList<Message> = mutableListOf(),
) {
    constructor() : this("", "", "", "", 0, mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
}
