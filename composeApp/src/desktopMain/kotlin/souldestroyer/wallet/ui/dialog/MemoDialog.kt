package souldestroyer.wallet.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun MemoDialog(
    showDialog: Boolean,
    publicKeyString: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (memoMsg: String) -> Unit
) {
    if (!showDialog)
        return

    var memoMsg by remember { mutableStateOf("SoulDestroyer Wallet $publicKeyString says hi.") }

    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
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
                    text = "Leave memo message in chain",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                )

                TextField(
                    value = memoMsg,
                    label = { Text("Memo Message") },
                    onValueChange = {
                        memoMsg = it
                    },
                    modifier = Modifier.padding(8.dp)
                )

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
                        onClick = { onConfirmation(memoMsg) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Send Request")
                    }
                }
            }
        }

    }
}