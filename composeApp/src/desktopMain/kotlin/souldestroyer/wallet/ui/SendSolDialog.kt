package souldestroyer.wallet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.materialkolor.ktx.blend
import foundation.metaplex.base58.decodeBase58
import foundation.metaplex.solanaeddsa.SolanaEddsa
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import souldestroyer.logs.LogRepository
import souldestroyer.wallet.domain.WalletManager.walletScope

@Composable
fun SendSolDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (solAmount: String, receiverPublicKey: String) -> Unit
) {
    if (!showDialog)
        return

    var solAmount by remember { mutableStateOf("") }
    var receiverPublicKey by remember { mutableStateOf("") }
    val addressValid = mutableStateOf(AddressValidState.EMPTY)

    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .width(550.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Send SOL to address",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                )

                Text(
                    text = "LOSS RISK WARNING:\n" +
                            "Only supports basic Solana Network transfers,\n" +
                            "make sure that the receiver address is on the basic Solana network.\n" +
                            "\n" +
                            "Receiver address verification before sending may be faulty,\n" +
                            "your funds may get lost, if you don't double check the address yourself.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp),
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = solAmount,
                    label = { Text("SOL Amount") },
                    onValueChange = {
                        if (it.toDoubleOrNull() != null || it == "")
                            solAmount = it
                    },
                    modifier = Modifier.padding(8.dp)
                )

                Column {
                    TextField(
                        value = receiverPublicKey,
                        label = { Text("Receiver Address (Solana Network)") },
                        onValueChange = {
                            receiverPublicKey = it.trim()

                            if (receiverPublicKey.isBlank()) {
                                addressValid.value = AddressValidState.EMPTY
                            }
                            else {
                                addressValid.value = AddressValidState.CHECKING

                                checkAddressValid(it, addressValid)
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    Button(
                        onClick = { onConfirmation(solAmount, receiverPublicKey) },
                        modifier = Modifier.padding(8.dp),
                        enabled =
                            addressValid.value == AddressValidState.VALID
                                    && solAmount.toDoubleOrNull() != null
                    ) {
                        Text("Transfer SOL")
                    }
                }
            }
        }

    }
}

fun checkAddressValid(publicKeyString: String, addressValid: MutableState<AddressValidState>) {
    walletScope.launch(Dispatchers.Default) {
        try {
            if (PublicKey(publicKeyString).toByteArray().size <= PublicKey.PUBLIC_KEY_LENGTH)
                addressValid.value = AddressValidState.VALID
            else
                addressValid.value = AddressValidState.INVALID
        } catch (e: Throwable) {
            addressValid.value = AddressValidState.INVALID
        }
    }
}

enum class AddressValidState(val description: String) {
    EMPTY("Empty"),
    CHECKING("Checking..."),
    VALID("Valid size."),
    INVALID("Invalid size.")
}
