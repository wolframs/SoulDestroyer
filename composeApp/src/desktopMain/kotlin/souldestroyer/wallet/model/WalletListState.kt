package souldestroyer.wallet.model

import souldestroyer.database.entity.WfWallet

data class WalletListState(
    val logList: List<WfWallet> = listOf()
)
