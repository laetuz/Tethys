package id.neotica.tethys.feature.tethys

import androidx.compose.ui.unit.IntSize

data class TethysUiState(
    val sharkX: Float = 0f,
    val sharkY: Float = 0f,
    val velocityX: Float = 1.3f,
    val velocityY: Float = 0.3f,
    val bitingTimer: Float = 0f,
    val frameCount: Float = 0f,
    val fish: List<FishState> = emptyList(),
    val textSize: IntSize = IntSize.Zero
) {
    val isBiting: Boolean get() = bitingTimer > 0f
}

data class FishState(
    val x: Float,
    val y: Float,
    val velocityX: Float,
    val velocityY: Float,
    val alive: Boolean,
    val respawnTimer: Int = 0
)
