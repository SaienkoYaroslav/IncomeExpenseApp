package com.example.incomeexpenseapp.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.incomeexpenseapp.ui.screens.adding.AddingScreen
import com.example.incomeexpenseapp.ui.screens.analytic.AnalyticScreen
import com.example.incomeexpenseapp.ui.screens.main.MainScreen
import com.example.incomeexpenseapp.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = NavSplashScreen
    ) {

        composable<NavSplashScreen> {
            SplashScreen(
                navHostController = navHostController,
            )
        }

        composable<NavMainScreen> {
            MainScreen(
                viewModel = hiltViewModel()
            )
        }

        composable<NavAddingScreen> {
            AddingScreen(
                viewModel = hiltViewModel(),
                viewModelTrans = hiltViewModel()
            )
        }

        composable<NavAnalyticScreen> {
            AnalyticScreen(
                viewModel = hiltViewModel()
            )
        }

    }
}