package app.kodatek.keygenfm.ui

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import app.kodatek.keygenfm.data.remote.AzuraCastApi
import app.kodatek.keygenfm.data.remote.dto.NowPlayingDto
import app.kodatek.keygenfm.media.RadioPlaybackService
import app.kodatek.keygenfm.util.Constants
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrackInfo(
    val title: String = "",
    val artist: String = "",
    val artUrl: String = "",
    val album: String = "",
    val elapsed: Int = 0,
    val duration: Int = 0,
)

data class RadioUiState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val isConnected: Boolean = false,
    val currentTrack: TrackInfo = TrackInfo(),
    val listenerCount: Int = 0,
    val isOnline: Boolean = false,
    val volume: Int = 0,
    val maxVolume: Int = 15,
    val error: String? = null,
)

@HiltViewModel
class RadioViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: AzuraCastApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RadioUiState())
    val uiState: StateFlow<RadioUiState> = _uiState.asStateFlow()

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    init {
        connectToService()
        startPollingNowPlaying()
    }

    private fun connectToService() {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, RadioPlaybackService::class.java)
        )
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync().also { future ->
            future.addListener({
                try {
                    mediaController = future.get()
                    setupPlayerListener()
                    _uiState.value = _uiState.value.copy(isConnected = true)
                    syncPlayerState()
                    syncVolume()
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(error = "Failed to connect to player")
                }
            }, MoreExecutors.directExecutor())
        }
    }

    private fun setupPlayerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                _uiState.value = _uiState.value.copy(
                    isBuffering = playbackState == Player.STATE_BUFFERING
                )
            }

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                syncPlayerState()
            }

            override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
                syncVolume()
            }
        })
    }

    private fun syncPlayerState() {
        mediaController?.let { controller ->
            _uiState.value = _uiState.value.copy(
                isPlaying = controller.isPlaying,
                isBuffering = controller.playbackState == Player.STATE_BUFFERING,
            )
        }
    }

    private fun syncVolume() {
        mediaController?.let { controller ->
            _uiState.value = _uiState.value.copy(
                volume = controller.deviceVolume,
                maxVolume = controller.deviceInfo.maxVolume.coerceAtLeast(1),
            )
        }
    }

    private fun startPollingNowPlaying() {
        viewModelScope.launch {
            while (true) {
                try {
                    val nowPlaying = api.getNowPlaying()
                    updateFromNowPlaying(nowPlaying)
                } catch (_: Exception) {
                    // Silently continue polling on error
                }
                delay(Constants.POLLING_INTERVAL_MS)
            }
        }
    }

    private fun updateFromNowPlaying(dto: NowPlayingDto) {
        val song = dto.nowPlaying?.song
        _uiState.value = _uiState.value.copy(
            currentTrack = TrackInfo(
                title = song?.title ?: "",
                artist = song?.artist ?: "",
                artUrl = song?.art ?: "",
                album = song?.album ?: "",
                elapsed = dto.nowPlaying?.elapsed ?: 0,
                duration = dto.nowPlaying?.duration ?: 0,
            ),
            listenerCount = dto.listeners?.current ?: 0,
            isOnline = dto.isOnline,
            error = null,
        )
    }

    fun togglePlayback() {
        mediaController?.let { controller ->
            if (controller.isPlaying) {
                controller.pause()
            } else {
                controller.play()
            }
        }
    }

    fun setVolume(level: Int) {
        mediaController?.let { controller ->
            val clamped = level.coerceIn(0, controller.deviceInfo.maxVolume)
            controller.setDeviceVolume(clamped, C.VOLUME_FLAG_SHOW_UI)
            syncVolume()
        }
    }

    fun volumeUp() {
        mediaController?.let { controller ->
            controller.increaseDeviceVolume(C.VOLUME_FLAG_SHOW_UI)
            syncVolume()
        }
    }

    fun volumeDown() {
        mediaController?.let { controller ->
            controller.decreaseDeviceVolume(C.VOLUME_FLAG_SHOW_UI)
            syncVolume()
        }
    }

    override fun onCleared() {
        super.onCleared()
        controllerFuture?.let { MediaController.releaseFuture(it) }
        mediaController = null
    }
}
