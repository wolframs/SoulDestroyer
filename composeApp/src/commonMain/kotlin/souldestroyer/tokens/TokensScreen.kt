package souldestroyer.tokens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import souldestroyer.navigation.Screen
import souldestroyer.raydium.requests.commandExecTest
import souldestroyer.raydium.requests.fetchAllRaydiumPools
import souldestroyer.raydium.requests.fetchPoolInfo
import souldestroyer.raydium.requests.jScriptEngineTest
import souldestroyer.shared.ui.ScrollableScreenColumnWithHeadline
import souldestroyer.sol.WfSolana

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
                jScriptEngineTest()
                /*CoroutineScope(Dispatchers.IO).launch {
                    fetchAllRaydiumPools()
                }*/
            }
        ) {
            Text("Test JS Script Engine")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                commandExecTest()
            }
        ) {
            Text("Test Command Execution")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                fetchPoolInfo(WfSolana.instance().rpcEndpoint.url)
            }
        ) {
            Text("Test Node HTTP Call")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    fetchAllRaydiumPools()
                }
            }
        ) {
            Text("Fetch all Raydium Pools")
        }
    }
}