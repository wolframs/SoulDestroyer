package souldestroyer.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jdk.jfr.Enabled
import souldestroyer.shared.ui.HyperlinkText
import souldestroyer.sol.RPCEndpoint
import souldestroyer.wallet.ui.WalletImportSelectedMethod

@Composable
fun RPCEndpointRadioList(
    options: List<RPCEndpoint>,
    selectedOption: RPCEndpoint,
    enabled: Boolean,
    onSelectOption: (RPCEndpoint) -> Unit
) {
    Column(
        modifier = Modifier.padding(start = 32.dp, top = 16.dp)
    ) {
        Text(
            text = "RPC Endpoint",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "This decides through which RPC node the app tries to interact with the Solana blockchain.\n" +
                    "A different node can mean a different blockchain. Your wallets in this app's database likely only exist on either one of the chains.\n" +
                    "\n" +
                    "The RPC node decides, which chain is interacted with. The default options interact with either the" +
                    "public Solana DevNet or TestNet, which use worthless and limitless SOL, or the MainNet, which uses real SOL.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(4.dp))

        options.forEach { rpcEndpoint ->
            RPCEndpointRadioButtonRow(
                rpcEndpoint,
                selectedOption,
                enabled
            ) {
                onSelectOption(rpcEndpoint)
            }
        }
    }
}

@Composable
private fun RPCEndpointRadioButtonRow(
    thisRpcEndpoint: RPCEndpoint,
    selectedOption: RPCEndpoint,
    enabled: Boolean,
    onSelectOption: (RPCEndpoint) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedOption == thisRpcEndpoint,
            onClick = { onSelectOption(thisRpcEndpoint) },
            enabled = enabled
        )
        Spacer(modifier = Modifier.width(8.dp))
        HyperlinkText(
            text = thisRpcEndpoint.description,
            url = thisRpcEndpoint.url,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}