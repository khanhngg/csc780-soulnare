package com.csc780fall21.soulnareapplication.view.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.csc780fall21.soulnareapplication.data.repository.UsersRepository
import com.csc780fall21.soulnareapplication.domain.model.*
import com.csc780fall21.soulnareapplication.view.home.HomeViewModel
import com.csc780fall21.soulnareapplication.view.home.HomeViewModelFactory
import com.csc780fall21.soulnareapplication.view.likes.LikesViewModel
import com.csc780fall21.soulnareapplication.view.likes.LikesViewModelFactory
import com.csc780fall21.soulnareapplication.view.profile.ProfileViewModel
import com.csc780fall21.soulnareapplication.view.profile.ProfileViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@Composable
fun MessagesScreen(
    navController: NavController,
    messagesViewModel: MessagesViewModel = viewModel(
        factory = MessagesViewModelFactory(UsersRepository())
    ),
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
                        if (youLikeUserIds?.contains(it.uid)!! && !youRejectUserIds?.contains(it.uid)!!) {
                            tempUsersToShow.add(it)
                        }
                    }

                    val usersToShow: List<User> by homeViewModel.usersToShow.observeAsState(mutableListOf())
                    homeViewModel.updateUsersToShow(tempUsersToShow)

                    Column(modifier = Modifier.fillMaxSize()) {
                        TopAppBar(
                            elevation = 4.dp,
                            title = {
                                Text("Messages")
                            },
                            backgroundColor =  MaterialTheme.colors.primarySurface,
                        )
                        // TODO
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
                                    MessageRow(user, navController)
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
                                    text = "No messages yet!",
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier.padding(bottom = 20.dp)
                                )
                                Icon(
                                    imageVector = Icons.Filled.ChatBubbleOutline,
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
@Composable
fun MessageRow(
    user: User,
    navController: NavController
) {
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
                text = "${user.firstName} ${user.lastName}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Hi",
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
class MessagesViewModelFactory(private val usersRepository: UsersRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessagesViewModel::class.java)) {
            return MessagesViewModel(usersRepository) as T
        }

        throw IllegalStateException()
    }
}
