package app.kodatek.keygenfm.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NowPlayingDto(
    val station: StationDto? = null,
    val listeners: ListenersDto? = null,
    @SerialName("now_playing") val nowPlaying: NowPlayingTrackDto? = null,
    @SerialName("playing_next") val playingNext: PlayingNextDto? = null,
    @SerialName("is_online") val isOnline: Boolean = false,
)

@Serializable
data class StationDto(
    val name: String = "",
    val description: String = "",
)

@Serializable
data class ListenersDto(
    val total: Int = 0,
    val unique: Int = 0,
    val current: Int = 0,
)

@Serializable
data class NowPlayingTrackDto(
    val elapsed: Int = 0,
    val remaining: Int = 0,
    val duration: Int = 0,
    val song: SongDto? = null,
)

@Serializable
data class PlayingNextDto(
    val song: SongDto? = null,
)

@Serializable
data class SongDto(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val art: String = "",
    val text: String = "",
)
