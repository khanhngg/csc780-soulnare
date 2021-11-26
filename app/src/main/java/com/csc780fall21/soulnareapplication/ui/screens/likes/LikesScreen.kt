package com.csc780fall21.soulnareapplication.ui.screens.likes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.csc780fall21.soulnareapplication.models.User

@Composable
fun LikesScreen() {
    val users = mutableListOf<User>()
    users.add(User("John", "", 0, "", "", ""))
    users.add(User("Joe", "", 0, "", "", ""))
    users.add(User("David", "", 0, "", "", ""))
    users.add(User("Jerry", "", 0, "", "", ""))
    users.add(User("James", "", 0, "", "", ""))
    users.add(User("Mike", "", 0, "", "", ""))

    Box(modifier = Modifier.fillMaxSize()) {
        // TODO - add top app bar?

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
            items(users) { user ->
                LikesRow(user)
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                )
            }
        }
    }
}

@Composable
fun LikesRow(model: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Row(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
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
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = model.name
            )
        }

        Row(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // reject button
            IconButton(onClick = {  },
                modifier = Modifier
                    .size(30.dp)
                    .border(1.dp, Color.Red, shape = CircleShape)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Reject",
                    tint = Color.Red,
                    modifier = Modifier.size(15.dp)
                )
            }

            // like button
            IconButton(onClick = {  },
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(30.dp)
                    .border(1.dp, Color.Green, shape = CircleShape)
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Like",
                    tint = Color.Green,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}