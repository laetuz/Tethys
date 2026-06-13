package id.neotica.tethys.wear

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.BatteryManager
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
        return ComplicationSlotsManager(emptyList(), currentUserStyleRepository)
    }

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        try {
            val renderer = object : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
                surfaceHolder, currentUserStyleRepository, watchState,
                CanvasType.HARDWARE, 16L, false
            ) {
                private var lastBatteryCheck = 0L
                private var cachedBatteryLevel = 0

                override suspend fun createSharedAssets(): Renderer.SharedAssets {
                    return object : Renderer.SharedAssets {
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
                    val isAmbient = watchState.isAmbient.value == true
                    val textColor = if (isAmbient) Color.WHITE else Color.GREEN
                    val antiAlias = !isAmbient

                    val pad = bounds.height() * 0.06f

                    // --- Time (top, centered) ---
                    val timePaint = Paint().apply {
                        color = textColor
                        typeface = Typeface.MONOSPACE
                        isAntiAlias = antiAlias
                        textSize = bounds.width() / 8f
                    }
                    val timeStr = "%02d:%02d".format(zonedDateTime.hour, zonedDateTime.minute)
                    val timeY = pad + timePaint.textSize
                    canvas.drawText(
                        timeStr,
                        bounds.centerX() - timePaint.measureText(timeStr) / 2f,
                        timeY,
                        timePaint
                    )

                    // --- Battery (bottom, centered) ---
                    val now = System.currentTimeMillis()
                    if (now - lastBatteryCheck > 60_000) {
                        val bm = this@TethysWatchFaceService.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
                        cachedBatteryLevel = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 0
                        lastBatteryCheck = now
                    }
                    val batteryPaint = Paint().apply {
                        color = textColor
                        typeface = Typeface.MONOSPACE
                        isAntiAlias = antiAlias
                        textSize = bounds.width() / 16f
                    }
                    val batteryStr = "$cachedBatteryLevel%"
                    val batY = bounds.height() - pad.toInt()
                    canvas.drawText(
                        batteryStr,
                        bounds.centerX() - batteryPaint.measureText(batteryStr) / 2f,
                        batY.toFloat(),
                        batteryPaint
                    )

                    // --- Shark (middle, frozen in ambient mode) ---
                    val epochMillis = zonedDateTime.toInstant().toEpochMilli()
                    val swimFrame = !isAmbient && (epochMillis / 400) % 2 == 0L
                    val bobOffset = if (!isAmbient) sin(epochMillis * 0.005).toFloat() * 4f else 0f

                    val art = if (swimFrame) SHARK_NORMAL else SHARK_NORMAL2
                    val lines = art.lines()
                    val maxLineLength = lines.maxOf { it.length }

                    val sharkPaint = Paint().apply {
                        color = textColor
                        typeface = Typeface.MONOSPACE
                        isAntiAlias = antiAlias
                        textSize = bounds.width().toFloat() / maxLineLength
                    }
                    val lineHeight = sharkPaint.textSize * 1.2f
                    val totalHeight = lines.size * lineHeight

                    val topBound = timeY + timePaint.textSize * 0.3f
                    val bottomBound = batY - batteryPaint.textSize * 0.5f
                    val availableCenter = (topBound + bottomBound) / 2f
                    val startY = availableCenter - totalHeight / 2f + bobOffset

                    lines.forEachIndexed { i, line ->
                        canvas.drawText(
                            line,
                            bounds.centerX() - sharkPaint.measureText(line) / 2f,
                            startY + (i + 1) * lineHeight,
                            sharkPaint
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
