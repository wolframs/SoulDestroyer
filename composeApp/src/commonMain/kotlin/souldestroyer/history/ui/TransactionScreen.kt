package souldestroyer.history.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import souldestroyer.navigation.Screen
import souldestroyer.shared.ui.ScrollableScreenColumnWithHeadline

@Serializable
object TransactionScreen: Screen {
    override val label: String = "Transactions"
    override val route: String = "/transactions"
    override val iconImageVector: ImageVector = Icons.Outlined.Timeline
    override var selected = mutableStateOf(false)
}

@Composable
fun TransactionScreen(
    paddingValues: PaddingValues
) {
    ScrollableScreenColumnWithHeadline(
        paddingValues = paddingValues,
        headline = "Transactions"
    ) {

        PendingTransactions()

        ConfirmedTransactions()
    }
}

@Composable
private fun PendingTransactions(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(12.dp)
    ) {
        Text(
            text = "Pending",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun ConfirmedTransactions(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(12.dp)
    ) {
        Text(
            text = "Completed",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))
    }
}