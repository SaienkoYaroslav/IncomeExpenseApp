package com.example.incomeexpenseapp.nav

import androidx.annotation.DrawableRes
import com.example.incomeexpenseapp.R

enum class BottomNavScreens(val route: NavScreen, @DrawableRes val iconId: Int) {
    BotNavMainScreen(NavMainScreen, R.drawable.ie_home_ic),
    BotNavAddingScreen(NavAddingScreen, R.drawable.ie_add_ic),
    BotNavAnalyticScreen(NavAnalyticScreen, R.drawable.ie_analytic_ic),
}