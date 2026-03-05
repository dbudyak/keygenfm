package app.kodatek.keygenfm.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import app.kodatek.keygenfm.ui.theme.Win95Black
import app.kodatek.keygenfm.ui.theme.Win95ButtonFace

@Composable
fun PixelPlayButton(
    isPlaying: Boolean,
    isBuffering: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconColor = Win95Black

    val transition = rememberInfiniteTransition(label = "buffering")
    val bufferAlpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "buffer_alpha",
    )

    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .win95Raised(1.dp)
            .background(Win95ButtonFace)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(2.dp)
    ) {
        val padding = size.width * 0.25f
        val innerSize = size.width - padding * 2
        val alpha = if (isBuffering) bufferAlpha else 1f

        if (isPlaying) {
            drawStopIcon(padding, innerSize, iconColor.copy(alpha = alpha))
        } else {
            drawPlayIcon(padding, innerSize, iconColor.copy(alpha = alpha))
        }
    }
}

private fun DrawScope.drawPlayIcon(padding: Float, innerSize: Float, color: Color) {
    val path = Path().apply {
        moveTo(padding + innerSize * 0.15f, padding)
        lineTo(padding + innerSize, padding + innerSize / 2)
        lineTo(padding + innerSize * 0.15f, padding + innerSize)
        close()
    }
    drawPath(path, color)
}

private fun DrawScope.drawStopIcon(padding: Float, innerSize: Float, color: Color) {
    val inset = innerSize * 0.1f
    drawRect(
        color = color,
        topLeft = Offset(padding + inset, padding + inset),
        size = Size(innerSize - inset * 2, innerSize - inset * 2),
    )
}
