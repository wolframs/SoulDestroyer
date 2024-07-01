package souldestroyer.settings

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
    }

    var rpcEndpoint: RPCEndpoint
        get() = RPCEndpoint.entries[preferences.getInt(KEY_RPC_ENDPOINT, 0)]
        set(value) {
            preferences.putInt(KEY_RPC_ENDPOINT, RPCEndpoint.entries.indexOf(value))
        }

    var themeColor: Int?
        get() = preferences.getInt(KEY_THEME_COLOR, 0)
        set(value) {
            preferences.putInt(KEY_THEME_COLOR, value ?: 0)
        }

    var themePalette: Int?
        get() = preferences.getInt(KEY_THEME_PALETTE, 0)
        set(value) {
            preferences.putInt(KEY_THEME_PALETTE, value ?: 0)
        }

    fun clearSettings() {
        preferences.clear()
    }
}