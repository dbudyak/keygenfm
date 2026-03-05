# Keygen FM - Android

24/7 chiptune/cracktro/demoscene radio player for Android. Streams from AzuraCast at `keygen-fm.kodatek.app`.

## Build

Requires JDK 17 (Gradle doesn't support JDK 25+):

```bash
cd lab/keygen-fm-android

# Build release APK
JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.18/libexec/openjdk.jdk/Contents/Home \
  ./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

Debug build:

```bash
JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.18/libexec/openjdk.jdk/Contents/Home \
  ./gradlew assembleDebug
```

## Install on device

```bash
adb install app/build/outputs/apk/release/app-release.apk
```

## Tech stack

- Kotlin + Jetpack Compose
- Hilt (DI)
- Media3 / ExoPlayer (audio streaming)
- Retrofit + kotlinx.serialization (API)
- Coil (image loading)

## Signing

Release keystore: `keystore/release.jks`
Config: `keystore.properties`
