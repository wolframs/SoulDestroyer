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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.materialkolor.PaletteStyle
import souldestroyer.settings.SeedColors
import souldestroyer.settings.SettingsManager
import souldestroyer.settings.ui.elements.SettingRowTextField

@Composable
fun ThemeSettingsUI(
    settingsManager: SettingsManager = SettingsManager()
) {
    val isDark by settingsManager.themeIsDarkFlow.collectAsState(initial = false)
    val activeColorId by settingsManager.themeColorFlow.collectAsState(initial = 0)
    val activePaletteId by settingsManager.themePaletteFlow.collectAsState(initial = 0)

    Column(
        modifier = Modifier.padding(start = 32.dp, top = 16.dp)
    ) {
        Text(
            text = "RPC Endpoint",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "These values show and let you edit the current theme settings.\n" +
                    "You can also use the theme settings next to the navigation bar to change the app's theme.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(4.dp))

        DarkLightModeSetting(isDark = isDark) { newValue ->
            settingsManager.darkTheme = newValue
        }

        Spacer(Modifier.height(4.dp))

        ThemeColorSetting(
            activeColorId
        ) { newValue ->
            settingsManager.themeColor = newValue
        }

        Spacer(Modifier.height(4.dp))

        ThemePaletteSetting(
            activePaletteId
        ) { newValue ->
            settingsManager.themePalette = newValue
        }
    }
}

@Composable
private fun DarkLightModeSetting(
    isDark: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isDark,
            onCheckedChange = { newValue ->
                onCheckedChange(newValue)
            }
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "Use dark theme",
            style = TextStyle.Default.copy(fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 3.dp)
        )
    }
}

@Composable
private fun ThemeColorSetting(
    activeColorId: Int,
    onIdChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingRowTextField(
            primaryText = "Color (0 - ${SeedColors.size - 1})",
            textFieldValue = activeColorId.toString(),
            textFieldLabelText = "ID #"
        ) {
            if (it.isNotEmpty() && it.toIntOrNull() != null) {
                onIdChange(it.toInt())
            }
        }
    }
}

@Composable
private fun ThemePaletteSetting(
    activePaletteId: Int,
    onIdChange: (Int) -> Unit
) {
    SettingRowTextField(
        primaryText = "Palette (0 - ${PaletteStyle.entries.size - 1})",
        secondaryText = PaletteStyle.entries[activePaletteId].name,
        textFieldValue = activePaletteId.toString(),
        textFieldLabelText = "ID #"
    ) {
        if (it.isNotEmpty() && it.toIntOrNull() != null && it.toInt() in 0..8) {
            onIdChange(it.toInt())
        }
    }
}