package com.csc780fall21.soulnareapplication.ui.features.messages

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.csc780fall21.soulnareapplication.domain.model.User

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