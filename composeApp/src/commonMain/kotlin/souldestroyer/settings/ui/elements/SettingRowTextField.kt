package souldestroyer.settings.ui.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SettingRowTextField(
    modifier: Modifier = Modifier,
    primaryText: String,
    textFieldValue: String,
    textFieldLabelText: String,
    textFieldWidth: Dp = 140.dp,
    secondaryText: String? = null,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(textFieldLabelText) },
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .width(textFieldWidth)
        )

        Spacer(modifier = Modifier.width(2.dp))

        SettingsRowText(primaryText)

        secondaryText?.let {
            Spacer(modifier = Modifier.width(8.dp))

            SettingsRowText(text = it, isSecondary = true)
        }
    }
}