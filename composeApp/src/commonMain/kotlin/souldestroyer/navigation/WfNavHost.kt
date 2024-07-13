package souldestroyer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import souldestroyer.history.ui.TransactionScreen
import souldestroyer.settings.ui.DebugScreen
import souldestroyer.settings.ui.SettingsScreen
import souldestroyer.main.MainScreen
import souldestroyer.wallet.WalletRepository
import souldestroyer.wallet.ui.CreateWalletScreen
import souldestroyer.wallet.ui.ImportWalletScreen
import souldestroyer.wallet.ui.WalletScreen

@Composable
fun WfNavHost(
    modifier: Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = MainScreen.route,
        modifier = modifier.padding(4.dp)
    ) {
        composable(route = MainScreen.route) {
            MainScreen(paddingValues = paddingValues)
        }
        composable(route = WalletScreen.route) {
            val walletListState by WalletRepository.instance().walletListModel.collectAsState()
            WalletScreen(
                paddingValues = paddingValues,
                wallets = walletListState.logList,
                onGoToCreateWalletScreen = {
                    navController.navigate(CreateWalletScreen.route)
                },
                onGoToImportWalletScreen = {
                    navController.navigate(ImportWalletScreen.route)
                }
            )
        }
        composable(route = TransactionScreen.route) {
            TransactionScreen(
                paddingValues = paddingValues
            )
        }
        composable(route = SettingsScreen.route) {
            DebugScreen(paddingValues = paddingValues)
        }
        composable(route = ImportWalletScreen.route) {
            ImportWalletScreen(
                paddingValues = paddingValues,
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = CreateWalletScreen.route) {
            CreateWalletScreen(
                paddingValues = paddingValues,
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}