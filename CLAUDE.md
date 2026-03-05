# Keygen FM

A 24/7 chiptune/cracktro/demoscene internet radio station. The stream runs on AzuraCast at `https://keygen-fm.kodatek.app`. This repo contains client apps.

## Repository Structure

```
keygenfm/
└── android/    # Android app (Kotlin + Jetpack Compose)
```

More clients (web, iOS, desktop) may be added here in the future.

## Radio Backend

The station runs on **AzuraCast** (self-hosted). You don't manage the backend from this repo.

| Endpoint | URL |
|----------|-----|
| Stream (MP3) | `https://keygen-fm.kodatek.app/listen/keygen-fm/radio.mp3` |
| Now Playing API | `https://keygen-fm.kodatek.app/api/nowplaying/keygen-fm` |
| AzuraCast admin | `https://keygen-fm.kodatek.app` |

The Now Playing API is standard AzuraCast — returns station info, current track (title, artist, album, art URL, elapsed/duration), listener count, and next track. No auth required.

---

## Android App (`android/`)

A single-screen radio player with a Windows 95 aesthetic. Streams audio, shows now-playing info, and controls playback and volume.

### Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| DI | Hilt |
| Audio | Media3 / ExoPlayer (`MediaSessionService`) |
| Networking | Retrofit + kotlinx.serialization |
| Image loading | Coil |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 35 |

### Project Structure

```
android/
├── app/
│   └── src/main/java/app/kodatek/keygenfm/
│       ├── KeygenFmApp.kt              # Hilt application class
│       ├── MainActivity.kt             # Single activity, hosts PlayerScreen
│       ├── data/
│       │   ├── di/NetworkModule.kt     # Hilt module: Retrofit + AzuraCastApi
│       │   └── remote/
│       │       ├── AzuraCastApi.kt     # Retrofit interface (GET nowplaying)
│       │       └── dto/NowPlayingDto.kt # Deserialized API response
│       ├── media/
│       │   └── RadioPlaybackService.kt # MediaSessionService wrapping ExoPlayer
│       ├── ui/
│       │   ├── RadioViewModel.kt       # State + MediaController + API polling
│       │   ├── screens/PlayerScreen.kt # Main UI composable
│       │   ├── components/             # Win95-styled UI components
│       │   │   ├── Win95Components.kt  # Window, button, divider, sunken box
│       │   │   ├── PixelPlayButton.kt  # Play/stop toggle button
│       │   │   ├── VolumeBar.kt        # Volume slider
│       │   │   └── LoadingStripes.kt   # Animated stripes (top/bottom decoration)
│       │   └── theme/                  # Colors, typography, theme
│       └── util/Constants.kt           # STREAM_URL, API_BASE_URL, polling interval
├── keystore/
│   └── release.jks                    # Release signing keystore (keep safe)
├── keystore.properties                # Keystore credentials (not committed)
└── build.gradle.kts
```

### Architecture

```
PlayerScreen (Compose)
    └── RadioViewModel (HiltViewModel)
            ├── MediaController ──────────► RadioPlaybackService (foreground)
            │                                       └── ExoPlayer (streams MP3)
            └── AzuraCastApi (Retrofit)
                    └── polls /api/nowplaying/keygen-fm every 15 seconds
```

- **`RadioPlaybackService`** is a `MediaSessionService` — runs as a foreground service, survives app backgrounding, shows a persistent notification with playback controls (play/pause + custom volume up/down buttons).
- **`RadioViewModel`** connects to the service via `MediaController`, listens for player state changes, and separately polls the AzuraCast API for track metadata (title, artist, art, progress).
- **`PlayerScreen`** collects `RadioUiState` from the ViewModel and renders everything.

### UI State (`RadioUiState`)

```kotlin
data class RadioUiState(
    val isPlaying: Boolean,
    val isBuffering: Boolean,
    val isConnected: Boolean,      // MediaController connected to service
    val currentTrack: TrackInfo,   // title, artist, artUrl, album, elapsed, duration
    val listenerCount: Int,
    val isOnline: Boolean,         // AzuraCast station online status
    val volume: Int,
    val maxVolume: Int,
    val error: String?,
)
```

### Design: Windows 95 Aesthetic

The UI is intentionally styled after Windows 95 — grey backgrounds, beveled borders, title bars, sunken boxes. All Win95 components are custom-drawn in Compose (`Win95Components.kt`). Colors are defined in `theme/Color.kt`:

- `Win95Desktop` — grey background
- `Win95TitleBar` — dark blue title bars
- `Win95Black`, `Win95Text`, `Win95ButtonShadow` — text colors
- `Win95ButtonFace`, `Win95ButtonHighlight`, `Win95ButtonShadow` — 3D bevel effect

### Constants (`util/Constants.kt`)

```kotlin
STREAM_URL = "https://keygen-fm.kodatek.app/listen/keygen-fm/radio.mp3"
API_BASE_URL = "https://keygen-fm.kodatek.app/"
NOW_PLAYING_ENDPOINT = "api/nowplaying/keygen-fm"
POLLING_INTERVAL_MS = 15_000L
```

### Building

Requires **JDK 17** — Gradle does not support JDK 21+.

```bash
cd android

# Release APK
JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.18/libexec/openjdk.jdk/Contents/Home \
  ./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk

# Debug APK
JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.18/libexec/openjdk.jdk/Contents/Home \
  ./gradlew assembleDebug

# Install on connected device
adb install app/build/outputs/apk/release/app-release.apk
```

### Signing

Release builds are signed with `keystore/release.jks`. Credentials go in `keystore.properties` (not committed):

```
storeFile=../keystore/release.jks
storePassword=...
keyAlias=...
keyPassword=...
```

### App Versions

- `versionCode = 2`, `versionName = "1.0.1"`
- Package: `app.kodatek.keygenfm`

### Known TODOs

- "SONG HISTORY" button — not implemented (stub in `PlayerScreen.kt`)
- "PLAYLIST" button — not implemented (stub in `PlayerScreen.kt`)
