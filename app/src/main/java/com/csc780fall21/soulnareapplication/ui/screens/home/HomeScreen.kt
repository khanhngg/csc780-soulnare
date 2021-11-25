package com.csc780fall21.soulnareapplication.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation

@ExperimentalCoilApi
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // TODO - add topbar?
        ProfileSection()
        GenresSection()
        ArtistsSection()
        SongsSection()
    }
}

@ExperimentalCoilApi
@Composable
fun ProfileSection() {
    // user's name
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Julia Goldberg",
            style = MaterialTheme.typography.h4
        )

        /*
         * Reference: https://coil-kt.github.io/coil/compose/
         */
        // profile picture
        val painter =
            rememberImagePainter(data = "https://firebasestorage.googleapis.com/v0/b/csc780-fall21-project.appspot.com/o/matthew-hamilton-tNCH0sKSZbA-unsplash.jpg?alt=media&token=3656bfa6-0047-4fd2-944b-ffdc4b44c7e0",
                builder = {
                    transformations(CircleCropTransformation())
                })
        Image(
            painter = painter,
            contentDescription = "Profile Picture",
            modifier = Modifier.size(128.dp)
        )
    }
}

@Composable
fun GenresSection() {
    var hasGenres = false
    Column(modifier = Modifier.padding(10.dp, 15.dp)) {
        // Label
        Text(
            text = "Genres",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )

        // Content
        if (hasGenres) {
            Text(text = "Pop, Rock, Rap")
        } else {
            Text(text = "No music genres yet.")
        }
    }
}

@Composable
fun ArtistsSection() {
    var hasArtists = false
    Column(modifier = Modifier.padding(10.dp, 15.dp)) {
        // Label
        Text(
            text = "Artists",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )

        // TODO - show text if no content
        // Content
        if (hasArtists) {
            Text(text = "Pop, Rock, Rap")
        } else {
            Text(text = "No artists yet.")
        }
    }
}

@Composable
fun SongsSection() {
    var hasSongs = false
    Column(modifier = Modifier.padding(10.dp, 15.dp)) {
        // Label
        Text(
            text = "Songs",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )

        // TODO - show text if no content
        // Content
        if (hasSongs) {
            Text(text = "Pop, Rock, Rap")
        } else {
            Text(text = "No songs yet.")
        }
    }
}
