package souldestroyer.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import souldestroyer.SoulDestroyer
import souldestroyer.navigation.Screen
import souldestroyer.sol.RPCEndpoint

@Serializable
object SettingsScreen : Screen {
    override val label: String = "Settings"
    override val route = "/settings"
    override val iconImageVector: ImageVector = Icons.Filled.Settings
    override var selected = mutableStateOf(false)
}

@Composable
fun DebugScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(24.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(24.dp))

        val availableRpcEndpoints = RPCEndpoint.entries
        var selectedRPCEndpoint by remember {
            mutableStateOf(SoulDestroyer.instance().solana.rpcEndpoint)
        }
        var rpcEndpointRadioListEnabled by remember { mutableStateOf(true) }
        RPCEndpointRadioList(
            options = availableRpcEndpoints,
            selectedOption = selectedRPCEndpoint,
            enabled = rpcEndpointRadioListEnabled
        ) { newSelectedRpcEndpoint ->
            rpcEndpointRadioListEnabled = false
            SoulDestroyer.instance().soulScope.launch {
                if (
                    SoulDestroyer.instance().solana.changeEndpoint(newSelectedRpcEndpoint)
                ) {
                    selectedRPCEndpoint = newSelectedRpcEndpoint
                }
            }.invokeOnCompletion {
                rpcEndpointRadioListEnabled = true
            }
        }



        Spacer(Modifier.height(24.dp))
    }
}