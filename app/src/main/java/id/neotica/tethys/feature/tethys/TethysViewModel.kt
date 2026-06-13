package id.neotica.tethys.feature.tethys

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.sin
import kotlin.random.Random

private const val FISH_COUNT = 10
private const val RESPAWN_DELAY_FRAMES = 180
private const val BITE_DURATION_FRAMES = 40
private const val FISH_W = 14f
private const val FISH_H = 14f

class TethysViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TethysUiState())
    val uiState: StateFlow<TethysUiState> = _uiState.asStateFlow()

    private var maxWidthPx = 0f
    private var maxHeightPx = 0f
    private var initialized = false

    fun setBounds(widthPx: Float, heightPx: Float) {
        maxWidthPx = widthPx
        maxHeightPx = heightPx
        if (!initialized && widthPx > 0 && heightPx > 0) {
            initialized = true
            val spacing = maxWidthPx / (FISH_COUNT + 1)
            _uiState.value = _uiState.value.copy(
                sharkX = maxWidthPx / 2f,
                sharkY = maxHeightPx / 2f,
                fish = List(FISH_COUNT) { i ->
                    FishState(
                        x = spacing * (i + 1),
                        y = maxHeightPx * (0.2f + 0.6f * i / FISH_COUNT),
                        velocityX = -(0.6f + Random.nextFloat() * 0.6f),
                        velocityY = (Random.nextFloat() - 0.5f) * 0.4f,
                        alive = true
                    )
                }
            )
        }
    }

    fun tick() {
        val s = _uiState.value
        if (!initialized) return

        val tw = s.textSize.width
        val th = s.textSize.height

        val frame = s.frameCount + 1f
        var x = s.sharkX + s.velocityX
        var y = s.sharkY + s.velocityY + sin(frame * 0.08f) * 0.5f
        var vx = s.velocityX
        var vy = s.velocityY

        if (x + tw > maxWidthPx) {
            vx = -vx
            x = (maxWidthPx - tw).coerceAtLeast(0f)
        } else if (x < 0f) {
            vx = -vx
            x = 0f
        }

        if (y + th > maxHeightPx) {
            vy = -vy
            y = (maxHeightPx - th).coerceAtLeast(0f)
        } else if (y < 0f) {
            vy = -vy
            y = 0f
        }

        var hitFish = false
        val moved = s.fish.map { f ->
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

        val collided = moved.map { f ->
            if (!f.alive) return@map f
            if (x < f.x + FISH_W && x + tw > f.x && y < f.y + FISH_H && y + th > f.y) {
                hitFish = true
                f.copy(alive = false, respawnTimer = RESPAWN_DELAY_FRAMES)
            } else {
                f
            }
        }

        val timer = if (hitFish) BITE_DURATION_FRAMES.toFloat()
            else (s.bitingTimer - 1f).coerceAtLeast(0f)

        _uiState.value = s.copy(
            sharkX = x,
            sharkY = y,
            velocityX = vx,
            velocityY = vy,
            frameCount = frame,
            fish = collided,
            bitingTimer = timer
        )
    }

    fun bite() {
        _uiState.value = _uiState.value.copy(bitingTimer = BITE_DURATION_FRAMES.toFloat())
    }

    fun updateTextSize(size: IntSize) {
        _uiState.value = _uiState.value.copy(textSize = size)
    }
}
