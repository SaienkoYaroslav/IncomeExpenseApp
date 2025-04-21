package com.example.incomeexpenseapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.incomeexpenseapp.ui.screens.adding.AddingScreen
import com.example.incomeexpenseapp.ui.screens.analytic.AnalyticScreen
import com.example.incomeexpenseapp.ui.screens.main.MainScreen
import com.example.incomeexpenseapp.ui.screens.splash.SplashScreen

@Composable
fun NavGraph() {

    val navHostController = LocalNavController.current

    NavHost(
        navController = navHostController,
        startDestination = NavSplashScreen
    ) {

        composable<NavSplashScreen> { SplashScreen() }

        composable<NavMainScreen> { MainScreen() }

        composable<NavAddingScreen> { AddingScreen() }

        composable<NavAnalyticScreen> { AnalyticScreen() }

    }
}