package com.csc780fall21.soulnareapplication.ui.screens.messages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.csc780fall21.soulnareapplication.models.User

@Composable
fun MessageScreen(navController: NavController) {
    // TODO - if user.id == fromId --> your message; ele --> their msg
    val messages = mutableListOf<User>()

    Text(text = "In message screen!!!")
//    Column(modifier = Modifier.fillMaxSize()) {
//        TopAppBar(
//            elevation = 4.dp,
//            title = {
//                Text("Likes")
//            },
//            backgroundColor =  MaterialTheme.colors.primarySurface,
//            navigationIcon = {
//                IconButton(onClick = {/* Do Something*/ }) {
//                    Icon(Icons.Filled.ArrowBack, null)
//                }
//            })
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize(),
//            contentPadding = PaddingValues(
//                start = 12.dp,
//                top = 16.dp,
//                end = 12.dp,
//                bottom = 16.dp
//            ),
//        ) {
//            items(users) { user ->
//                LikesRow(user)
//            }
//        }
//    }
}