package id.neotica.tethys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import id.neotica.tethys.feature.tethys.TethysScreen
import id.neotica.tethys.ui.theme.TethysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TethysTheme {
                TethysScreen()
            }
        }
    }
}
