package com.csc780fall21.soulnareapplication.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.csc780fall21.soulnareapplication.ui.screens.home.HomeScreen
import com.csc780fall21.soulnareapplication.ui.screens.likes.LikesScreen
import com.csc780fall21.soulnareapplication.ui.screens.messages.MessagesScreen
import com.csc780fall21.soulnareapplication.ui.screens.profile.ProfileScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen()
        }

        composable("likes") {
            LikesScreen()
        }

        composable("messages") {
            MessagesScreen()
        }

        composable("profile") {
            ProfileScreen()
        }
    }
}
