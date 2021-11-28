package com.csc780fall21.soulnareapplication.view.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * References: https://github.com/pradyotprksh/development_learning/tree/main/jetpack_compose/FlashChat
 */
@Composable
fun RegisterScreen(
    home: () -> Unit,
    back: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val email: String by registerViewModel.email.observeAsState("")
    val password: String by registerViewModel.password.observeAsState("")
    val loading: Boolean by registerViewModel.loading.observeAsState(initial = false)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (loading) {
            CircularProgressIndicator()
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                elevation = 4.dp,
                title = {
                    Text("Register")
                },
                backgroundColor =  MaterialTheme.colors.primarySurface,
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                })

            OutlinedTextField(
                value = email,
                onValueChange = { registerViewModel.updateEmail(it) },
                label = {
                    Text("Email")
                },
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                visualTransformation = VisualTransformation.None
            )

            OutlinedTextField(
                value = password,
                onValueChange = { registerViewModel.updatePassword(it) },
                label = {
                    Text("Password")
                },
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { registerViewModel.registerUser(home = home) }) {
                Text("Register")
            }
        }
    }
}
