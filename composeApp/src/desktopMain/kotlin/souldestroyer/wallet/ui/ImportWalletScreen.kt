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
import androidx.compose.material3.RadioButton
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
import souldestroyer.navigation.Screen
import souldestroyer.wallet.Wallets
import souldestroyer.wallet.domain.WalletImportSelectedMethod

@Serializable
object ImportWalletScreen : Screen {
    override val label: String = "Recover Wallet"
    override val route: String = "/wallet/importWallet"
    override val iconImageVector: ImageVector = Icons.Filled.AddCircle
    override var selected = mutableStateOf(false)
}

@Composable
fun ImportWalletScreen(
    paddingValues: PaddingValues,
    onCancel: () -> Unit
) {
    // State to hold the selected option
    var selectedMethod by remember { mutableStateOf(WalletImportSelectedMethod.PRIVATE_KEY) }
    var mnemonic: List<String> by remember { mutableStateOf(listOf("")) }
    var privateKeyString by remember { mutableStateOf("") }
    var tagString by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(24.dp)
    ) {
        Text(
            text = "Recover Wallet",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(24.dp))

        ImportMethodRadioList(selectedMethod) {
            selectedMethod = it
        }
        Spacer(Modifier.height(24.dp))

        when (selectedMethod) {
            // WORD LIST INPUT
            WalletImportSelectedMethod.MNEMONIC -> {
                WordListImportInput(
                    wordList = mnemonic,
                    onWordListChanged = {
                        mnemonic = it.split(" ")
                    }
                )
            }
            // PRIVATE KEY INPUT
            WalletImportSelectedMethod.PRIVATE_KEY,
            WalletImportSelectedMethod.BYTE_ARRAY -> {
                SecretStringInput(
                    tagString = tagString,
                    secretString = privateKeyString,
                    method = selectedMethod,
                    onSecretStringChanged = {
                        privateKeyString = it
                    },
                    onTagChanged = {
                        tagString = it
                    }
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        ActionButtons(
            onImport = {
                when (selectedMethod) {
                    WalletImportSelectedMethod.MNEMONIC -> {
                        Wallets.instance().createFromMnemonic(mnemonic, "")
                    }
                    WalletImportSelectedMethod.PRIVATE_KEY,
                    WalletImportSelectedMethod.BYTE_ARRAY -> {
                        Wallets.instance().createFromSecret(selectedMethod, tagString, privateKeyString)
                    }
                }

            },
            onCancel = onCancel
        )
    }
}

@Composable
private fun ActionButtons(
    onCancel: () -> Unit,
    onImport: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(Modifier.width(24.dp))

        Button(
            modifier = Modifier.weight(0.25f),
            onClick = { onImport() }
        ) {
            Text("Recover Wallet")
        }

        Spacer(Modifier.width(24.dp))

        Button(
            modifier = Modifier.weight(0.25f),
            onClick = {
                onCancel()
            }
        ) {
            Text("Cancel")
        }

        Spacer(Modifier.width(24.dp))
    }
}

@Composable
private fun ImportMethodRadioList(
    selectedOption: WalletImportSelectedMethod,
    onSelectOption: (WalletImportSelectedMethod) -> Unit
) {

    Column(
        modifier = Modifier.padding(start = 32.dp, top = 16.dp)
    ) {
        Text(
            text = "Method",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Currently only supports restoring wallets from base58 encoded private keys\n" +
                    "or raw byte arrays, as found in wallet.json files.\n" +
                    "This might change with upcoming releases of the SolanaKMP library by Metaplex.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(4.dp))

        // Radio button for "From Word List"
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedOption == WalletImportSelectedMethod.MNEMONIC,
                onClick = { onSelectOption(WalletImportSelectedMethod.MNEMONIC) },
                enabled = false
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(WalletImportSelectedMethod.MNEMONIC.string)
        }

        // Radio button for "From Private Key"
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedOption == WalletImportSelectedMethod.PRIVATE_KEY,
                onClick = { onSelectOption(WalletImportSelectedMethod.PRIVATE_KEY) },
                enabled = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(WalletImportSelectedMethod.PRIVATE_KEY.string)
        }

        // Radio button for "From Byte Array"
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedOption == WalletImportSelectedMethod.BYTE_ARRAY,
                onClick = { onSelectOption(WalletImportSelectedMethod.BYTE_ARRAY) },
                enabled = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(WalletImportSelectedMethod.BYTE_ARRAY.string)
        }
    }
}

@Composable
fun WordListImportInput(
    wordList: List<String>,
    onWordListChanged: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier.padding(start = 32.dp, top = 16.dp)
    ) {
        Text(
            text = "Wallet Data",
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = wordList.joinToString(" "),
                onValueChange = { onWordListChanged(it) },
                label = { Text("Word List (Mnemonic)") },
                modifier = Modifier.fillMaxWidth(0.6f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                onClick = {
                    val clipboardText = clipboardManager.getText()
                    if (clipboardText != null) {
                        onWordListChanged(clipboardText.text)
                    }
                }
            ) {
                Text("Paste")
            }
        }
    }
}

@Composable
fun SecretStringInput(
    tagString: String,
    secretString: String,
    method: WalletImportSelectedMethod,
    onSecretStringChanged: (String) -> Unit,
    onTagChanged: (String) -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier.padding(start = 32.dp, top = 16.dp)
    ) {
        Text(
            text = "Wallet Data",
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = tagString,
                onValueChange = { onTagChanged(it) },
                label = { Text("Tag") },
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }

        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val labelText = if (method == WalletImportSelectedMethod.PRIVATE_KEY) "Private Key" else "Byte Array []"
            OutlinedTextField(
                value = secretString,
                onValueChange = { onSecretStringChanged(it) },
                label = { Text(labelText) },
                modifier = Modifier.fillMaxWidth(0.6f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                onClick = {
                    val clipboardText = clipboardManager.getText()
                    if (clipboardText != null) {
                        onSecretStringChanged(clipboardText.text)
                    }
                }
            ) {
                Text("Paste")
            }
        }
    }
}

