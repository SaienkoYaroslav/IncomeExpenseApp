package com.example.incomeexpenseapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.incomeexpenseapp.nav.BottomNavScreens
import com.example.incomeexpenseapp.nav.NavMainScreen
import com.example.incomeexpenseapp.nav.routeClass
import com.example.incomeexpenseapp.ui.theme.Blue

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    content: @Composable () -> Unit,
) {

    val systemBottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp.dp
    val destination = navBackStackEntry.routeClass()

    Box(modifier = modifier.fillMaxSize()) {
        content()
        if (BottomNavScreens.entries.any { it.route::class == destination }) {

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 5.dp)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(horizontal = 5.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        BottomNavScreens.entries.forEach { item ->
                            val isSelectedItem = destination == item.route::class
                            Box(
                                modifier = modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color = if (isSelectedItem) Blue else Color.Transparent)
                                    .padding(5.dp)
//                                    .size(screenWidthDp / 10)
                                    .clickable(
                                        enabled = !isSelectedItem,
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            navHostController.navigate(item.route) {
                                                popUpTo(NavMainScreen) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 5.dp,
                                        vertical = 5.dp
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        painter = painterResource(item.iconId),
                                        contentDescription = null,
                                        tint = Color.White
                                    )

                                }

                            }
                        }

                    }
                }
                Spacer(Modifier.height(systemBottomPadding))
            }

        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewBottomBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue)
    ) {
        BottomNavigationBar(navHostController = rememberNavController()) {

        }
    }

}


