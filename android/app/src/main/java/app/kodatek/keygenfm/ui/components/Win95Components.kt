package app.kodatek.keygenfm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.kodatek.keygenfm.ui.theme.Win95Black
import app.kodatek.keygenfm.ui.theme.Win95ButtonDarkShadow
import app.kodatek.keygenfm.ui.theme.Win95ButtonFace
import app.kodatek.keygenfm.ui.theme.Win95ButtonHighlight
import app.kodatek.keygenfm.ui.theme.Win95ButtonShadow
import app.kodatek.keygenfm.ui.theme.Win95TitleBar
import app.kodatek.keygenfm.ui.theme.Win95TitleText

/**
 * Win95 raised 3D border effect (highlight top-left, shadow bottom-right).
 */
fun Modifier.win95Raised(width: Dp = 2.dp): Modifier = this.drawWithContent {
    drawContent()
    val bw = width.toPx()
    // Highlight (top and left)
    drawRect(Win95ButtonHighlight, Offset(0f, 0f), Size(size.width, bw))
    drawRect(Win95ButtonHighlight, Offset(0f, 0f), Size(bw, size.height))
    // Shadow (bottom and right)
    drawRect(Win95ButtonDarkShadow, Offset(0f, size.height - bw), Size(size.width, bw))
    drawRect(Win95ButtonDarkShadow, Offset(size.width - bw, 0f), Size(bw, size.height))
}

/**
 * Win95 sunken 3D border effect (shadow top-left, highlight bottom-right).
 */
fun Modifier.win95Sunken(width: Dp = 2.dp): Modifier = this.drawWithContent {
    drawContent()
    val bw = width.toPx()
    // Shadow (top and left)
    drawRect(Win95ButtonShadow, Offset(0f, 0f), Size(size.width, bw))
    drawRect(Win95ButtonShadow, Offset(0f, 0f), Size(bw, size.height))
    // Highlight (bottom and right)
    drawRect(Win95ButtonHighlight, Offset(0f, size.height - bw), Size(size.width, bw))
    drawRect(Win95ButtonHighlight, Offset(size.width - bw, 0f), Size(bw, size.height))
}

/**
 * Classic Win95 window with blue title bar and 3D border.
 */
@Composable
fun Win95Window(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .drawWithContent {
                drawContent()
                // Black outer border
                drawRect(Win95Black, style = Stroke(2.dp.toPx()))
            }
            .win95Raised()
            .background(Win95ButtonFace)
            .padding(4.dp)
    ) {
        // Title bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Win95TitleBar)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = Win95TitleText,
                textAlign = TextAlign.Center,
            )
        }

        // Content area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            content = content,
        )
    }
}

/**
 * Classic Win95 raised button.
 */
@Composable
fun Win95Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: String? = null,
) {
    Row(
        modifier = modifier
            .win95Raised(1.dp)
            .background(Win95ButtonFace)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (icon != null) {
            Text(
                text = icon,
                style = MaterialTheme.typography.labelMedium,
                color = Win95TitleBar,
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = Win95Black,
        )
    }
}

/**
 * Win95-style groove divider (sunken horizontal line).
 */
@Composable
fun Win95Divider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
            .drawWithContent {
                drawRect(Win95ButtonShadow, Offset(0f, 0f), Size(size.width, 1.dp.toPx()))
                drawRect(Win95ButtonHighlight, Offset(0f, 1.dp.toPx()), Size(size.width, 1.dp.toPx()))
            }
    )
}

/**
 * Win95-style sunken container (for progress bars, text fields, album art).
 */
@Composable
fun Win95SunkenBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Win95ButtonFace,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .win95Sunken()
            .background(backgroundColor)
            .padding(2.dp),
    ) {
        content()
    }
}
