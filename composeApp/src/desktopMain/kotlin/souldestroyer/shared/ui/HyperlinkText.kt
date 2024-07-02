package souldestroyer.shared.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.sun.tools.javac.jvm.ByteCodes.pop
import java.awt.Desktop
import java.net.URI

@Composable
fun HyperlinkText(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    url: String,
    urlColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    style: TextStyle = TextStyle()
) {
    val annotatedString = buildAnnotatedString {
        pushStyle(
            style = SpanStyle(
                color = textColor
            )
        )
        append(text)
        append(" (")
        pop()
        val startIndex = length
        pushStyle(
            style = SpanStyle(
                color = urlColor,
                textDecoration = TextDecoration.Underline
            )
        )
        append(url)
        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = startIndex,
            end = startIndex + url.length
        )
        pop()
        pushStyle(
            style = SpanStyle(
                color = textColor
            )
        )
        append(")")
        pop()
    }

    ClickableText(
        text = annotatedString,
        modifier = Modifier.fillMaxWidth(),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(URI(annotation.item))
                    }
                }
        },
        style = style
    )
}