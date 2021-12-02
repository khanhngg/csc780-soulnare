package com.csc780fall21.soulnareapplication.view.likes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.csc780fall21.soulnareapplication.domain.model.OnError
import com.csc780fall21.soulnareapplication.domain.model.OnSuccessQuery
import com.csc780fall21.soulnareapplication.domain.model.User
import com.csc780fall21.soulnareapplication.data.repository.UsersRepository
import com.csc780fall21.soulnareapplication.domain.model.OnSuccess
import com.csc780fall21.soulnareapplication.view.home.*
import com.csc780fall21.soulnareapplication.view.profile.ProfileViewModel
import com.csc780fall21.soulnareapplication.view.profile.ProfileViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@Composable
fun LikesScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(UsersRepository())
    ),
    homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(UsersRepository())
    ),
    likesViewModel: LikesViewModel = viewModel(
        factory = LikesViewModelFactory(UsersRepository())
    )
) {
    when(val currentUser = profileViewModel.userStateFlow.asStateFlow().collectAsState().value) {
        is OnError -> {
            Text(text = "Please try after sometime")
        }

        is OnSuccess -> {
            val user = currentUser.documentSnapshot?.toObject(User::class.java)
            val youLikeUserIds = user?.youLikeUserIds
            val youRejectUserIds = user?.youRejectUserIds

            when (val userProfiles = likesViewModel.usersStateFlow.asStateFlow().collectAsState().value) {
                is OnError -> {
                    Text(text = "Please try after sometime")
                }

                is OnSuccessQuery -> {
                    val usersThatLikeYou = userProfiles.querySnapshot?.toObjects(User::class.java)
                    val tempUsersToShow = mutableListOf<User>()
                    usersThatLikeYou?.forEach {
                        if (!youLikeUserIds?.contains(it.uid)!! && !youRejectUserIds?.contains(it.uid)!!) {
                            tempUsersToShow.add(it)
                        }
                    }

                    val usersToShow: List<User> by homeViewModel.usersToShow.observeAsState(mutableListOf())
                    homeViewModel.updateUsersToShow(tempUsersToShow)

                    Column(modifier = Modifier.fillMaxSize()) {
                        TopAppBar(
                            elevation = 4.dp,
                            title = {
                                Text("Likes")
                            },
                            backgroundColor =  MaterialTheme.colors.primarySurface,
                        )
                        if (usersToShow.isNotEmpty()) {
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
                                items(usersToShow) { user ->
                                    LikesRow(user, homeViewModel)
                                    Divider(
                                        color = Color.LightGray,
                                        thickness = 1.dp,
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No likes yet!",
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier.padding(bottom = 20.dp)
                                )
                                Icon(
                                    imageVector = Icons.Filled.SentimentDissatisfied,
                                    contentDescription = null,
                                    tint = Color(0xFFF9844A),
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@Composable
fun LikesRow(
    user: User,
    homeViewModel: HomeViewModel
) {
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
            user.firstName?.let {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = it
                )
            }
        }

        Row(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.4f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                /**
                * References: https://stackoverflow.com/questions/66671902/how-to-create-a-circular-outlined-button-with-jetpack-compose
                 */
                // reject button
                FloatingActionButton(
                    onClick = { homeViewModel.addUserToYourRejects(user.uid) },
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = Color.White,
                    modifier = Modifier.size(30.dp)
                ){
                    Icon(
                        Icons.Filled.ThumbDown,
                        "Reject",
                        modifier = Modifier.size(15.dp)
                    )
                }

                // like button
                FloatingActionButton(
                    onClick = { homeViewModel.addUserToYourRejects(user.uid) },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                    modifier = Modifier.size(30.dp)
                ){
                    Icon(
                        Icons.Filled.ThumbUp,
                        "LIke",
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

/**
 * References: https://github.com/raipankaj/Bookish/blob/main/app/src/main/java/com/sample/jetbooks/MainActivity.kt
 */
@ExperimentalCoroutinesApi
class LikesViewModelFactory(private val usersRepository: UsersRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikesViewModel::class.java)) {
            return LikesViewModel(usersRepository) as T
        }

        throw IllegalStateException()
    }
}
