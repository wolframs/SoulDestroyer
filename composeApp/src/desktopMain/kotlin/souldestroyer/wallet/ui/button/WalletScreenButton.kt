package souldestroyer.wallet.ui.button

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import souldestroyer.wallet.Wallets
import souldestroyer.wallet.domain.WalletManager
import souldestroyer.wallet.domain.transaction.sendAirdropRequest
import souldestroyer.wallet.domain.transaction.sendMemoTransaction
import souldestroyer.wallet.domain.transaction.sendSolToReceiver
import souldestroyer.wallet.ui.dialog.AirdropRequestDialog
import souldestroyer.wallet.ui.dialog.MemoDialog
import souldestroyer.wallet.ui.dialog.SendSolDialog

@Composable
fun RetrieveBalanceButton(publicKeyString: String) {
    Button(
        onClick = {
            Wallets.instance().wList
                .firstOrNull { it.publicKey.toString() == publicKeyString }
                ?.retrieveBalance()
        }
    ) {
        Text("Update Balance")
    }
}

@Composable
fun MemoButton(publicKeyString: String) {
    var showDialog by remember { mutableStateOf(false) }

    MemoDialog(
        showDialog = showDialog,
        publicKeyString = publicKeyString,
        onDismissRequest = { showDialog = false },
        onConfirmation = { memoMsg ->
            WalletManager.getByPublicKey(publicKeyString)
                ?.sendMemoTransaction(memoText = memoMsg)
            showDialog = false
        }
    )

    Button(
        onClick = { showDialog = true }
    ) {
        Text("Memo Tx")
    }
}

@Composable
fun RequestAirdropButton(publicKeyString: String) {
    var showDialog by remember { mutableStateOf(false) }

    AirdropRequestDialog(
        showDialog = showDialog,
        onDismissRequest = { showDialog = false },
        onConfirmation = { amount ->
            WalletManager.getByPublicKey(publicKeyString)?.sendAirdropRequest(
                amount.toDoubleOrNull() ?: 0.0
            )
            showDialog = false
        }
    )

    Button(
        onClick = { showDialog = true }
    ) {
        Text("Airdrop")
    }
}

@Composable
fun SendSolButton(publicKeyString: String) {
    var showDialog by remember { mutableStateOf(false) }

    SendSolDialog(
        showDialog = showDialog,
        onDismissRequest = { showDialog = false },
        onConfirmation = { amount, receiverPublicKey ->
            WalletManager.getByPublicKey(publicKeyString)?.sendSolToReceiver(
                amount.toDoubleOrNull() ?: 0.0,
                receiverPublicKey
            )
            showDialog = false
        }
    )

    Button(
        onClick = { showDialog = true }
    ) {
        Text("Transfer SOL")
    }
}

@Composable
fun RemoveButton(publicKeyString: String) {
    OutlinedButton(
        onClick = {
            Wallets.instance().wList
                .first { it.publicKey.toString() == publicKeyString }
                .remove()
        }
    ) {
        Text("Remove")
    }
}