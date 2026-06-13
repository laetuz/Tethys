package id.neotica.tethys.ui.screen.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import id.neotica.tethys.utils.mirror

private val SHARK_NORMAL = """
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣴⡾⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡞⠋⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠃⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣴⠶⣿⠟
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣴⢏⡀⠀⠀⠀⠸⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠋⢠⡾⠃⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⣤⠶⠶⠛⠛⠋⠉⠉⠀⠀⠀⠀⠈⠛⠿⠶⠶⠤⣤⣤⣄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⣠⡞⠋⠀⣰⠟⠀⠀⠀
⠀⠀⠀⣀⣠⡴⠶⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠛⠓⠶⢦⣤⣴⡞⠋⠀⢀⣼⠋⠀⠀⠀⠀
⣠⡴⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⠀⣾⠁⠀⠀⠀⠀⠀
⠛⠷⠦⢤⣤⡴⠖⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢦⡀⠀⠀⠀⣀⣀⣤⣤⡴⠶⢿⣄⠀⠀⢿⡀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠙⠛⠳⠶⠶⢤⣤⣤⣤⣀⣀⣀⣀⣤⣤⣞⡀⠀⠀⠸⣟⠛⠛⠛⠛⠛⠷⣤⣄⣟⠁⠀⠀⠀⠀⠀⠙⢷⣄⠘⣧⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠛⠳⣶⣦⣽⣷⣄⠀⠀⠀⠀⠀⠈⠙⠀⠀⠀⠀⠀⠀⠀⠀⠙⢷⣿⡆⠀⠀⠀⠀
""".trimIndent()

private val SHARK_NORMAL_2 = """
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣴⡾⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡞⠋⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠃⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀  ⠀ ⠀⣀⣴⠶⣿⠟
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣴⢏⡀⠀⠀⠀⠸⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   ⣠⡾⠋⢠⡾⠃⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⣤⠶⠶⠛⠛⠋⠉⠉⠀⠀⠀⠀⠈⠛⠿⠶⠶⠤⣤⣤⣄⣀⣀⡀⠀⠀⠀⠀⠀⠀⣠⡞⠋⠀⣰⠟⠀⠀⠀
⠀⠀⠀⣀⣠⡴⠶⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠛⠓⠶⢦⣤⣴⡞⠋⠀⢀⣼⠋⠀⠀⠀⠀
⣠⡴⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⣾⠁⠀⠀⠀⠀⠀
⠛⠷⠦⢤⣤⡴⠖⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢦⡀⠀⠀⠀⣀⣀⣤⣤⡴⠶⢿⣄⢿⡀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠙⠛⠳⠶⠶⢤⣤⣤⣤⣀⣀⣀⣀⣤⣤⣞⡀⠀⠀⠸⣟⠛⠛⠛⠛⠛⠷⣤⣄⣟⠁⠀⠀⠀⠙⢷⣄⠘⣧⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠛⠳⣶⣦⣽⣷⣄⠀⠀⠀⠀⠀⠈⠙⠀⠀⠀⠀⠀⠀⠀⠙⢷⣿⡆⠀⠀⠀⠀
""".trimIndent()

private val SHARK_NORMAL1 = SHARK_NORMAL_2
private val SHARK_NORMAL_RIGHT = SHARK_NORMAL.mirror()
private val SHARK_NORMAL1_RIGHT = SHARK_NORMAL_2.mirror()

private val SHARK_BITING = """
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣴⡾⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡞⠋⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠃⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣴⠶⣿⠟
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣴⢏⡀⠀⠀⠀⠸⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠋⢠⡾⠃⠀
⠀⠀⠀   ⣀⣠⡴⠶⠛⠶⠶⠛⠛⠋⠉⠉⠀⠀⠀⠀⠈⠛⠿⠶⠶⠤⣤⣤⣄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⣠⡞⠋⠀⣰⠟⠀⠀⠀
⣠⡴⠟⠋⠁⠀⠶⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠛⠓⠶⢦⣤⣴⡞⠋⠀⢀⣼⠋⠀⠀⠀⠀
⠛⠷⠦⢤⣤⡴⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⠀⣾⠁⠀⠀⠀⠀⠀
      ⠖⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢦⡀⠀⠀⠀⣀⣀⣤⣤⡴⠶⢿⣄⠀⠀⢿⡀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠙⠛⠳⠶⠶⢤⣤⣤⣤⣀⣀⣀⣀⣤⣤⣞⡀⠀⠀⠸⣟⠛⠛⠛⠛⠛⠷⣤⣄⣟⠁⠀⠀⠀⠀⠀⠙⢷⣄⠘⣧⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠛⠳⣶⣦⣽⣷⣄⠀⠀⠀⠀⠀⠈⠙⠀⠀⠀⠀⠀⠀⠀⠀⠙⢷⣿⡆⠀⠀⠀⠀
""".trimIndent().mirror()

private val SHARK_BITING_LEFT = SHARK_BITING.mirror()

enum class Direction { LEFT, RIGHT }

@Composable
fun Shark(
    x: Float,
    y: Float,
    velocityX: Float,
    isBiting: Boolean,
    frameCount: Float,
    onBite: () -> Unit,
    onSizeChanged: (IntSize) -> Unit,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val direction = if (velocityX > 0) Direction.RIGHT else Direction.LEFT
    val swimFrame = (frameCount / 25).toInt() % 2 == 0
    val art = when (direction) {
        Direction.RIGHT -> when {
            isBiting -> SHARK_BITING
            swimFrame -> SHARK_NORMAL_RIGHT
            else -> SHARK_NORMAL1_RIGHT
        }
        Direction.LEFT -> when {
            isBiting -> SHARK_BITING_LEFT
            swimFrame -> SHARK_NORMAL
            else -> SHARK_NORMAL1
        }
    }

    Text(
        text = art,
        color = Color.Green,
        fontFamily = FontFamily.Monospace,
        fontSize = fontSize,
        style = TextStyle(lineHeight = fontSize),
        modifier = modifier
            .offset(
                x = with(density) { x.toDp() },
                y = with(density) { y.toDp() }
            )
            .clickable { onBite() }
            .onSizeChanged(onSizeChanged)
    )
}
