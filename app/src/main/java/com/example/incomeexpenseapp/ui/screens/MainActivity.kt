package com.example.incomeexpenseapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.incomeexpenseapp.nav.LocalNavController
import com.example.incomeexpenseapp.nav.NavGraph
import com.example.incomeexpenseapp.ui.components.BottomNavigationBar
import com.example.incomeexpenseapp.ui.theme.IncomeExpenseAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        enableEdgeToEdge()
        setContent {
            IncomeExpenseAppTheme {

                val systemUiController = rememberSystemUiController()
                val navHostController = rememberNavController()

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        isNavigationBarContrastEnforced = false,
                        darkIcons = false
                    )
                }

                Scaffold(
                    contentWindowInsets = WindowInsets.statusBars.only(
                        WindowInsetsSides.Horizontal
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        CompositionLocalProvider(
                            LocalNavController provides navHostController
                        ) {
                            BottomNavigationBar(
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                NavGraph()
                            }
                        }

                    }
                }
            }
        }
    }
}
