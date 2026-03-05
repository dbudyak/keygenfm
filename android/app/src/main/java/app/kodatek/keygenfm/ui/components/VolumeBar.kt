package app.kodatek.keygenfm.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import app.kodatek.keygenfm.ui.theme.Win95Black
import app.kodatek.keygenfm.ui.theme.Win95ButtonDarkShadow
import app.kodatek.keygenfm.ui.theme.Win95ButtonFace
import app.kodatek.keygenfm.ui.theme.Win95ButtonHighlight
import app.kodatek.keygenfm.ui.theme.Win95ButtonShadow

@Composable
fun VolumeBar(
    volume: Int,
    maxVolume: Int,
    onVolumeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val segments = maxVolume.coerceAtLeast(1)

    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .pointerInput(segments) {
                    detectTapGestures { offset ->
                        val newVol = ((offset.x / size.width) * segments)
                            .toInt()
                            .coerceIn(0, segments)
                        onVolumeChange(newVol)
                    }
                }
                .pointerInput(segments) {
                    detectHorizontalDragGestures { change, _ ->
                        change.consume()
                        val newVol = ((change.position.x / size.width) * segments)
                            .toInt()
                            .coerceIn(0, segments)
                        onVolumeChange(newVol)
                    }
                }
        ) {
            val trackHeight = 6.dp.toPx()
            val trackY = (size.height - trackHeight) / 2
            val thumbWidth = 8.dp.toPx()
            val thumbHeight = size.height

            // Sunken track groove
            drawRect(Win95ButtonShadow, Offset(0f, trackY), Size(size.width, 1.dp.toPx()))
            drawRect(Win95ButtonDarkShadow, Offset(0f, trackY + 1.dp.toPx()), Size(size.width, trackHeight - 2.dp.toPx()))
            drawRect(Win95ButtonHighlight, Offset(0f, trackY + trackHeight - 1.dp.toPx()), Size(size.width, 1.dp.toPx()))

            // Fill the track up to thumb position
            val fraction = volume.toFloat() / segments
            val fillWidth = fraction * (size.width - thumbWidth)
            if (fillWidth > 0) {
                drawRect(
                    Win95Black,
                    Offset(0f, trackY + 1.dp.toPx()),
                    Size(fillWidth, trackHeight - 2.dp.toPx()),
                )
            }

            // Thumb (raised rectangle)
            val thumbX = fillWidth
            // Thumb face
            drawRect(Win95ButtonFace, Offset(thumbX, 0f), Size(thumbWidth, thumbHeight))
            // Thumb highlight (top + left)
            drawRect(Win95ButtonHighlight, Offset(thumbX, 0f), Size(thumbWidth, 1.dp.toPx()))
            drawRect(Win95ButtonHighlight, Offset(thumbX, 0f), Size(1.dp.toPx(), thumbHeight))
            // Thumb shadow (bottom + right)
            drawRect(Win95ButtonDarkShadow, Offset(thumbX, thumbHeight - 1.dp.toPx()), Size(thumbWidth, 1.dp.toPx()))
            drawRect(Win95ButtonDarkShadow, Offset(thumbX + thumbWidth - 1.dp.toPx(), 0f), Size(1.dp.toPx(), thumbHeight))
        }
    }
}
