package com.asepssp.soccerzonecompose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asepssp.soccerzonecompose.nav.NavigationItem
import com.asepssp.soccerzonecompose.nav.Screen
import com.asepssp.soccerzonecompose.ui.screen.aboutpage.AboutScreen
import com.asepssp.soccerzonecompose.ui.screen.detailpage.DetailScreen
import com.asepssp.soccerzonecompose.ui.screen.favoritepage.FavoriteScreen
import com.asepssp.soccerzonecompose.ui.screen.homepage.HomeScreen

@Composable
fun JetPlayerApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentRoute != Screen.DetailPlayer.route) {
                if (currentRoute != Screen.Favorite.route) {
                    if (currentRoute != Screen.About.route) {
                        BottomBar(navController = navController)
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Screen.Home.route
            ) {
                HomeScreen(navigateToDetailScreen = { animeId ->
                    navController.navigate(Screen.DetailPlayer.createRoute(animeId))
                })
            }
            composable(
                route = Screen.DetailPlayer.route,
                arguments = listOf(navArgument("animeId") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("animeId") ?: ""
                DetailScreen(
                    playerId = id,
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable(
                route = Screen.Favorite.route
            ) {
                FavoriteScreen(
                    navigateBack = { navController.navigateUp() },
                    navigateToDetail = { animeId ->
                        navController.navigate(Screen.DetailPlayer.createRoute(animeId))
                    }
                )
            }
            composable(
                route = Screen.About.route
            ) {
                AboutScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.menu_favorite),
                icon = Icons.Default.Favorite,
                screen = Screen.Favorite
            ),
            NavigationItem(
                title = stringResource(R.string.menu_about),
                icon = Icons.Default.Person,
                screen = Screen.About
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}