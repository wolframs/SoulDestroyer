import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState
import org.jetbrains.compose.ui.tooling.preview.Preview
import souldestroyer.logs.ui.SideDisplaySection
import souldestroyer.navigation.BottomNavBar
import souldestroyer.navigation.WfNavHost
import souldestroyer.navigation.mainScreens
import souldestroyer.settings.SeedColors
import souldestroyer.settings.SettingsManager
import souldestroyer.settings.ThemeState
import theme.AppTheme

@Composable
@Preview
fun App() {

    val isDarkTheme = SettingsManager().darkTheme
    val seedColor by rememberSaveable {
        mutableStateOf(SeedColors[SettingsManager().themeColor])
    }
    val style by rememberSaveable {
        mutableStateOf(PaletteStyle.entries[SettingsManager().themePalette])
    }

    val globalThemeState = ThemeState.instance()

    globalThemeState.state = rememberDynamicMaterialThemeState(
        seedColor = seedColor,
        isDark = isDarkTheme,
        style = style
    )

    AppTheme(
        state = globalThemeState.state
    ) {
        //var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            val navController = rememberNavController()
            Scaffold(
                modifier = Modifier,
                topBar = {},
                bottomBar = {
                    Row() {
                        BottomNavBar(
                            globalThemeState = globalThemeState,
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