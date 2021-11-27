package com.csc780fall21.soulnareapplication.ui.screens.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.csc780fall21.soulnareapplication.models.Message
import com.csc780fall21.soulnareapplication.models.User

@Composable
fun MessagesScreen(navController: NavController) {
    // messages for this user
    val messages = mutableListOf<Message>()
    messages.add(Message("1", "1", "Hello", "Joe", "Julia", 0))
    messages.add(Message("2", "1","Hello you", "Mike", "Julia", 0))
    messages.add(Message("3","1", "Yo", "John", "Julia",  0))
    messages.add(Message("4", "1","Ayy", "David", "Julia", 0))
    messages.add(Message("5", "1","sup", "Barry", "Julia",  0))
    messages.add(Message("6", "1","da hello", "Danny", "Julia",  0))
    messages.add(Message("7", "1","heyyy", "Jason", "Julia",  0))
    messages.add(Message("8", "1","da hello", "Danny", "Julia",  0))
    messages.add(Message("9", "1","heyyy", "Jasonnn", "Julia",  0))
    messages.add(Message("10", "1","da hello", "Joey", "Julia", 0))
    messages.add(Message("11", "1","heyyy", "Brandon", "Julia",  0))

    // current user
    val user = User("Julia", "", 0, "", "", "", messages)

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            elevation = 4.dp,
            title = {
                Text("Messages")
            },
            backgroundColor =  MaterialTheme.colors.primarySurface,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
        ) {
            items(messages) { user ->
                MessageRow(user, navController)
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                )
            }
        }
    }
}

@Composable
fun MessageRow(model: Message, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp)
            .clickable { navController.navigate("message") },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // profile picture
        val painter =
            rememberImagePainter(data = "https://firebasestorage.googleapis.com/v0/b/csc780-fall21-project.appspot.com/o/matthew-hamilton-tNCH0sKSZbA-unsplash.jpg?alt=media&token=3656bfa6-0047-4fd2-944b-ffdc4b44c7e0",
                builder = {
                    transformations(CircleCropTransformation())
                })
        Image (
            painter = painter,
            contentDescription = "Profile Picture",
            modifier = Modifier.size(50.dp)
        )

        // name
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = model.fromUserId,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = model.text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}
