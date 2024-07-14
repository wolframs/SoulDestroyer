package souldestroyer.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import souldestroyer.navigation.Screen
import souldestroyer.settings.SettingsManager
import souldestroyer.shared.ui.ScrollableScreenColumnWithHeadline

@Serializable
object SettingsScreen : Screen {
    override val label: String = "Settings"
    override val route = "/settings"
    override val iconImageVector: ImageVector = Icons.Filled.Settings
    override var selected = mutableStateOf(false)
}

@Composable
fun DebugScreen(paddingValues: PaddingValues) {

    ScrollableScreenColumnWithHeadline(
        paddingValues = paddingValues,
        headline = "Settings"
    ) {
        val settingsManager = SettingsManager()

        RPCEndpointSettingsUI()

        Spacer(Modifier.height(16.dp))

        LogSectionSettingsUI(settingsManager)

        Spacer(Modifier.height(16.dp))

        ThemeSettingsUI(settingsManager)

    }
}


