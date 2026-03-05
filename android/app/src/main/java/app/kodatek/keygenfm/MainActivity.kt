package app.kodatek.keygenfm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import app.kodatek.keygenfm.ui.screens.PlayerScreen
import app.kodatek.keygenfm.ui.theme.KeygenFmTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeygenFmTheme {
                PlayerScreen()
            }
        }
    }
}
