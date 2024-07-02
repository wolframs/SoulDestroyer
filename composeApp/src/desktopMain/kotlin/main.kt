import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() {
    application {
        Window(
            state = rememberWindowState(width = 1400.dp, height = 1000.dp),
            onCloseRequest = ::exitApplication,
            title = "souldestroyer.SoulDestroyer",
        ) {
            App()
        }
    }
}