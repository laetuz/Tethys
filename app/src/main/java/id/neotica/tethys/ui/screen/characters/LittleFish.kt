package id.neotica.tethys.ui.screen.characters

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit

@Composable
fun LittleFish(
    x: Float,
    y: Float,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    Text(
        text = "𓆝",
        color = Color.Green,
        fontFamily = FontFamily.Monospace,
        fontSize = fontSize,
        modifier = modifier.offset(
            x = with(density) { x.toDp() },
            y = with(density) { y.toDp() }
        )
    )
}
