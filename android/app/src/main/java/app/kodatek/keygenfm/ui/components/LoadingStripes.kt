package app.kodatek.keygenfm.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.kodatek.keygenfm.ui.theme.StripeColors

@Composable
fun LoadingStripes(
    modifier: Modifier = Modifier,
    stripeCount: Int = 8,
    height: Dp = 32.dp,
    speed: Int = 600,
) {
    val transition = rememberInfiniteTransition(label = "stripes")
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(speed, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "stripe_offset",
    )

    val colors = StripeColors
    val colorCount = colors.size

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val stripeHeight = size.height / stripeCount
        val shiftedIndex = (offset * colorCount).toInt()

        for (i in 0 until stripeCount) {
            val colorIndex = (i + shiftedIndex) % colorCount
            drawRect(
                color = colors[colorIndex],
                topLeft = Offset(0f, i * stripeHeight),
                size = Size(size.width, stripeHeight + 1f),
            )
        }
    }
}
