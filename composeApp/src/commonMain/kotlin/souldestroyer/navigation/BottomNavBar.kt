package souldestroyer.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.materialkolor.DynamicMaterialThemeState

@Composable
fun BottomNavBar(
    themeState: DynamicMaterialThemeState,
    sampleColors: List<Color>,
    navController: NavController,
    mainScreens: List<Screen>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        fun saveScreenSelection(newActiveScreen: Screen) {
            mainScreens.find { it == newActiveScreen }!!.selected.value = true
            mainScreens.filter { it != newActiveScreen }.forEach {
                it.selected.value = false
            }
        }

        mainScreens.forEach { screen ->
            NavigationBarItem(
                selected = screen.selected.value,
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(screen.route)
                    saveScreenSelection(screen)
                },
                icon = {
                    Icon(
                        imageVector = screen.iconImageVector,
                        contentDescription = "DebugScreen Navigation Button"
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        modifier = Modifier
                            .offset(y = 10.dp)
                            .height(18.dp)
                    )
                }
            )
        }

        ThemeSettings(
            modifier = Modifier,//.fillMaxWidth(),
            state = themeState,
            sampleColors = sampleColors
        )
    }
}