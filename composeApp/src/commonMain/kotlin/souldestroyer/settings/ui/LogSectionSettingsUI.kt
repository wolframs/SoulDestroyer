package souldestroyer.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import souldestroyer.settings.SettingsManager

@Composable
fun LogSectionSettingsUI(
    settingsManager: SettingsManager
) {
    var showVerboseLogs by remember { mutableStateOf(settingsManager.showVerboseLogs) }

    Column(
        modifier = Modifier.padding(start = 32.dp, top = 16.dp)
    ) {
        Text(
            text = "Logs",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Control whether to show verbose debug logs in the live log list.\n" +
                    "This includes logs of small database interactions or ongoing polling operations.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = showVerboseLogs,
                onCheckedChange = { newValue ->
                    SettingsManager().showVerboseLogs = newValue
                    showVerboseLogs = newValue
                }
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Show verbose logs",
                style = TextStyle.Default.copy(fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 3.dp)
            )
        }
    }
}