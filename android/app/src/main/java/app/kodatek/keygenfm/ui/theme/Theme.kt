package app.kodatek.keygenfm.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val KeygenFmColorScheme = lightColorScheme(
    primary = Win95TitleBar,
    onPrimary = Win95TitleText,
    secondary = Win95Blue,
    onSecondary = Win95TitleText,
    background = Win95Desktop,
    onBackground = Win95Text,
    surface = Win95ButtonFace,
    onSurface = Win95Text,
    surfaceVariant = Win95Desktop,
    onSurfaceVariant = Win95Text,
    error = StripeColors[0],
)

@Composable
fun KeygenFmTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KeygenFmColorScheme,
        typography = KeygenFmTypography,
        content = content,
    )
}
