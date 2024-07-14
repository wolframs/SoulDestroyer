package souldestroyer.logs.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import souldestroyer.logs.LogRepository
import souldestroyer.settings.SettingsManager
import souldestroyer.shared.ui.ColorFancies.modColor

@Composable
fun SideDisplaySection(
    modifier: Modifier,
    paddingValues: PaddingValues
) {
    val logRepository = LogRepository()
    val logListState by logRepository.logList().collectAsState()

    val settingsManager = SettingsManager()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                end = paddingValues.calculateRightPadding(LayoutDirection.Ltr),
                bottom = paddingValues.calculateBottomPadding()
            )
            .padding(4.dp)
    ) {
        val logListBackgroundColor = modColor(
            color = MaterialTheme.colorScheme.secondaryContainer,
            brightenBy = -0.375f
        )
        val conInfoBackgroundColor = modColor(
            color = MaterialTheme.colorScheme.secondaryContainer,
            brightenBy = -0.233f
        )

        LogSection(
            modifier = Modifier
                .fillMaxHeight(0.76f),
            backgroundColor = logListBackgroundColor,
            logList = logListState.logList
        )

        Spacer(Modifier.height(4.dp))

        ConnectionSection(
            modifier = Modifier
                .fillMaxHeight(),
            backgroundColor = conInfoBackgroundColor
        )
    }
}