package souldestroyer.logs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import souldestroyer.logs.model.LogEntryType
import souldestroyer.logs.ui.LogEntryStyle.getLogEntryRowBackgroundColor
import souldestroyer.logs.ui.LogEntryStyle.getLogEntryRowColor
import souldestroyer.logs.wfDateTimeFormat

@Composable
fun LogEntryRow(
    modifier: Modifier = Modifier,
    dateTime: LocalDateTime,
    message: String,
    type: LogEntryType,
    clipboardManager: ClipboardManager,
    keys: List<String>? = null,
    values: List<String>? = null
) {
    val backgroundColor = getLogEntryRowBackgroundColor(type)
    val color = getLogEntryRowColor(type)

    val containerRowPaddingValues = PaddingValues(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(containerRowPaddingValues)
            .background(backgroundColor, RoundedCornerShape(4.dp))
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .padding(4.dp),
            imageVector = type.iconImageVector,
            contentDescription = null,
            tint = color
        )

        Spacer(modifier = Modifier.width(6.dp))

        Column(
            modifier = Modifier.padding(top = 2.dp, bottom = 6.dp, end = 6.dp)
        ) {
            Text(
                text = dateTime.time.format(wfDateTimeFormat),
                style = MaterialTheme.typography.labelMedium,
                color = color
                /*style = TextStyle(
                    fontSize = 12.sp,
                    color = color,
                    fontStyle = FontStyle.Italic
                )*/
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = color
                )
            )

            if (keys == null || values == null)
                return@Column

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                keys.forEachIndexed { index, keyText ->
                    Row {

                        Text(
                            modifier = Modifier
                                .weight(0.32f, fill = true),
                            text = keyText,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = color,
                                fontStyle = FontStyle.Italic
                            )
                        )
                        Text(
                            modifier = Modifier
                                .weight(0.68f, fill = true)
                                .clickable(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(values[index]))
                                    }
                                ),
                            text = values[index],
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = color
                            )
                        )

                    }
                    Spacer(Modifier.height(2.dp))
                }
            }
        }
    }
}