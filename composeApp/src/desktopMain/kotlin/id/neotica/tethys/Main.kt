package id.neotica.tethys

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import id.neotica.tethys.di.appModule
import id.neotica.tethys.feature.tethys.TethysScreen
import id.neotica.tethys.ui.theme.TethysTheme
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(appModule)
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Tethys",
            state = rememberWindowState(width = 800.dp, height = 600.dp)
        ) {
            TethysTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    TethysScreen()
                }
            }
        }
    }
}
