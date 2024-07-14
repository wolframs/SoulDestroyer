package souldestroyer.wallet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import souldestroyer.wallet.domain.WalletManager
import souldestroyer.wallet.ui.button.MemoButton
import souldestroyer.wallet.ui.button.RemoveButton
import souldestroyer.wallet.ui.button.RequestAirdropButton
import souldestroyer.wallet.ui.button.RetrieveBalanceButton
import souldestroyer.wallet.ui.button.SendSolButton

@Composable
fun WalletRow(
    tag: String,
    balance: Double,
    publicKeyString: String,
    isActiveAccount: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(4.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .padding(all = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Checkbox(
                checked = isActiveAccount,
                onCheckedChange = { onCheckActiveAccount(publicKeyString, it) },
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Active Account",
                style = MaterialTheme.typography.labelSmall
            )
        }

        Column(
            modifier = Modifier
                .padding(all = 12.dp)
        ) {
            val clipboardManager = LocalClipboardManager.current
            PublicKeyRow(publicKeyString, clipboardManager)
            Text(
                text = "Wallet \"$tag\" has an amazing balance of $balance!",
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Row {
                RetrieveBalanceButton(publicKeyString)
                Spacer(Modifier.width(12.dp))

                MemoButton(publicKeyString)
                Spacer(Modifier.width(12.dp))

                RequestAirdropButton(publicKeyString)
                Spacer(Modifier.width(12.dp))

                SendSolButton(publicKeyString)

                Spacer(Modifier.width(32.dp))

                RemoveButton(publicKeyString)
            }

        }
    }
}

fun onCheckActiveAccount(publicKeyString: String, newIsActiveAccountValue: Boolean) {
    // Do not react to disabling the account, because we'd just reset to activating the first account.
    if (!newIsActiveAccountValue)
        return

    WalletManager.getByPublicKey(publicKeyString)?.setIsActiveAccount(true)
}

@Composable
private fun PublicKeyRow(
    publicKeyString: String,
    clipboardManager: ClipboardManager
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = publicKeyString,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .clickable(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(publicKeyString))
                    }
                )
        )
        Icon(
            imageVector = Icons.Outlined.CopyAll,
            contentDescription = "",
            tint = Color.Gray,
            modifier = Modifier
                .size(MaterialTheme.typography.labelMedium.fontSize.value.dp)
                .clickable(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(publicKeyString))
                    }
                )
        )
    }
}