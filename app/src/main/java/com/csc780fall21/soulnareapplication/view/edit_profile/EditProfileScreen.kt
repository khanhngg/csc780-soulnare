package com.csc780fall21.soulnareapplication.view.edit_profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.csc780fall21.soulnareapplication.domain.repository.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun EditProfileScreen(
    navController: NavController,
    editProfileViewModel: EditProfileViewModel = viewModel(
        factory = EditProfileViewModelFactory(UsersRepository())
    ),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBar(
                elevation = 4.dp,
                title = {
                    Text("Edit Profile")
                },
                backgroundColor =  MaterialTheme.colors.primarySurface,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                })

            EditProfileSection(editProfileViewModel = editProfileViewModel)
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun EditProfileSection(editProfileViewModel: EditProfileViewModel) {
    val genreQuery: String by editProfileViewModel.genreQuery.observeAsState("")
    val genreSearchResults: List<String> by editProfileViewModel.genreSearchResult.observeAsState(mutableListOf())

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp,
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                /**
                 * References: https://stackoverflow.com/questions/59133100/how-to-close-the-virtual-keyboard-from-a-jetpack-compose-textfield
                 */
                val keyboardController = LocalSoftwareKeyboardController.current
                TextField(
                    value = genreQuery,
                    onValueChange = { newValue -> editProfileViewModel.updateGenreQuery(newValue) },
                    label = {
                        Text("Search for genres")
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                    leadingIcon =  {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search icon")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFF8F9FA)
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            editProfileViewModel.searchGenre(genreQuery)
                            keyboardController?.hide()
                        }
                    )
                )
            }
        }

        // Search results
        LazyColumn() {
            items(genreSearchResults) { result ->
                SearchResultItem(model = result, editProfileViewModel = editProfileViewModel)
                if (result !== "No results found") {
                    Divider(
                        color = Color(0xFFF8F9FA),
                        thickness = 1.dp,
                    )
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun SearchResultItem(model : String, editProfileViewModel: EditProfileViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = model,
            modifier = Modifier.padding(start = 10.dp)
        )
        if (model !== "No results found") {
            IconButton(onClick = {
                editProfileViewModel.addGenre(model)
            }) {
                Icon(Icons.Default.AddCircle, null, tint = Color(0xFF343A40))
            }
        }
    }
}

/**
 * References: https://github.com/raipankaj/Bookish/blob/main/app/src/main/java/com/sample/jetbooks/MainActivity.kt
 */
@ExperimentalCoroutinesApi
class EditProfileViewModelFactory(private val usersRepository: UsersRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(usersRepository) as T
        }

        throw IllegalStateException()
    }
}
