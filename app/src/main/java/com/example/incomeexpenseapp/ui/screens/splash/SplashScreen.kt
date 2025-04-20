package com.example.incomeexpenseapp.ui.screens.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.nav.NavMainScreen
import com.example.incomeexpenseapp.ui.theme.Black
import com.example.incomeexpenseapp.ui.theme.Blue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navHostController: NavHostController
) {

    val systemBottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
    val systemTopPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

    val startAnimation = remember { mutableStateOf(false) }

    val offsetY by animateDpAsState(
        targetValue = if (startAnimation.value) 0.dp else 400.dp,
        animationSpec = tween(durationMillis = 1600)
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0f,
        animationSpec = tween(durationMillis = 1600)
    )

    val scale by animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0f,
        animationSpec = tween(durationMillis = 1600)
    )

    LaunchedEffect(Unit) {
        startAnimation.value = true
        delay(2000)
        navHostController.navigate(NavMainScreen) {
            navHostController.popBackStack()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .background(Blue, RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(systemTopPadding))
            Spacer(Modifier.weight(0.3f))
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .offset(y = offsetY)
                    .graphicsLayer(
                        alpha = alpha,
                        scaleX = scale,
                        scaleY = scale
                    ),
                painter = painterResource(id = R.drawable.income_expense_logo),
                contentDescription = null,
            )
            Spacer(Modifier.weight(0.7f))
        }
        Spacer(Modifier.weight(0.2f))
        CircularProgressIndicator(
            modifier = Modifier
                .padding(bottom = systemBottomPadding),
            color = Color.White
        )
        Spacer(Modifier.weight(0.1f))
    }
}
