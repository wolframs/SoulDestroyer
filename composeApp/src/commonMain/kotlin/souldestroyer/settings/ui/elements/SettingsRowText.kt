package souldestroyer.settings.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsRowText(
    text: String,
    isSecondary: Boolean = false,
    modifier: Modifier = Modifier
) {
    val style = if (isSecondary) {
        TextStyle.Default.copy(fontSize = 16.sp, fontStyle = FontStyle.Italic)
    } else {
        TextStyle.Default.copy(fontSize = 16.sp)
    }

    val color = if (isSecondary) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = text,
        color = color,
        style = style,
        modifier = modifier.padding(bottom = 3.dp)
    )
}