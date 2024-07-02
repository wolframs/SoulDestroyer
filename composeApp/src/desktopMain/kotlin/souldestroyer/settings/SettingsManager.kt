package souldestroyer.settings

import com.materialkolor.PaletteStyle
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

    fun clearSettings() {
        preferences.clear()
    }
}