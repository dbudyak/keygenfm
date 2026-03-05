package app.kodatek.keygenfm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import app.kodatek.keygenfm.ui.RadioViewModel
import app.kodatek.keygenfm.ui.components.LoadingStripes
import app.kodatek.keygenfm.ui.components.PixelPlayButton
import app.kodatek.keygenfm.ui.components.VolumeBar
import app.kodatek.keygenfm.ui.components.Win95Button
import app.kodatek.keygenfm.ui.components.Win95Divider
import app.kodatek.keygenfm.ui.components.Win95SunkenBox
import app.kodatek.keygenfm.ui.components.Win95Window
import app.kodatek.keygenfm.ui.theme.Win95Black
import app.kodatek.keygenfm.ui.theme.Win95ButtonShadow
import app.kodatek.keygenfm.ui.theme.Win95Desktop
import app.kodatek.keygenfm.ui.theme.Win95Text
import app.kodatek.keygenfm.ui.theme.Win95TitleBar

@Composable
fun PlayerScreen(
    viewModel: RadioViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Win95Desktop)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top loading stripes
        LoadingStripes(height = 12.dp, stripeCount = 7)

        Spacer(modifier = Modifier.weight(1f))

        // Main window
        Win95Window(
            title = "\u25A0 KEYGEN FM \u25A0",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            // Station name header
            Text(
                text = "Keygen-FM",
                style = MaterialTheme.typography.displaySmall,
                color = Win95Black,
            )

            Spacer(modifier = Modifier.height(12.dp))
            Win95Divider()
            Spacer(modifier = Modifier.height(12.dp))

            // Track info row: album art + title + progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                // Album art in sunken frame
                Win95SunkenBox(
                    modifier = Modifier.size(56.dp),
                ) {
                    if (state.currentTrack.artUrl.isNotEmpty()) {
                        AsyncImage(
                            model = state.currentTrack.artUrl,
                            contentDescription = "Album art",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Win95Black),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Track title + progress bar
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = state.currentTrack.title.ifEmpty { "---" },
                        style = MaterialTheme.typography.titleMedium,
                        color = Win95Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Progress bar
                    TrackProgressBar(
                        elapsed = state.currentTrack.elapsed,
                        duration = state.currentTrack.duration,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Win95Divider()
            Spacer(modifier = Modifier.height(12.dp))

            // Controls row: play button + volume
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Play/stop button
                PixelPlayButton(
                    isPlaying = state.isPlaying,
                    isBuffering = state.isBuffering,
                    onClick = viewModel::togglePlayback,
                    modifier = Modifier.size(36.dp),
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Volume: icon + slider
                Text(
                    text = "\uD83D\uDD08",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp),
                )

                VolumeBar(
                    volume = state.volume,
                    maxVolume = state.maxVolume,
                    onVolumeChange = viewModel::setVolume,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Win95Button(
                    text = "SONG HISTORY",
                    icon = "\u23F0",
                    onClick = { /* TODO */ },
                )
                Win95Button(
                    text = "PLAYLIST",
                    icon = "\u2B06",
                    onClick = { /* TODO */ },
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.6f))

        // Footer
        Text(
            text = "LISTENERS: ${state.listenerCount}",
            style = MaterialTheme.typography.labelMedium,
            color = Win95ButtonShadow,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Text(
            text = "\u00A9 KEYGEN FM \u2014 kodatek.app",
            style = MaterialTheme.typography.labelMedium,
            color = Win95ButtonShadow,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        // Bottom loading stripes
        LoadingStripes(height = 12.dp, stripeCount = 7)
    }
}

@Composable
private fun TrackProgressBar(
    elapsed: Int,
    duration: Int,
) {
    val fraction = if (duration > 0) (elapsed.toFloat() / duration).coerceIn(0f, 1f) else 0f

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = formatTime(elapsed),
            style = MaterialTheme.typography.labelMedium,
            color = Win95Text,
        )

        Spacer(modifier = Modifier.width(6.dp))

        // Progress bar (sunken track with blue fill)
        Win95SunkenBox(
            modifier = Modifier
                .weight(1f)
                .height(12.dp),
            backgroundColor = Win95Desktop,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (fraction > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .height(12.dp)
                            .background(Win95TitleBar)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = formatTime(duration),
            style = MaterialTheme.typography.labelMedium,
            color = Win95Text,
        )
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}
