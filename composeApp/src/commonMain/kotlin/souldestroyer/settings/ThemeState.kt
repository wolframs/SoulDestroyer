package souldestroyer.settings

import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialThemeState
import com.materialkolor.PaletteStyle
import souldestroyer.navigation.ArrowIconButtonDirection
import kotlin.enums.EnumEntries

class ThemeState private constructor() {
    companion object {
        private var instance: ThemeState? = null

        fun instance(): ThemeState {
            if (instance == null) {
                instance = ThemeState()
            }
            return instance!!
        }
    }

    private var _state: DynamicMaterialThemeState? = null
    var state: DynamicMaterialThemeState
        get() = _state!!
        set(value) {
            _state = value
        }

    fun changeThemeIsDark(state: DynamicMaterialThemeState) {
        state.isDark = !state.isDark
        SettingsManager().darkTheme = state.isDark
    }

    fun changeThemeSeedColorFromNavBar(
        seedColors: List<Color>,
        direction: ArrowIconButtonDirection
    ) {
        var indexOfSeedColor = seedColors.indexOf(state.seedColor)

        when (direction) {
            ArrowIconButtonDirection.LEFT -> {
                state.seedColor = seedColors.getOrNull(seedColors.indexOf(state.seedColor) - 1)
                    ?: seedColors[seedColors.size - 1]
            }

            ArrowIconButtonDirection.RIGHT -> {
                state.seedColor = seedColors.getOrNull(indexOfSeedColor + 1)
                    ?: seedColors[0]
            }
        }

        indexOfSeedColor = seedColors.indexOf(state.seedColor)
        SettingsManager().themeColor = indexOfSeedColor
    }

    fun changeThemePaletteFromNavBar(
        styleEntries: EnumEntries<PaletteStyle>,
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
}