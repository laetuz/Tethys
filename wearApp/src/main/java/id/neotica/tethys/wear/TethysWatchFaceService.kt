package id.neotica.tethys.wear

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema
import id.neotica.tethys.ui.screen.characters.SHARK_NORMAL
import id.neotica.tethys.ui.screen.characters.SHARK_NORMAL2
import java.time.ZonedDateTime
import kotlin.math.sin

class TethysWatchFaceService : WatchFaceService() {

    override fun createUserStyleSchema(): UserStyleSchema {
        return UserStyleSchema(emptyList())
    }

    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {
        return ComplicationSlotsManager(
            emptyList(),
            currentUserStyleRepository
        )
    }

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        try {
            val renderer = object : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
                surfaceHolder,
                currentUserStyleRepository,
                watchState,
                CanvasType.HARDWARE,
                16L,
                false
            ) {
                override suspend fun createSharedAssets(): SharedAssets {
                    return object : SharedAssets {
                        override fun onDestroy() {}
                    }
                }

                override fun render(
                    canvas: Canvas,
                    bounds: Rect,
                    zonedDateTime: ZonedDateTime,
                    sharedAssets: Renderer.SharedAssets
                ) {
                    canvas.drawColor(Color.BLACK)
                    val paint = Paint().apply {
                        color = Color.GREEN
                        typeface = Typeface.MONOSPACE
                        isAntiAlias = true
                    }

                    val epochMillis = zonedDateTime.toInstant().toEpochMilli()
                    val swimFrame = (epochMillis / 400) % 2 == 0L
                    val bobOffset = sin(epochMillis * 0.005).toFloat() * 4f

                    val art = if (swimFrame) SHARK_NORMAL else SHARK_NORMAL2
                    val lines = art.lines()
                    val maxLineLength = lines.maxOf { it.length }
                    paint.textSize = bounds.width().toFloat() / maxLineLength

                    val lineHeight = paint.textSize * 1.2f
                    val totalHeight = lines.size * lineHeight
                    val startY = bounds.centerY() - totalHeight / 2f + bobOffset

                    lines.forEachIndexed { i, line ->
                        canvas.drawText(
                            line,
                            bounds.centerX() - paint.measureText(line) / 2f,
                            startY + (i + 1) * lineHeight,
                            paint
                        )
                    }
                }

                override fun renderHighlightLayer(
                    canvas: Canvas,
                    bounds: Rect,
                    zonedDateTime: ZonedDateTime,
                    sharedAssets: Renderer.SharedAssets
                ) {}
            }

            return WatchFace(0, renderer)
        } catch (e: Exception) {
            Log.e("TethysWatchFace", "Failed to create watch face", e)
            throw e
        }
    }
}
