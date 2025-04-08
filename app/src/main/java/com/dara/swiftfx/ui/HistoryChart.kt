package com.dara.swiftfx.ui

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import com.dara.swiftfx.ui.theme.BlueDark
import com.dara.swiftfx.ui.theme.GraphBlue
import com.dara.swiftfx.ui.theme.LightGrey
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties

@Composable
fun HistoryChart(
    dates: List<String>,
    rates: List<Double>
) {
    Column(
        Modifier
            .height(500.dp)
            .background(
                color = BlueDark,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(vertical = 32.dp, horizontal = 24.dp)

    ) {
        LineChart(
            modifier = Modifier.fillMaxHeight(0.7f),
            data = remember {
                listOf(
                    Line(
                        label = "",
                        values = rates,
                        color = SolidColor(Transparent),
                        firstGradientFillColor = GraphBlue,
                        secondGradientFillColor = BlueDark,
                        strokeAnimationSpec = tween(500, easing = EaseInOutCubic),
                        gradientAnimationDelay = 500,
                        drawStyle = DrawStyle.Fill,
                    )
                )
            },
            zeroLineProperties = ZeroLineProperties(enabled = true, color = SolidColor(Red)),
            gridProperties = GridProperties(enabled = false),
            labelProperties = LabelProperties(
                enabled = true,
                labels = dates,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = LightGrey)
            ),
            indicatorProperties = HorizontalIndicatorProperties(enabled = false),
            animationMode = AnimationMode.Together(delayBuilder = { it * 500L })
        )
        Spacer(Modifier.height(32.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Get rate alerts straight to your email inbox",
            color = White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            textDecoration = Underline
        )
    }
}
