package souldestroyer.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitcoinj.core.Base58

@Entity
data class WfWallet(
    @PrimaryKey val publicKey: String,
    var privateKey: ByteArray,
    var tag: String,
    var balance: Double = 0.0
) {
    override fun toString(): String {
        val encodedSecret = Base58.encode(privateKey).substring(0..5) + "***..."
        return "$tag \nPublic Key: $publicKey \nSecret: $encodedSecret)"
    }
}
