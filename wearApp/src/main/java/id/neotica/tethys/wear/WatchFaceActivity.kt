package id.neotica.tethys.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import id.neotica.tethys.ui.screen.characters.Shark
import id.neotica.tethys.ui.theme.TethysTheme
import kotlinx.coroutines.isActive
import kotlin.math.sin

class WatchFaceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TethysTheme(content = { WatchFace() })
        }
    }
}

@Composable
fun WatchFace() {
    var frameCount by remember { mutableFloatStateOf(0f) }
    var textSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameNanos { _ ->
                frameCount += 1f
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        val bobY = sin(frameCount * 0.05f) * 4f
        val fontSize = (widthPx / 80).coerceAtLeast(3f).sp
        Shark(
            x = (widthPx - textSize.width) / 2f,
            y = (heightPx - textSize.height) / 2f + bobY,
            velocityX = 1f,
            isBiting = false,
            frameCount = frameCount,
            onBite = {},
            onSizeChanged = { textSize = it },
            fontSize = fontSize,
            modifier = Modifier.fillMaxSize()
        )
    }
}
