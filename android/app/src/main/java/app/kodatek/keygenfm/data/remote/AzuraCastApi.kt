package app.kodatek.keygenfm.data.remote

import app.kodatek.keygenfm.data.remote.dto.NowPlayingDto
import retrofit2.http.GET

interface AzuraCastApi {
    @GET("api/nowplaying/keygen-fm")
    suspend fun getNowPlaying(): NowPlayingDto
}
