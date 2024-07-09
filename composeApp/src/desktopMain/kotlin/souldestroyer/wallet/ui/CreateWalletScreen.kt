package souldestroyer.wallet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import souldestroyer.logs.LogRepository
import souldestroyer.navigation.Screen
import souldestroyer.wallet.Wallets
import souldestroyer.wallet.WalletRepository

@Serializable
object CreateWalletScreen : Screen {
    override val label: String = "Create Wallet"
    override val route: String = "/wallet/createWallet"
    override val iconImageVector: ImageVector = Icons.Filled.AddCircle
    override var selected = mutableStateOf(false)
}

@Composable
fun CreateWalletScreen(
    paddingValues: PaddingValues,
    onCancel: () -> Unit
) {
    var tag: String by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current

    val walletRepository = WalletRepository.instance()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(24.dp)
    ) {
        Text(
            text = "Create Wallet",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = tag,
                onValueChange = { tag = it },
                label = { Text("Tag") },
                modifier = Modifier.fillMaxWidth(0.6f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                onClick = {
                    val clipboardText = clipboardManager.getText()
                    if (clipboardText != null) {
                        tag = clipboardText.text
                    }
                }
            ) {
                Text("Paste")
            }
        }

        Spacer(Modifier.height(24.dp))

        Spacer(Modifier.height(24.dp))

        ActionButtons(
            onCreate = {
                if (Wallets.instance().createNew(tag))
                    onCancel() // return to WalletScreen on success
            },
            onCancel = onCancel
        )
    }
}

@Composable
private fun ActionButtons(
    onCancel: () -> Unit,
    onCreate: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(Modifier.width(24.dp))

        OutlinedButton(
            modifier = Modifier.weight(0.25f),
            onClick = {
                onCancel()
            }
        ) {
            Text("Cancel")
        }

        Spacer(Modifier.width(24.dp))

        Button(
            modifier = Modifier.weight(0.25f),
            onClick = { onCreate() }
        ) {
            Text("Create Wallet")
        }

        Spacer(Modifier.width(24.dp))
    }
}