package com.csc780fall21.soulnareapplication.view.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.csc780fall21.soulnareapplication.domain.model.*
import com.csc780fall21.soulnareapplication.domain.repository.UsersRepository
import com.csc780fall21.soulnareapplication.view.edit_profile.EditProfileViewModel
import com.csc780fall21.soulnareapplication.view.edit_profile.EditProfileViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow

@ExperimentalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(UsersRepository())
    ),
    editProfileViewModel: EditProfileViewModel = viewModel(
        factory = EditProfileViewModelFactory(UsersRepository())
    ),
    ) {
    when (val userProfile = profileViewModel.userStateFlow.asStateFlow().collectAsState().value) {
        is OnError -> {
            Text(text = "Please try after sometime")
        }

        is OnSuccess -> {
            val user = userProfile.documentSnapshot?.toObject(User::class.java)
            user?.let {
                Log.i("ProfileScreen", it.toString())
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                TopAppBar(
                    elevation = 4.dp,
                    title = {
                        Text("Profile")
                    },
                    backgroundColor =  MaterialTheme.colors.primarySurface,
                )
                ProfileSection(user)
                GenresSection(navController, user, editProfileViewModel)
                ArtistsSection(navController)
                SongsSection(navController)
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ProfileSection(user : User?) {
    // user's name
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${user!!.firstName} ${user.lastName}",
            style = MaterialTheme.typography.h4
        )

        /**
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

@ExperimentalCoroutinesApi
@Composable
fun GenresSection(navController: NavController, user : User?, editProfileViewModel: EditProfileViewModel) {
    Column(modifier = Modifier.padding(10.dp, 15.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Label
            Text(
                text = "Genres",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )

            // Button
            TextButton(onClick = { navController.navigate("edit-profile/genres") }) {
                Text("Edit")
            }
        }

        // Content
        if (user!!.genres.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(user.genres) { genre ->
                    GenreChip(
                        genre = genre,
                        editProfileViewModel = editProfileViewModel
                    )
                }
            }
        } else {
            Text(text = "No music genres yet.")
        }
    }
}

/**
 * References: https://medium.com/@Rieger_san/create-a-chipgroup-with-jetpack-compose-f4744b94fa34
 */
@ExperimentalCoroutinesApi
@Composable
fun GenreChip(
    genre: String,
    editProfileViewModel: EditProfileViewModel
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.primary
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = genre,
                style = MaterialTheme.typography.body2,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
            IconButton(onClick = {
                editProfileViewModel.deleteGenre(genre = genre)
            }) {
                Icon(Icons.Default.Clear, null)
            }
        }
    }
}

private val artists = mutableListOf<Artist>()

@Composable
fun ArtistsSection(navController: NavController) {
    var hasArtists = true

    // TODO
    artists.add(Artist("Adele", ""))
    artists.add(Artist("The Beatles", ""))
    artists.add(Artist("Nirvana", ""))
    artists.add(Artist("Radiohead", ""))
    artists.add(Artist("Coldplay", ""))
    artists.add(Artist("Beach Boys", ""))

    Column(modifier = Modifier.padding(10.dp, 15.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Label
            Text(
                text = "Artist",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )

            // Button
            TextButton(onClick = { navController.navigate("edit-profile/artists") }) {
                Text("Edit")
            }
        }

        // TODO - show text if no content
        // Content
        if (hasArtists) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(artists) { model ->
                    ArtistItem(model = model)
                }
            }
        } else {
            Text(text = "No artists yet.")
        }
    }
}

@Composable
fun ArtistItem(model: Artist) {
    Column(
        modifier = Modifier.padding(10.dp, 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO - use avatar string
        val painter =
            rememberImagePainter(data = "https://firebasestorage.googleapis.com/v0/b/csc780-fall21-project.appspot.com/o/matthew-hamilton-tNCH0sKSZbA-unsplash.jpg?alt=media&token=3656bfa6-0047-4fd2-944b-ffdc4b44c7e0",
                builder = {
                    transformations(CircleCropTransformation())
                })
        Image(
            painter = painter,
            contentDescription = "Artist Picture",
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = model.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )
    }
}

private val songs = mutableListOf<Song>()

@Composable
fun SongsSection(navController: NavController) {
    var hasSongs = true

    songs.add(Song("Easy on me", "Adele", ""))
    songs.add(Song("Ok Computer", "Radiohead", ""))
    songs.add(Song("Creep", "Radiohead", ""))
    songs.add(Song("No Surprises", "Radiohead", ""))
    songs.add(Song("Karma Police", "Radiohead", ""))
    songs.add(Song("Hello", "Adele", ""))
    songs.add(Song("Turning Tables", "Adele", ""))

    Column(modifier = Modifier.padding(10.dp, 15.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Label
            Text(
                text = "Songs",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )

            // Button
            TextButton(onClick = { navController.navigate("edit-profile/songs") }) {
                Text("Edit")
            }
        }

        // Content
        if (hasSongs) {
            songs.forEach { song ->
                SongItem(model = song)
            }
        } else {
            Text(text = "No songs yet.")
        }
    }
}

@Composable
fun SongItem(model: Song) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO - use album cover string
        val painter = rememberImagePainter(data = "https://firebasestorage.googleapis.com/v0/b/csc780-fall21-project.appspot.com/o/matthew-hamilton-tNCH0sKSZbA-unsplash.jpg?alt=media&token=3656bfa6-0047-4fd2-944b-ffdc4b44c7e0")
        Image(
            painter = painter,
            contentDescription = "Album Cover Picture",
            modifier = Modifier.size(100.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = model.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = model.artist,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

/**
 * References: https://github.com/raipankaj/Bookish/blob/main/app/src/main/java/com/sample/jetbooks/MainActivity.kt
 */
@ExperimentalCoroutinesApi
class ProfileViewModelFactory(private val usersRepository: UsersRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(usersRepository) as T
        }

        throw IllegalStateException()
    }
}
