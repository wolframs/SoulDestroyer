package souldestroyer.wallet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import souldestroyer.navigation.Screen
import souldestroyer.shared.ui.ScrollableScreenColumnWithHeadline
import souldestroyer.wallet.model.WfWallet

@Serializable
object WalletScreen : Screen {
    override val label: String = "Wallets"
    override val route: String = "/wallet"
    override val iconImageVector: ImageVector = Icons.Filled.Email
    override var selected = mutableStateOf(false)
}

@Composable
fun WalletScreen(
    paddingValues: PaddingValues,
    wallets: List<WfWallet>,
    onGoToCreateWalletScreen: () -> Unit,
    onGoToImportWalletScreen: () -> Unit
) {
    ScrollableScreenColumnWithHeadline(
        paddingValues = paddingValues,
        headline = "Wallets"
    ) {
        wallets.forEach { wallet ->

            WalletRow(
                tag = wallet.tag,
                balance = wallet.balance,
                publicKeyString = wallet.publicKey,
                isActiveAccount = wallet.isActiveAccount
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlobalWalletActions(
            onGoToCreateWalletScreen,
            onGoToImportWalletScreen
        )
    }
}

@Composable
fun GlobalWalletActions(
    onGoToCreateWalletScreen: () -> Unit,
    onGoToImportWalletScreen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = {
            onGoToCreateWalletScreen()
        }) {
            Text("Create Wallet")
        }
        Button(onClick = {
            onGoToImportWalletScreen()
        }) {
            Text("Import Wallet")
        }
    }
}