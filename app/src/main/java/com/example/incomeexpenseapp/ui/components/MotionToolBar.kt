package com.example.incomeexpenseapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.ui.theme.Blue
import com.example.incomeexpenseapp.utils.formatNumber


@OptIn(ExperimentalMotionApi::class)
@Composable
fun MotionToolBar(
    modifier: Modifier = Modifier,
    progress: Float,
    systemTopPadding: Dp,
    totalSum: Long,
    categoryType: String,
    onCategoryTypeChange: (String) -> Unit
) {

    val fontSize = remember(progress) {
        val minSize = 12
        val maxSize = 32
        TextUnit((maxSize - (maxSize - minSize) * progress), TextUnitType.Sp)
    }


    MotionLayout(
        start = startConstraintSet(),
        end = endConstraintSet(),
        progress = progress,
        modifier = modifier
            .fillMaxWidth()
            .background(
                Blue,
                RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
            )
            .padding(horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .layoutId("statusBar")
                .fillMaxWidth()
                .height(systemTopPadding)
        )
        Image(
            painter = painterResource(id = R.drawable.ie_wallet_img),
            contentDescription = null,
            modifier = Modifier
                .layoutId("image")
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "${stringResource(R.string.dollar)}${formatNumber(totalSum)}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.layoutId("sumText")
        )

        Toggle(
            options = listOf(stringResource(R.string.income), stringResource(R.string.expense)),
            selectedOption = categoryType,
            onOptionSelected = onCategoryTypeChange,
            modifier = Modifier.layoutId("toggle")
        )
    }
}


private fun startConstraintSet() = ConstraintSet {
    val statusBar = createRefFor("statusBar")
    val image = createRefFor("image")
    val sumText = createRefFor("sumText")
    val toggle = createRefFor("toggle")

    createVerticalChain(
        statusBar, image, sumText, toggle,
        chainStyle = ChainStyle.Spread
    )

    constrain(statusBar) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        bottom.linkTo(image.top)
        width = Dimension.matchParent
        height = Dimension.wrapContent
    }

    constrain(image) {
        top.linkTo(statusBar.bottom)
        bottom.linkTo(sumText.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        height = Dimension.wrapContent
        width = Dimension.matchParent
        alpha = 1f
    }

    constrain(sumText) {
        top.linkTo(image.bottom, margin = 15.dp)
        bottom.linkTo(toggle.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        alpha = 1f
    }

    constrain(toggle) {
        top.linkTo(sumText.bottom, margin = 15.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        alpha = 1f
    }
}

private fun endConstraintSet() = ConstraintSet {
    val statusBar = createRefFor("statusBar")
    val image = createRefFor("image")
    val sumText = createRefFor("sumText")
    val toggle = createRefFor("toggle")

    constrain(statusBar) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        width = Dimension.matchParent
        height = Dimension.wrapContent
    }

    constrain(image) {
        top.linkTo(statusBar.bottom)
        alpha = 0f
    }

    constrain(sumText) {
        top.linkTo(statusBar.bottom)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        alpha = 0f
    }

    constrain(toggle) {
        top.linkTo(statusBar.bottom, margin = 8.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        alpha = 1f
    }
}
