import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState
import souldestroyer.settings.SettingsScreen
import souldestroyer.logs.ui.SideDisplaySection
import souldestroyer.main.MainScreen
import souldestroyer.navigation.BottomNavBar
import souldestroyer.navigation.Screen
import souldestroyer.navigation.WfNavHost
import org.jetbrains.compose.ui.tooling.preview.Preview
import souldestroyer.SoulDestroyer
import souldestroyer.settings.SettingsManager
import theme.AppTheme
import souldestroyer.wallet.ui.WalletScreen

@Composable
@Preview
fun App() {
    SoulDestroyer.instance()

    val SampleColors = listOf(
        Color(0xFFD32F2F),
        Color(0xFFC2185B),
        Color(0xFF7B1FA2),
        Color(0xFF512DA8),
        Color(0xFF303F9F),
        Color(0xFF1976D2),
        Color(0xFF0288D1),
        Color(0xFF0097A7),
        Color(0xFF00796B),
        Color(0xFF388E3C),
        Color(0xFF689F38),
        Color(0xFFAFB42B),
        Color(0xFFFBC02D),
        Color(0xFFFFA000),
        Color(0xFFF57C00),
        Color(0xFFE64A19),
        Color(0xFF5D4037),
        Color(0xFF616161),
        Color(0xFF455A64),
        Color(0xFF263238),
    )

    val isDarkTheme = SettingsManager().darkTheme
    val seedColor by rememberSaveable {
        mutableStateOf(SampleColors[SettingsManager().themeColor ?: 7])
    }
    val style by rememberSaveable {
        mutableStateOf(PaletteStyle.entries[SettingsManager().themePalette])
    }
    val themeState = rememberDynamicMaterialThemeState(
        seedColor = seedColor,
        isDark = isDarkTheme,
        style = style,
    )

    AppTheme(
        state = themeState
    ) {
        val mainScreens: List<Screen> = listOf(MainScreen, WalletScreen, SettingsScreen)

        //var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            val navController = rememberNavController()
            Scaffold(
                modifier = Modifier,
                topBar = {},
                bottomBar = {
                    Row() {
                        BottomNavBar(
                            themeState = themeState,
                            sampleColors = SampleColors,
                            navController = navController,
                            mainScreens = mainScreens,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
            ) { paddingValues ->
                Row(Modifier.fillMaxSize()) {

                    WfNavHost(
                        modifier = Modifier
                            .weight(0.67f),
                        navController = navController,
                        paddingValues = paddingValues
                    )

                    SideDisplaySection(
                        modifier = Modifier
                            .weight(0.33f),
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}