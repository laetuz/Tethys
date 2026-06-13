package id.neotica.tethys.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import id.neotica.tethys.ui.screen.characters.LittleFish
import id.neotica.tethys.ui.screen.characters.Shark
import id.neotica.tethys.ui.theme.TethysTheme
import kotlin.math.sin
import kotlin.random.Random
import kotlinx.coroutines.isActive

private const val FISH_COUNT = 10
private const val SHARK_FONT_SIZE = 5
private const val FISH_FONT_SIZE = 15
private const val RESPAWN_DELAY_FRAMES = 180
private const val BITE_DURATION_FRAMES = 40
private const val FISH_W = 14f
private const val FISH_H = 14f

private data class FishState(
    val x: Float,
    val y: Float,
    val velocityX: Float,
    val velocityY: Float,
    val alive: Boolean,
    val respawnTimer: Int = 0
)

@Composable
fun TethysScreen() {
    val density = LocalDensity.current
    var textSize by remember { mutableStateOf(IntSize.Zero) }
    var bitingTimer by remember { mutableFloatStateOf(0f) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val maxHeightPx = with(density) { maxHeight.toPx() }

        var x by remember { mutableFloatStateOf(maxWidthPx / 2f) }
        var y by remember { mutableFloatStateOf(maxHeightPx / 2f) }
        var velocityX by remember { mutableFloatStateOf(0.3f) }
        var velocityY by remember { mutableFloatStateOf(0.3f) }
        var time by remember { mutableFloatStateOf(0f) }

        var fish by remember {
            val fishSize = maxWidthPx / (FISH_COUNT + 1)
            mutableStateOf(
                List(FISH_COUNT) { i ->
                    FishState(
                        x = fishSize * (i + 1),
                        y = maxHeightPx * (0.2f + 0.6f * i / FISH_COUNT),
                        velocityX = -(0.6f + Random.nextFloat() * 0.6f),
                        velocityY = (Random.nextFloat() - 0.5f) * 0.4f,
                        alive = true
                    )
                }
            )
        }

        LaunchedEffect(Unit) {
            while (isActive) {
                withFrameNanos { _ ->
                    val tw = textSize.width
                    val th = textSize.height

                    time += 1f
                    x += velocityX
                    y += velocityY + sin(time * 0.08f) * 0.5f

                    if (x + tw > maxWidthPx) {
                        velocityX = -velocityX
                        x = (maxWidthPx - tw).coerceAtLeast(0f)
                    } else if (x < 0f) {
                        velocityX = -velocityX
                        x = 0f
                    }

                    if (y + th > maxHeightPx) {
                        velocityY = -velocityY
                        y = (maxHeightPx - th).coerceAtLeast(0f)
                    } else if (y < 0f) {
                        velocityY = -velocityY
                        y = 0f
                        y = 0f
                    }

                    var hitFish = false
                    val updated = fish.map { f ->
                        if (!f.alive) {
                            val next = f.copy(respawnTimer = f.respawnTimer - 1)
                            if (next.respawnTimer <= 0) {
                                val edge = Random.nextInt(4)
                                val (fx, fy) = when (edge) {
                                    0 -> Pair(Random.nextFloat() * maxWidthPx, -FISH_H)
                                    1 -> Pair(Random.nextFloat() * maxWidthPx, maxHeightPx + FISH_H)
                                    2 -> Pair(-FISH_W, Random.nextFloat() * maxHeightPx)
                                    else -> Pair(maxWidthPx + FISH_W, Random.nextFloat() * maxHeightPx)
                                }
                                FishState(
                                    x = fx, y = fy,
                                    velocityX = -(0.6f + Random.nextFloat() * 0.6f),
                                    velocityY = (Random.nextFloat() - 0.5f) * 0.4f,
                                    alive = true
                                )
                            } else {
                                next
                            }
                        } else {
                            var fx = f.x + f.velocityX
                            var fy = f.y + f.velocityY
                            var fvx = f.velocityX
                            var fvy = f.velocityY

                            if (fx + FISH_W > maxWidthPx || fx < 0f) fvx = -fvx
                            if (fy + FISH_H > maxHeightPx || fy < 0f) fvy = -fvy

                            f.copy(x = fx, y = fy, velocityX = fvx, velocityY = fvy)
                        }
                    }

                    val collided = updated.map { f ->
                        if (!f.alive) return@map f
                        if (x < f.x + FISH_W && x + tw > f.x &&
                            y < f.y + FISH_H && y + th > f.y
                        ) {
                            hitFish = true
                            f.copy(alive = false, respawnTimer = RESPAWN_DELAY_FRAMES)
                        } else {
                            f
                        }
                    }

                    fish = collided
                    if (hitFish) bitingTimer = BITE_DURATION_FRAMES.toFloat()
                    if (bitingTimer > 0f) bitingTimer -= 1f
                }
            }
        }

        Shark(
            x = x,
            y = y,
            velocityX = velocityX,
            isBiting = bitingTimer > 0f,
            onSizeChanged = { textSize = it },
            fontSize = SHARK_FONT_SIZE.sp
        )

        fish.forEach { f ->
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

@Preview(showBackground = true)
@Composable
fun TethysScreenPreview() {
    TethysTheme {
        TethysScreen()
    }
}
