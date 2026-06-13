package id.neotica.tethys.ui.screen.characters

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

private fun String.mirror() = lines().joinToString("\n") { it.reversed() }

private val SHARK_NORMAL_LEFT = SHARK_NORMAL.mirror()

private val SHARK_BITING = """
⢰⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠈⢧⠨⠢⡀⠀⠀⠀⠀⠀⠀⠲⢒⠒⠦⠤⣀⡀⠀⠀⠀⠀⢀⣠⣤⠴⠒⠋⠙⡄
⠀⠸⠀⠐⢱⠀⠀⠀⠀⠀⠀⢀⣈⠦⠤⠀⠒⠊⠉⠉⠀⠈⠁⣀⠭⠀⢁⣀⣀⣹
⠀⠀⠖⠀⠃⠓⢤⠖⠒⠚⠉⠀⠀⡀⠀⠀⠀⢀⣰⢴⠶⠐⢈⣴⣿⠟⠿⢻⣿⡞
⠀⢰⠀⠀⠀⡤⢤⣄⠀⠀⠤⠤⠀⠀⣐⠄⠀⠼⡏⡧⠄⢢⡾⠋⠀⠀⢠⣎⡟⠀
⢠⠃⣀⠴⠋⠀⠀⠈⠑⠢⢄⣀⣠⠊⠀⢀⠎⠀⠡⠅⠀⠻⢿⣤⣤⡴⣾⣴⣿⠀
⡬⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⡼⠃⣠⠶⣇⣀⣀⠀⠀⠀⠀⠀⣀⣌⡠⠤⠘⠋⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣔⡡⠚⠁⠀⠀⠀⠈⠉⠉⠉⠻⡍⠙⡄⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠃⠀⠀⠀⠀⠀
""".trimIndent()

private val SHARK_BITING_LEFT = SHARK_BITING.mirror()

@Composable
fun Shark(
    x: Float,
    y: Float,
    velocityX: Float,
    isBiting: Boolean,
    onSizeChanged: (IntSize) -> Unit,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val facingLeft = velocityX > 0
    val art = when {
        isBiting && facingLeft -> SHARK_BITING_LEFT
        isBiting -> SHARK_BITING
        facingLeft -> SHARK_NORMAL_LEFT
        else -> SHARK_NORMAL
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
            .onSizeChanged(onSizeChanged)
    )
}
