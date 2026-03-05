package app.kodatek.keygenfm.media

import android.content.Intent
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import app.kodatek.keygenfm.R
import app.kodatek.keygenfm.util.Constants
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class RadioPlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    companion object {
        const val ACTION_VOLUME_DOWN = "app.kodatek.keygenfm.VOLUME_DOWN"
        const val ACTION_VOLUME_UP = "app.kodatek.keygenfm.VOLUME_UP"
        val COMMAND_VOLUME_DOWN = SessionCommand(ACTION_VOLUME_DOWN, Bundle.EMPTY)
        val COMMAND_VOLUME_UP = SessionCommand(ACTION_VOLUME_UP, Bundle.EMPTY)
    }

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true
            )
            .setHandleAudioBecomingNoisy(true)
            .setDeviceVolumeControlEnabled(true)
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(Constants.STREAM_URL)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Keygen FM")
                    .setArtist("24/7 Chiptune Radio")
                    .build()
            )
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()

        val volumeDownButton = CommandButton.Builder(CommandButton.ICON_VOLUME_DOWN)
            .setDisplayName("Volume Down")
            .setSessionCommand(COMMAND_VOLUME_DOWN)
            .build()

        val volumeUpButton = CommandButton.Builder(CommandButton.ICON_VOLUME_UP)
            .setDisplayName("Volume Up")
            .setSessionCommand(COMMAND_VOLUME_UP)
            .build()

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(SessionCallback())
            .setCustomLayout(ImmutableList.of(volumeDownButton, volumeUpButton))
            .build()
    }

    private inner class SessionCallback : MediaSession.Callback {
        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                .add(COMMAND_VOLUME_DOWN)
                .add(COMMAND_VOLUME_UP)
                .build()

            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            when (customCommand.customAction) {
                ACTION_VOLUME_DOWN -> session.player.decreaseDeviceVolume(C.VOLUME_FLAG_SHOW_UI)
                ACTION_VOLUME_UP -> session.player.increaseDeviceVolume(C.VOLUME_FLAG_SHOW_UI)
            }
            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player
        if (player == null || !player.playWhenReady || player.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }
}
