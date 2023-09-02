package com.example.anime.ui.screens.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.anime.ui.screens.bottom_navigation.BottomNavBar
import com.example.anime.ui.screens.bottom_navigation.NavDestinations
import com.example.anime.ui.screens.bottom_navigation.Screen
import com.example.anime.ui.screens.home.HomeScreen
import com.example.anime.ui.screens.upload.UploadScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainScreen() {

    var bottomBarVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    bottomBarVisible = when (navBackStackEntry?.destination?.route) {
        Screen.Home.route -> true
        Screen.Upload.route -> true
        else -> false
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController, visible = bottomBarVisible) }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = NavDestinations.MAIN,
            modifier = Modifier.padding(paddingValues)
        ) {

            navigation(startDestination = Screen.Home.route, route = NavDestinations.MAIN) {
                composable(route = Screen.Home.route) {
                    HomeScreen(navController = navController)
                }

                composable(route = Screen.Upload.route) {
                    UploadScreen(navController = navController)
                }
            }

        }

    }

}