package souldestroyer.logs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ConnectionSection(
    modifier: Modifier,
    backgroundColor: Color
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(4.dp))
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            text = "Connection Info"
        )
        Button(
            onClick = {
                TODO()
            }
        ) {
            Text("Measure C&C Latency")
        }
    }
}