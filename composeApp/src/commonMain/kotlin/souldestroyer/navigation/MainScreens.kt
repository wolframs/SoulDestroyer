package souldestroyer.navigation

import souldestroyer.history.ui.TransactionScreen
import souldestroyer.main.MainScreen
import souldestroyer.settings.ui.SettingsScreen
import souldestroyer.wallet.ui.WalletScreen

val mainScreens: List<Screen> = listOf(MainScreen, WalletScreen, TransactionScreen, SettingsScreen)