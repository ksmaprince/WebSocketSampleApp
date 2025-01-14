package com.khun.websocketsample.presentation

sealed class Screen(val route: String,  ) {
    object Home : Screen(route = "Home",)
    object Detail : Screen(route = "Detail/{productId}",)
}
