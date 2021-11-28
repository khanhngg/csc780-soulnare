package com.csc780fall21.soulnareapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.csc780fall21.soulnareapplication.ui.theme.SoulnareApplicationTheme

/**
 * References: https://github.com/pradyotprksh/development_learning/tree/main/jetpack_compose/FlashChat
 */
@Composable
fun AuthenticationScreen(register: () -> Unit, login: () -> Unit) {
    SoulnareApplicationTheme() {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Soulnare", modifier = Modifier.padding(bottom = 20.dp))

                Button(onClick = register) {
                    Text("Register")
                }

                TextButton(onClick = login) {
                    Text("Log In")
                }
            }
        }
    }
}