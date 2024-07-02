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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.materialkolor.DynamicMaterialThemeState
import com.materialkolor.PaletteStyle
import souldestroyer.settings.SettingsManager
import kotlin.enums.EnumEntries

@Composable
fun ThemeSettings(
    modifier: Modifier = Modifier,
    state: DynamicMaterialThemeState,
    sampleColors: List<Color>
) {
//    val borderColor = NavigationBarDefaults.containerColor
    Row(
//        modifier = modifier
//            .drawBehind {
//
//                val strokeWidth = 1.dp.toPx()
//                val y = 0f// + strokeWidth
//
//                drawLine(
//                    borderColor,
//                    Offset(0f, y),
//                    Offset(size.width, y),
//                    strokeWidth
//                )
//            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    changeThemeIsDark(state)
                },
                modifier = Modifier.size(46.dp)
            ) {
                val icon =
                    if (state.isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(42.dp)
                )
            }
            Text(
                text = if (state.isDark) "Light Mode" else "Dark Mode",
                style = MaterialTheme.typography.labelSmall
            )
        }

        Column {
            ColorSelector(state, sampleColors)
            PaletteSelector(state)
        }
    }
}

private fun changeThemeIsDark(state: DynamicMaterialThemeState) {
    state.isDark = !state.isDark
    SettingsManager().darkTheme = state.isDark
}

@Composable
private fun ColorSelector(
    state: DynamicMaterialThemeState,
    sampleColors: List<Color>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArrowIconButton(
            direction = ArrowIconButtonDirection.LEFT,
            onClick = {
                changeThemeSeedColor(sampleColors, state, ArrowIconButtonDirection.LEFT)
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
                changeThemeSeedColor(sampleColors, state, ArrowIconButtonDirection.RIGHT)
            }
        )
    }
}

private fun changeThemeSeedColor(
    sampleColors: List<Color>,
    state: DynamicMaterialThemeState,
    direction: ArrowIconButtonDirection
) {
    var indexOfSeedColor = sampleColors.indexOf(state.seedColor)

    when (direction) {
        ArrowIconButtonDirection.LEFT -> {
            state.seedColor = sampleColors.getOrNull(sampleColors.indexOf(state.seedColor) - 1)
                ?: sampleColors[sampleColors.size - 1]
        }

        ArrowIconButtonDirection.RIGHT -> {
            state.seedColor = sampleColors.getOrNull(indexOfSeedColor + 1)
                ?: sampleColors[0]
        }
    }

    indexOfSeedColor = sampleColors.indexOf(state.seedColor)
    SettingsManager().themeColor = indexOfSeedColor
}

@Composable
private fun PaletteSelector(
    state: DynamicMaterialThemeState
) {
    val styleEntries = PaletteStyle.entries
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArrowIconButton(
            direction = ArrowIconButtonDirection.LEFT,
            onClick = {
                changeThemePalette(styleEntries, state, ArrowIconButtonDirection.LEFT)
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
                changeThemePalette(styleEntries, state, ArrowIconButtonDirection.RIGHT)
            }
        )
    }
}

private fun changeThemePalette(
    styleEntries: EnumEntries<PaletteStyle>,
    state: DynamicMaterialThemeState,
    direction: ArrowIconButtonDirection
) {
    var indexOfPaletteEntry = styleEntries.indexOf(state.style)

    when (direction) {
        ArrowIconButtonDirection.LEFT -> {
            state.style = styleEntries.getOrNull(indexOfPaletteEntry - 1)
                ?: styleEntries[styleEntries.size - 1]
        }

        ArrowIconButtonDirection.RIGHT -> {
            state.style = styleEntries.getOrNull(indexOfPaletteEntry + 1)
                ?: styleEntries[0]
        }
    }

    indexOfPaletteEntry = styleEntries.indexOf(state.style)
    SettingsManager().themePalette = indexOfPaletteEntry
}

@Composable
private fun ArrowIconButton(
    modifier: Modifier = Modifier.size(20.dp),
    direction: ArrowIconButtonDirection,
    onClick: () -> Unit
) {
    val icon = when (direction) {
        ArrowIconButtonDirection.LEFT -> Icons.Outlined.ChevronLeft
        ArrowIconButtonDirection.RIGHT -> Icons.Outlined.ChevronRight
    }
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(icon, contentDescription = null)
    }
}

enum class ArrowIconButtonDirection {
    LEFT,
    RIGHT
}