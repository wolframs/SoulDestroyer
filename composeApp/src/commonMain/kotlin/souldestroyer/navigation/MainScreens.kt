package souldestroyer.navigation

import souldestroyer.history.ui.TransactionScreen
import souldestroyer.home.ui.HomeScreen
import souldestroyer.settings.ui.SettingsScreen
import souldestroyer.tokens.TokensScreen
import souldestroyer.wallet.ui.WalletScreen

val mainScreens: List<Screen> = listOf(
    HomeScreen,
    WalletScreen,
    TokensScreen,
    TransactionScreen,
    SettingsScreen
)