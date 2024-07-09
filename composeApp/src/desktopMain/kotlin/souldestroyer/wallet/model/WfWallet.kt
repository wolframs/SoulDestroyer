package souldestroyer.wallet.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitcoinj.core.Base58

@Entity
data class WfWallet(
    @PrimaryKey val publicKey: String,
    var privateKey: ByteArray,
    var tag: String,
    var balance: Double = 0.0
)
