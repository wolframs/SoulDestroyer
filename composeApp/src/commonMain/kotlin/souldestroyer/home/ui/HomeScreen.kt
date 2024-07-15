package souldestroyer.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import souldestroyer.navigation.Screen
import souldestroyer.shared.ui.ScrollableScreenColumnWithHeadline

@Serializable
object HomeScreen : Screen {
    override val label: String = "Home"
    override val route: String = "/home"
    override val iconImageVector: ImageVector = Icons.Filled.Home
    override var selected = mutableStateOf(true)
}

@Composable
fun MainScreen(paddingValues: PaddingValues) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        ScrollableScreenColumnWithHeadline(
            modifier = Modifier.fillMaxHeight(),
            paddingValues = paddingValues,
            headline = "Soul Destroyer",
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //InputField()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val screenHeight = this@BoxWithConstraints.constraints.maxHeight
                var imageHeight by remember { mutableStateOf(0) }

                val didYouKnowTexts = listOf(
                    "It's impossible to release a Solana trading application without bespoke AI generated images!",
                    "The Solana blockchain is the fastest blockchain to burn your money in the world!",
                    "Throwing away all of your money to finally get that lucky trade and make it back in a single instant is the best strategy!",
                    "Not touching your savings is for losers - the people around you will agree!",
                    "If you don't have much income, you can always take out a loan to trade with!",
                    "Common Solana Trading Bots are only called bots, because they're telegram bots. They have no intelligence of their own.",
                    "The name of this application was inspired by the Soul Ripper from the Supreme Commander game series and a popular telegram bot.",
                    "The Soul Destroyer application is designed to allow you to trade away your soul to the gods of the chain with maximum technical transparency.",
                    "If this application had been tested half as well as there was effort put into these texts, it would be safe to use.",
                    "Yes, this Home screen IS actually useless, unless you're big into the images and texts."
                )

                var didYouKnowText by remember { mutableStateOf(didYouKnowTexts.random()) }
                var currentDidYouKnowText = didYouKnowText

                SoulDestroyerWallpaperImage(
                    modifier = Modifier
                        .heightIn(max = if (imageHeight == 0) screenHeight.dp else imageHeight.dp),
                    onNextRandom = {
                        didYouKnowText = didYouKnowTexts.dropWhile { it == currentDidYouKnowText }.random()
                        currentDidYouKnowText = didYouKnowText
                    }
                )

                val density = LocalDensity.current
                LaunchedEffect(screenHeight, density) {
                    with(density) {
                        val paddingVertical =
                            paddingValues.calculateTopPadding().toPx() + paddingValues.calculateBottomPadding().toPx()
                        imageHeight =
                            (screenHeight - (paddingVertical + 220.dp.toPx())).toInt() // 120 -> remove the height of the headline and outer column paddings, leave space for text below
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Did you know?",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 72.dp, end = 72.dp)
                )
                Text(
                    text = didYouKnowText,
                    modifier = Modifier.padding(start = 72.dp, end = 72.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
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