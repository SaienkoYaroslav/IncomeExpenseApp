package com.example.incomeexpenseapp.nav

import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
sealed interface NavScreen


@Serializable
data object NavSplashScreen : NavScreen

@Serializable
data object NavMainScreen : NavScreen

@Serializable
data object NavAddingScreen : NavScreen

@Serializable
data object NavAnalyticScreen : NavScreen


fun NavBackStackEntry?.routeClass(): KClass<*>? {
    return this?.destination?.route
        ?.split("?")
        ?.first()
        ?.let { Class.forName(it)}
        ?.kotlin
}