package souldestroyer.settings.ui.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingRowTextField(
    modifier: Modifier = Modifier,
    primaryText: String,
    textFieldValue: String,
    textFieldLabelText: String,
    secondaryText: String? = null,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsRowText(primaryText)

        Spacer(modifier = Modifier.width(2.dp))

        TextField(
            value = textFieldValue,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(textFieldLabelText) }
        )

        Spacer(modifier = Modifier.width(2.dp))

        secondaryText?.let {
            SettingsRowText(text = it, isSecondary = true)
        }
    }
}