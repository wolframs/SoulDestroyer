package souldestroyer.settings

import com.materialkolor.PaletteStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import souldestroyer.sol.RPCEndpoint
import java.util.prefs.Preferences

class SettingsManager {
    private val preferences: Preferences =
        Preferences.userNodeForPackage(SettingsManager::class.java)

    companion object {
        private const val KEY_RPC_ENDPOINT = "rpc_endpoint"
        private const val KEY_CUSTOM_ENDPOINT = "custom_endpoint"
        private const val KEY_THEME_COLOR = "theme_color"
        private const val KEY_THEME_PALETTE = "theme_palette"
        private const val KEY_THEME_DARK = "theme_dark"
        private const val KEY_LOGS_SHOW_VERBOSE = "logs_show_verbose"
    }

    // RPC ENDPOINT
    var rpcEndpoint: RPCEndpoint
        get() = RPCEndpoint.entries[preferences.getInt(KEY_RPC_ENDPOINT, 0)]
        set(value) {
            preferences.putInt(KEY_RPC_ENDPOINT, RPCEndpoint.entries.indexOf(value))
        }

    // THEME COLOR
    private val _themeColorFlow = MutableStateFlow(themeColor)
    val themeColorFlow: Flow<Int> get() = _themeColorFlow

    var themeColor: Int
        get() = preferences.getInt(KEY_THEME_COLOR, 7)
        set(value) {
            preferences.putInt(KEY_THEME_COLOR, value)
            _themeColorFlow.value = value
        }

    // THEME PALETTE
    private val _themePaletteFlow = MutableStateFlow(themePalette)
    val themePaletteFlow: Flow<Int> get() = _themePaletteFlow

    var themePalette: Int
        get() = preferences.getInt(KEY_THEME_PALETTE, PaletteStyle.entries.indexOf(PaletteStyle.TonalSpot))
        set(value) {
            preferences.putInt(KEY_THEME_PALETTE, value)
            _themePaletteFlow.value = value
        }

    private val _themeIsDarkFlow = MutableStateFlow(darkTheme)
    val themeIsDarkFlow: Flow<Boolean> get() = _themeIsDarkFlow

    // THEME DARK
    var darkTheme: Boolean
        get() = preferences.getBoolean(KEY_THEME_DARK, false)
        set(value) {
            preferences.putBoolean(KEY_THEME_DARK, value)
            _themeIsDarkFlow.value = value
        }

    // LOGS SHOW VERBOSE
    private val _showVerboseLogsFlow = MutableStateFlow(showVerboseLogs)
    val showVerboseLogsFlow: Flow<Boolean> get() = _showVerboseLogsFlow

    var showVerboseLogs: Boolean
        get() = preferences.getBoolean(KEY_LOGS_SHOW_VERBOSE, false)
        set(value) {
            preferences.putBoolean(KEY_LOGS_SHOW_VERBOSE, value)
            _showVerboseLogsFlow.value = value
        }


    // CLEAR SETTINGS
    fun clearSettings() {
        preferences.clear()
    }
}