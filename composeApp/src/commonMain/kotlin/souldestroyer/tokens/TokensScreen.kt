package souldestroyer.tokens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Token
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import souldestroyer.navigation.Screen
import souldestroyer.shared.ui.ScrollableScreenColumnWithHeadline

@Serializable
object TokensScreen : Screen {
    override val label: String = "Tokens"
    override val route = "/tokens"
    override val iconImageVector: ImageVector = Icons.Default.Token
    override var selected = mutableStateOf(false)
}

@Composable
fun TokensScreen(paddingValues: PaddingValues) {

    ScrollableScreenColumnWithHeadline(
        paddingValues = paddingValues,
        headline = "Tokens"
    ) {

    }
}