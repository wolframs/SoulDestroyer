package souldestroyer.tokens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import souldestroyer.navigation.Screen
import souldestroyer.raydium.requests.fetchAllRaydiumPools
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
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    fetchAllRaydiumPools()
                }
            }
        ) {
            Text("Fetch Raydium Pools")
        }
    }
}