package com.csc780fall21.soulnareapplication.view.home

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
import androidx.compose.material.icons.filled.Delete
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow

@ExperimentalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(UsersRepository())
    ),
) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState()),
//    ) {
//        ProfileSection()
//        GenresSection()
//        ArtistsSection()
//        SongsSection()
//    }

    when (val userProfiles = homeViewModel.usersStateFlow.asStateFlow().collectAsState().value) {
        is OnError -> {
            Text(text = "Please try after sometime")
        }

        is OnSuccessQuery -> {
            val users = userProfiles.querySnapshot?.toObjects(User::class.java)

            Log.i("homescreen...", "users: ${users}")
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                ProfileSection(user = users?.get(0))
                GenresSection(navController, user = users?.get(0))
                ArtistsSection(navController, user = users?.get(0))
                SongsSection(navController, user = users?.get(0))
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
fun GenresSection(navController: NavController, user : User?) {
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
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun ArtistsSection(navController: NavController, user : User?) {
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
        }

        // Content
        if (user?.artists?.isNotEmpty() == true) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(user.artists) { model ->
                    ArtistItem(model = model)
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
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
        Text(
            text = model["name"]!!,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@Composable
fun SongsSection(navController: NavController, user : User?) {
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
        }

        // Content
        if (user!!.songs.isNotEmpty()) {
            user.songs.forEach { song ->
                SongItem(model = song)
            }
        } else {
            Text(text = "No songs yet.")
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun SongItem(model: Map<String, String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
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
}

/**
 * References: https://github.com/raipankaj/Bookish/blob/main/app/src/main/java/com/sample/jetbooks/MainActivity.kt
 */
@ExperimentalCoroutinesApi
class HomeViewModelFactory(private val usersRepository: UsersRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(usersRepository) as T
        }

        throw IllegalStateException()
    }
}
