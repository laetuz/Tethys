package id.neotica.tethys.feature.tethys

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import id.neotica.tethys.ui.screen.characters.LittleFish
import id.neotica.tethys.ui.screen.characters.Shark
import kotlinx.coroutines.isActive
import org.koin.androidx.compose.koinViewModel

private const val SHARK_FONT_SIZE = 5
private const val FISH_FONT_SIZE = 15

@Composable
fun TethysScreen(viewModel: TethysViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        viewModel.setBounds(
            widthPx = with(density) { maxWidth.toPx() },
            heightPx = with(density) { maxHeight.toPx() }
        )

        LaunchedEffect(Unit) {
            while (isActive) {
                withFrameNanos { _ ->
                    viewModel.tick()
                }
            }
        }

        Shark(
            x = state.sharkX,
            y = state.sharkY,
            velocityX = state.velocityX,
            isBiting = state.isBiting,
            frameCount = state.frameCount,
            onBite = { viewModel.bite() },
            onSizeChanged = { viewModel.updateTextSize(it) },
            fontSize = SHARK_FONT_SIZE.sp
        )

        state.fish.forEach { f ->
            if (f.alive) {
                LittleFish(
                    x = f.x,
                    y = f.y,
                    fontSize = FISH_FONT_SIZE.sp
                )
            }
        }
    }
}
