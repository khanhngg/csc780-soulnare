package com.csc780fall21.soulnareapplication.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import coil.annotation.ExperimentalCoilApi
import com.csc780fall21.soulnareapplication.ui.features.home.HomeScreen
import com.csc780fall21.soulnareapplication.ui.features.likes.LikesScreen
import com.csc780fall21.soulnareapplication.ui.features.messages.MessageScreen
import com.csc780fall21.soulnareapplication.ui.features.messages.MessagesScreen
import com.csc780fall21.soulnareapplication.ui.features.profile.ProfileScreen

/**
 * References: https://github.com/philipplackner/BottomNavWithBadges
 */
@ExperimentalCoilApi
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }

        composable("likes") {
            LikesScreen(navController)
        }

        /**
         * References: https://developer.android.com/jetpack/compose/navigation#nested-nav
         */
        navigation(startDestination = "messages", route = "messaging") {
            composable("messages") {
                MessagesScreen(navController)
            }
            // TODO - pass message id from view model???
            composable("message") {
                MessageScreen(navController)
            }
        }

        composable("profile") {
            ProfileScreen(navController)
        }
    }
}
