package souldestroyer.settings

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

    fun changeThemeIsDark(newValue: Boolean? = null) {
        state.isDark = newValue ?: !state.isDark
        SettingsManager().darkTheme = state.isDark
    }

    fun changeThemeSeedColorFromNavBar(direction: ArrowIconButtonDirection) {
        var indexOfSeedColor = SeedColors.indexOf(state.seedColor)

        when (direction) {
            ArrowIconButtonDirection.LEFT -> {
                state.seedColor = SeedColors.getOrNull(SeedColors.indexOf(state.seedColor) - 1)
                    ?: SeedColors[SeedColors.size - 1]
            }

            ArrowIconButtonDirection.RIGHT -> {
                state.seedColor = SeedColors.getOrNull(indexOfSeedColor + 1)
                    ?: SeedColors[0]
            }
        }

        indexOfSeedColor = SeedColors.indexOf(state.seedColor)
        SettingsManager().themeColor = indexOfSeedColor
    }

    fun changeThemeSeedColorByListIndex(colorIndex: Int) {
        state.seedColor = SeedColors.getOrNull(colorIndex)
            ?: SeedColors[0]
        SettingsManager().themeColor = SeedColors.indexOf(state.seedColor)
    }

    fun changeThemePaletteFromNavBar(direction: ArrowIconButtonDirection) {
        val styleEntries: EnumEntries<PaletteStyle> = PaletteStyle.entries
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

    fun changeThemePaletteByListIndex(paletteIndex: Int) {
        state.style = PaletteStyle.entries.getOrNull(paletteIndex)
            ?: PaletteStyle.entries[0]
        SettingsManager().themePalette = PaletteStyle.entries.indexOf(state.style)
    }
}