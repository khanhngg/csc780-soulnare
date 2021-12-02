package com.csc780fall21.soulnareapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import coil.annotation.ExperimentalCoilApi
import com.csc780fall21.soulnareapplication.view.AuthenticationScreen
import com.csc780fall21.soulnareapplication.view.edit_profile.EditProfileScreen
import com.csc780fall21.soulnareapplication.view.home.HomeScreen
import com.csc780fall21.soulnareapplication.view.likes.LikesScreen
import com.csc780fall21.soulnareapplication.view.login.LoginScreen
import com.csc780fall21.soulnareapplication.view.messages.MessageScreen
import com.csc780fall21.soulnareapplication.view.messages.MessagesScreen
import com.csc780fall21.soulnareapplication.view.profile.ProfileScreen
import com.csc780fall21.soulnareapplication.view.register.RegisterScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * References:
 * - https://github.com/philipplackner/BottomNavWithBadges
 * - https://github.com/pradyotprksh/development_learning/tree/main/jetpack_compose/FlashChat
 */
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalCoilApi
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination =
        if (FirebaseAuth.getInstance().currentUser != null)
            "home"
        else
            "auth"
    ) {
        composable("auth") {
            AuthenticationScreen(
                register = { navController.navigate("register") },
                login = { navController.navigate("login") },
            )
        }

        composable("register") {
            RegisterScreen(
                home = {
                    navController.navigate("home") {
                        popUpTo("register") {
                            inclusive = true
                        }
                    }},
                back = { navController.popBackStack() }
            )
        }

        composable("login") {
            LoginScreen(
                home = {
                    navController.navigate("home") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }},
                back = { navController.popBackStack() }
            )
        }

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
            composable("message/{roomId}") { backStackEntry ->
                MessageScreen(navController = navController, roomId = backStackEntry.arguments?.getString("roomId"))
            }
        }

        navigation(startDestination = "profile", route = "user-profile") {
            composable("profile") {
                ProfileScreen(navController)
            }

            composable("edit-profile/{field}") { backStackEntry ->
                EditProfileScreen(navController = navController, field = backStackEntry.arguments?.getString("field"))
            }
        }
    }
}
