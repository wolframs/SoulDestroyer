package souldestroyer.shared.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableScreenColumnWithHeadline(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    headline: String,
    verticalArrangement: Arrangement. Vertical = Arrangement.Top,
    horizontalAlignment: Alignment. Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(paddingValues)
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {

        Text(
            text = headline,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(24.dp))

        content()
    }
}