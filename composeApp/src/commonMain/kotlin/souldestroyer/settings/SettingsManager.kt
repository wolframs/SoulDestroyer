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

    var rpcEndpoint: RPCEndpoint
        get() = RPCEndpoint.entries[preferences.getInt(KEY_RPC_ENDPOINT, 0)]
        set(value) {
            preferences.putInt(KEY_RPC_ENDPOINT, RPCEndpoint.entries.indexOf(value))
        }

    var themeColor: Int
        get() = preferences.getInt(KEY_THEME_COLOR, 7)
        set(value) {
            preferences.putInt(KEY_THEME_COLOR, value)
        }

    var themePalette: Int
        get() = preferences.getInt(KEY_THEME_PALETTE, PaletteStyle.entries.indexOf(PaletteStyle.TonalSpot))
        set(value) {
            preferences.putInt(KEY_THEME_PALETTE, value)
        }

    var darkTheme: Boolean
        get() = preferences.getBoolean(KEY_THEME_DARK, false)
        set(value) {
            preferences.putBoolean(KEY_THEME_DARK, value)
        }

    private val _showVerboseLogsFlow = MutableStateFlow(showVerboseLogs)
    val showVerboseLogsFlow: Flow<Boolean> get() = _showVerboseLogsFlow

    var showVerboseLogs: Boolean
        get() = preferences.getBoolean(KEY_LOGS_SHOW_VERBOSE, false)
        set(value) {
            preferences.putBoolean(KEY_LOGS_SHOW_VERBOSE, value)
            _showVerboseLogsFlow.value = value
        }

    fun clearSettings() {
        preferences.clear()
    }
}