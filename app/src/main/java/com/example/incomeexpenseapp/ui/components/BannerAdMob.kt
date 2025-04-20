package com.example.incomeexpenseapp.ui.components

import android.app.Activity
import android.util.DisplayMetrics
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.getString
import com.example.incomeexpenseapp.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAdMob(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val activity = context as? Activity

    val testKey = getString(context, R.string.TEST_KEY_ADMOB)

    val systemBottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    if (activity != null) {
        val adSize = rememberAdSize(activity)

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = systemBottomPadding + 60.dp)
        ) {
            AndroidView(
                modifier = Modifier,
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(adSize)
                        this.adUnitId = testKey
                        loadAd(AdRequest.Builder().build())
                    }
                },
                update = { adView ->
                    adView.loadAd(AdRequest.Builder().build())
                }
            )
        }

    }
}

fun rememberAdSize(activity: Activity): AdSize {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val adWidthPixels = displayMetrics.widthPixels
    val density = displayMetrics.density
    val adWidth = (adWidthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
}