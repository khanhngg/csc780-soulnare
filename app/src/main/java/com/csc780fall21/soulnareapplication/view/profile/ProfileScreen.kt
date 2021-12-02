package com.csc780fall21.soulnareapplication.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.VolunteerActivism
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
import com.csc780fall21.soulnareapplication.data.repository.UsersRepository
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
                ArtistsSection(navController, user, editProfileViewModel)
                SongsSection(navController, user, editProfileViewModel)
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
            rememberImagePainter(data = user.avatar ?: User.DEFAULT_AVATAR_URL,
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
                horizontalArrangement = Arrangement.Start
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

@ExperimentalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun ArtistsSection(navController: NavController, user : User?, editProfileViewModel: EditProfileViewModel) {
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

        // Content
        if (user?.artists?.isNotEmpty() == true) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                items(user.artists) { model ->
                    ArtistItem(model = model, editProfileViewModel = editProfileViewModel)
                }
            }
        } else {
            Text(text = "No artists yet.")
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun ArtistItem(
    model: Map<String, String>,
    editProfileViewModel: EditProfileViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (model["imageUrl"] == "") {
                Icon(
                    imageVector = Icons.Filled.Face,
                    contentDescription = null,
                    tint = Color(0xFFADB5BD),
                    modifier = Modifier.size(100.dp)
                )
            } else {
                val painter =
                    rememberImagePainter(data = model["imageUrl"]!!,
                        builder = {
                            transformations(CircleCropTransformation())
                        })
                Image(
                    painter = painter,
                    contentDescription = "Artist Picture",
                    modifier = Modifier.size(100.dp)
                )
            }
            Text(
                text = model["name"]!!,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }

        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = {
                editProfileViewModel.deleteArtist(artist = model["name"]!!, imageUrl = model["imageUrl"]!!)
        }) {
            Icon(Icons.Default.Delete, null)
        }
    }
}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@Composable
fun SongsSection(navController: NavController, user : User?, editProfileViewModel: EditProfileViewModel) {
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
        if (user!!.songs.isNotEmpty() && user.songs[0].isNotEmpty()) {
            user.songs.forEach { song ->
                SongItem(model = song, editProfileViewModel = editProfileViewModel)
            }
        } else {
            Text(text = "No songs yet.")
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun SongItem(model: Map<String, String>, editProfileViewModel: EditProfileViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberImagePainter(data = model["imageUrl"])
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
                    text = model["name"]!!,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = model["artists"]!!,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    editProfileViewModel.deleteSong(uri = model["uri"]!!, imageUrl = model["imageUrl"]!!, artists = model["artists"]!!, name = model["name"]!!)
                }) {
                Icon(Icons.Default.Delete, null)
            }
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
