package souldestroyer.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import souldestroyer.settings.ThemeState

@Composable
fun NavBarThemeSettings(
    modifier: Modifier = Modifier,
    globalThemeState: ThemeState = ThemeState.instance()
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    globalThemeState.changeThemeIsDark()
                },
                modifier = Modifier.size(46.dp)
            ) {
                val icon =
                    if (globalThemeState.state.isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(42.dp)
                )
            }
            Text(
                text = if (globalThemeState.state.isDark) "Light Mode" else "Dark Mode",
                style = MaterialTheme.typography.labelSmall
            )
        }

        Column {
            ColorSelector(globalThemeState)
            PaletteSelector(globalThemeState)
        }
    }
}

@Composable
private fun ColorSelector(
    globalThemeState: ThemeState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArrowIconButton(
            direction = ArrowIconButtonDirection.LEFT,
            onClick = {
                globalThemeState.changeThemeSeedColorFromNavBar(ArrowIconButtonDirection.LEFT)
            }
        )

        Column {
            Icon(
                imageVector = Icons.Outlined.Palette,
                contentDescription = null
            )
            Text(
                text = "Color",
                style = MaterialTheme.typography.labelSmall
            )
        }

        ArrowIconButton(
            direction = ArrowIconButtonDirection.RIGHT,
            onClick = {
                globalThemeState.changeThemeSeedColorFromNavBar(ArrowIconButtonDirection.RIGHT)
            }
        )
    }
}

@Composable
private fun PaletteSelector(
    globalThemeState: ThemeState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArrowIconButton(
            direction = ArrowIconButtonDirection.LEFT,
            onClick = {
                globalThemeState.changeThemePaletteFromNavBar(ArrowIconButtonDirection.LEFT)
            }
        )

        Column {
            Icon(
                imageVector = Icons.Outlined.Style,
                contentDescription = null
            )
            Text(
                text = "Style",
                style = MaterialTheme.typography.labelSmall
            )
        }

        ArrowIconButton(
            direction = ArrowIconButtonDirection.RIGHT,
            onClick = {
                globalThemeState.changeThemePaletteFromNavBar(ArrowIconButtonDirection.RIGHT)
            }
        )
    }
}

@Composable
private fun ArrowIconButton(
    modifier: Modifier = Modifier,
    direction: ArrowIconButtonDirection,
    onClick: () -> Unit
) {
    val icon = when (direction) {
        ArrowIconButtonDirection.LEFT -> Icons.Outlined.ChevronLeft
        ArrowIconButtonDirection.RIGHT -> Icons.Outlined.ChevronRight
    }
    IconButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Icon(icon, contentDescription = null)
    }
}

enum class ArrowIconButtonDirection {
    LEFT,
    RIGHT
}