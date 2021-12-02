package com.csc780fall21.soulnareapplication.view.messages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.csc780fall21.soulnareapplication.data.repository.MessagesRepository
import com.csc780fall21.soulnareapplication.data.repository.UsersRepository
import com.csc780fall21.soulnareapplication.domain.model.User
import com.csc780fall21.soulnareapplication.view.home.HomeViewModel
import com.csc780fall21.soulnareapplication.view.home.HomeViewModelFactory
import com.csc780fall21.soulnareapplication.view.likes.LikesViewModel
import com.csc780fall21.soulnareapplication.view.likes.LikesViewModelFactory
import com.csc780fall21.soulnareapplication.view.profile.ProfileViewModel
import com.csc780fall21.soulnareapplication.view.profile.ProfileViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun MessageScreen(
    navController: NavController,
    roomId: String?,
    messagesViewModel: MessagesViewModel = viewModel(
        factory = MessagesViewModelFactory(UsersRepository(), MessagesRepository())
    ),
    homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(UsersRepository())
    ),
) {
    roomId?.let { Text(text = it) }

    val message: String by messagesViewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by messagesViewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            elevation = 4.dp,
            title = {
                Text("Message")
            },
            backgroundColor = MaterialTheme.colors.primarySurface,
        )
    }
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Bottom
//    ) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(weight = 0.85f, fill = true),
//            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
//            verticalArrangement = Arrangement.spacedBy(4.dp),
//            reverseLayout = true
//        ) {
//            items(messages) { message ->
//                val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean
//
//                SingleMessage(
//                    message = message[Constants.MESSAGE].toString(),
//                    isCurrentUser = isCurrentUser
//                )
//            }
//        }
//        OutlinedTextField(
//            value = message,
//            onValueChange = {
//                homeViewModel.updateMessage(it)
//            },
//            label = {
//                Text(
//                    "Type Your Message"
//                )
//            },
//            maxLines = 1,
//            modifier = Modifier
//                .padding(horizontal = 15.dp, vertical = 1.dp)
//                .fillMaxWidth()
//                .weight(weight = 0.09f, fill = true),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Text
//            ),
//            singleLine = true,
//            trailingIcon = {
//                IconButton(
//                    onClick = {
//                        homeViewModel.addMessage()
//                    }
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Send,
//                        contentDescription = "Send Button"
//                    )
//                }
//            }
//        )
//    }
}

/**
 * References: https://github.com/raipankaj/Bookish/blob/main/app/src/main/java/com/sample/jetbooks/MainActivity.kt
 */
@ExperimentalCoroutinesApi
class MessageViewModelFactory(private val usersRepository: UsersRepository, private val messagesRepository: MessagesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessagesViewModel::class.java)) {
            return MessagesViewModel(usersRepository, messagesRepository) as T
        }

        throw IllegalStateException()
    }
}
