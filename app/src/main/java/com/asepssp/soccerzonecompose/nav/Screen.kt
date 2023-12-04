package com.asepssp.soccerzonecompose.nav

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object About : Screen("about")
    object DetailPlayer : Screen("home/{animeId}") {
        fun createRoute(animeId: String) = "home/$animeId"
    }
}