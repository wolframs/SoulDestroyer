package souldestroyer.wallet

import foundation.metaplex.solanaeddsa.Keypair
import foundation.metaplex.solanaeddsa.SolanaEddsa
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.runBlocking
import org.bitcoinj.core.Base58
import souldestroyer.logs.model.LogEntryType
import souldestroyer.logs.LogRepository

class SolKeypair(override val publicKey: PublicKey, override val secretKey: ByteArray) : Keypair {

    companion object {
        private val logRepo = LogRepository.instance()
        private val edDSA = SolanaEddsa

        fun fromPrivateKey(privateKeyString: String): SolKeypair {
            return runBlocking {
                val secretAsByteArray = Base58.decode(privateKeyString)
                val keypair = edDSA.createKeypairFromSecretKey(secretAsByteArray)
                return@runBlocking SolKeypair(keypair.publicKey, keypair.secretKey)
            }
        }

        fun fromByteArray(secretKey: ByteArray): SolKeypair {
            return runBlocking {
                val keypair = edDSA.createKeypairFromSecretKey(secretKey)
                return@runBlocking SolKeypair(keypair.publicKey, keypair.secretKey)
            }
        }

        fun generate(): SolKeypair {
            logRepo.log(
                message = "Generating new keypair...",
                type = LogEntryType.INFO
            )
            return runBlocking {
                val keypair = edDSA.generateKeypair()
                SolKeypair(keypair.publicKey, keypair.secretKey)
            }
        }
    }
}