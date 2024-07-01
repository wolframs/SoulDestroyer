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
import org.bitcoinj.core.Base58
import org.bouncycastle.util.test.Test
import souldestroyer.navigation.Screen
import souldestroyer.sol.WalletCoder.encodeJsonFileWalletByteArrayPrivKey
import souldestroyer.wallet.WalletRepository
import souldestroyer.wallet.Wallets

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
    var selectedOption by remember { mutableStateOf(WalletImportSelectedMethod.PRIVATE_KEY) }
    var mnemonic by remember { mutableStateOf(words) }
    var privateKeyString by remember { mutableStateOf("") }
    var publicKeyString by remember { mutableStateOf("") }

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

        ImportByRadioList(selectedOption) {
            selectedOption = it
        }

        Spacer(Modifier.height(24.dp))

        when (selectedOption) {
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
            WalletImportSelectedMethod.PRIVATE_KEY -> {
                PrivateKeyInput(
                    publicKeyString = publicKeyString,
                    privateKeyString = privateKeyString,
                    onPrivateKeyChanged = { inputString ->
                        privateKeyString = encodeJsonFileWalletByteArrayPrivKey(inputString, privateKeyString)
                    },
                    onPublicKeyChanged = {
                        publicKeyString = it
                    }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        ActionButtons(
            onImport = {
                when (selectedOption) {
                    WalletImportSelectedMethod.MNEMONIC -> Wallets.get().walletFromMnemonic(mnemonic, "")
                    WalletImportSelectedMethod.PRIVATE_KEY -> Wallets.get().walletFromPrivateKey(
                        Base58.decode(privateKeyString)
                    )
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
fun ImportByRadioList(
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
            text = "Currently only supports restoring wallets from 32 byte Base58 encoded private keys.\n" +
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
                onClick = { onSelectOption(WalletImportSelectedMethod.PRIVATE_KEY) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(WalletImportSelectedMethod.PRIVATE_KEY.string)
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
fun PrivateKeyInput(
    publicKeyString: String,
    privateKeyString: String,
    onPrivateKeyChanged: (String) -> Unit,
    onPublicKeyChanged: (String) -> Unit,
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
                value = publicKeyString,
                onValueChange = { onPublicKeyChanged(it) },
                label = { Text("Public Key") },
                modifier = Modifier.fillMaxWidth(0.6f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                onClick = {
                    val clipboardText = clipboardManager.getText()
                    if (clipboardText != null) {
                        onPublicKeyChanged(clipboardText.text)
                    }
                }
            ) {
                Text("Paste")
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = privateKeyString,
                onValueChange = { onPrivateKeyChanged(it) },
                label = { Text("Private Key") },
                modifier = Modifier.fillMaxWidth(0.6f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                onClick = {
                    val clipboardText = clipboardManager.getText()
                    if (clipboardText != null) {
                        onPrivateKeyChanged(clipboardText.text)
                    }
                }
            ) {
                Text("Paste")
            }
        }
    }
}

enum class WalletImportSelectedMethod(val string: String) {
    MNEMONIC("From Word List"),
    PRIVATE_KEY("From Private Key")
}

private val words = listOf(
    "hint",
    "begin",
    "crowd",
    "dolphin",
    "drive",
    "render",
    "finger",
    "above",
    "sponsor",
    "prize",
    "runway",
    "invest",
    "dizzy",
    "pony",
    "bitter",
    "trial",
    "ignore",
    "crop",
    "please",
    "industry",
    "hockey",
    "wire",
    "jeep",
    "meep"
)