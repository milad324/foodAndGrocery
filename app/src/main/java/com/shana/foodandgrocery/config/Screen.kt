package com.shana.foodandgrocery.config

sealed class Screen(val route: String) {
    object MainScreen : Screen("Home")
    object ShowRecipe : Screen("ShowRecipe")
    object SearchFilterScreen : Screen("SearchFilter")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}