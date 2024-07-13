package souldestroyer.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import souldestroyer.navigation.Screen
import souldestroyer.shared.ui.ScrollableScreenColumnWithHeadline

@Serializable
object MainScreen: Screen {
    override val label: String = "Home"
    override val route: String = "/main"
    override val iconImageVector: ImageVector = Icons.Filled.Home
    override var selected = mutableStateOf(true)
}

@Composable
fun MainScreen(paddingValues: PaddingValues) {
    ScrollableScreenColumnWithHeadline(
        paddingValues = paddingValues,
        headline = "Home"
    ) {
        InputField()
    }
}

@Composable
fun InputField() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Input field") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val clipboardText = clipboardManager.getText()
            if (clipboardText != null) {
                text = TextFieldValue(clipboardText.text)
            }
        }) {
            Text("Paste")
        }
    }
}