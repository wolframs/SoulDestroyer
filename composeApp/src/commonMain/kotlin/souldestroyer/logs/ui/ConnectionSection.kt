package souldestroyer.logs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import souldestroyer.SoulDestroyer

@Composable
fun ConnectionSection(
    modifier: Modifier,
    backgroundColor: Color
) {
    val timeToBlockhash by SoulDestroyer.instance().timeToBlockhashFlow.collectAsState()
    val timeToBlockhashAverage by SoulDestroyer.instance().timeToBlockhashAverageFlow.collectAsState()

    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(4.dp))
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                text = "Connection Info",
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(Modifier.height(8.dp))

            val keys = listOf<String>("Last RPC Blockhash Latency", "Average RPC Blockhash Latency")
            val values = listOf<String>("$timeToBlockhash ms", "$timeToBlockhashAverage ms")

            Column(
                Modifier.padding(start = 8.dp, end = 8.dp)
            ) {
                keys.forEachIndexed { index, keyText ->
                    Row {

                        Text(
                            modifier = Modifier
                                .weight(0.5f, fill = true),
                            text = keyText,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic
                            )
                        )
                        Text(
                            modifier = Modifier
                                .weight(0.5f, fill = true),
                            text = values[index],
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )

                    }
                    Spacer(Modifier.height(2.dp))
                }
            }
        }
    }
}