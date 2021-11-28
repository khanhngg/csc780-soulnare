package com.csc780fall21.soulnareapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.csc780fall21.soulnareapplication.ui.BottomNavItem
import com.csc780fall21.soulnareapplication.ui.Navigation
import com.csc780fall21.soulnareapplication.ui.theme.SoulnareApplicationTheme
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * References: https://github.com/philipplackner/BottomNavWithBadges
 */
@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            SoulnareApplicationTheme() {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(
                                    name = "Home",
                                    route = "home",
                                    icon = Icons.Default.Home
                                ),
                                BottomNavItem(
                                    name = "Likes",
                                    route = "likes",
                                    icon = Icons.Default.Favorite
                                ),
                                BottomNavItem(
                                    name = "Messages",
                                    route = "messages",
                                    icon = Icons.Default.Forum
                                ),
                                BottomNavItem(
                                    name = "Profile",
                                    route = "profile",
                                    icon = Icons.Default.Person
                                ),
                            ),
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        // TODO - add user id here
                        Navigation(navController = navController)
                    }
                    /**
                    * References: https://stackoverflow.com/questions/66573601/bottom-nav-bar-overlaps-screen-content-in-jetpack-compose
                    */
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    // draws the BottomNavigation composable
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
    ) {
        // iterates through items to draw each item as a BottomNavigationItem composable
        items.forEach{ item ->
            val selected = item.route == backStackEntry.value?.destination?.route

            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Display screen's icon
                        Icon(imageVector = item.icon, contentDescription = item.name)

                        // Display screen's name
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize =  10.sp
                            )
                        }
                    }
                }
            )
        }
    }
}
