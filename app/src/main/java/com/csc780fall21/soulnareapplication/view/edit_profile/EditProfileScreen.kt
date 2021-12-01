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
import com.csc780fall21.soulnareapplication.data.repository.UsersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun EditProfileScreen(
    navController: NavController,
    field: String? = "genres",
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
                    Text("Edit ${field}")
                },
                backgroundColor =  MaterialTheme.colors.primarySurface,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                })

            EditProfileSection(editProfileViewModel = editProfileViewModel, field = field)
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun EditProfileSection(
    editProfileViewModel: EditProfileViewModel,
    field: String? = "genres"
) {
    val genreQuery: String by editProfileViewModel.genreQuery.observeAsState("")
    val genreSearchResults: List<String> by editProfileViewModel.genreSearchResults.observeAsState(mutableListOf())

    val artistQuery: String by editProfileViewModel.artistQuery.observeAsState("")
    val artistSearchResults: Map<String, String> by editProfileViewModel.artistSearchResults.observeAsState(mutableMapOf())

    val songQuery: String by editProfileViewModel.songQuery.observeAsState("")
    val songSearchResults: Map<String, Map<String, String>> by editProfileViewModel.songSearchResults.observeAsState(mutableMapOf())

    when (field) {
        "genres" -> {
            SearchSection(
                field = field,
                query = genreQuery,
                searchResults = genreSearchResults,
                updateQuery = { newValue -> editProfileViewModel.updateGenreQuery(newValue) },
                search = { query -> editProfileViewModel.searchGenres(query = query) },
                addItem = { value -> editProfileViewModel.addGenre(value) }
            )
        }
        "artists" -> {
            SearchSection(
                field = field,
                query = artistQuery,
                searchResults = artistSearchResults.keys.toList(),
                updateQuery = { newValue -> editProfileViewModel.updateArtistQuery(newValue) },
                search = { query -> editProfileViewModel.searchArtists(query = query) },
                addItem = { value -> editProfileViewModel.addArtist(value) }
            )
        }
        else -> {
            SearchSection(
                field = field,
                query = songQuery,
                searchResults = songSearchResults.entries.map { it.value["uri"] },
                resultsMap = songSearchResults,
                updateQuery = { newValue -> editProfileViewModel.updateSongQuery(newValue) },
                search = { query -> editProfileViewModel.searchSongs(query = query) },
                addItem = { value -> editProfileViewModel.addSong(value) }
            )
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@Composable
fun SearchSection(
    field: String?,
    query: String,
    searchResults: List<String?>,
    resultsMap: Map<String, Map<String, String>>? = null,
    updateQuery: (String) -> Unit,
    search: (String) -> Unit,
    addItem: (String) -> Unit
) {
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
                    value = query,
                    onValueChange = { newValue -> updateQuery(newValue) },
                    label = {
                        Text("Search for $field")
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
                            search(query)
                            keyboardController?.hide()
                        }
                    )
                )
            }
        }

        // Search results
        LazyColumn {
            items(searchResults) { result ->
                SearchResultItem(
                    model = result!!,
                    add = { result -> addItem(result) },
                    resultsMap = resultsMap
                )
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
fun SearchResultItem(
    model : String,
    add: (String) -> Unit,
    resultsMap: Map<String, Map<String, String>>? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val text: String = if (resultsMap != null) {
            resultsMap[model]?.get("name")!!
        } else {
            model
        }
        Text(
            text = text,
            modifier = Modifier.padding(start = 10.dp)
        )
        // TODO - test
        if (model !== "No results found") {
            IconButton(onClick = {
                add(model)
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
