package souldestroyer.wallet

import foundation.metaplex.solanaeddsa.Keypair
import foundation.metaplex.solanaeddsa.SolanaEddsa
import foundation.metaplex.solanapublickeys.PublicKey
import kotlinx.coroutines.runBlocking
import souldestroyer.logs.LogEntryType
import souldestroyer.logs.LogRepository

class SolKeypair(override val publicKey: PublicKey, override val secretKey: ByteArray) : Keypair {

    companion object {
        private val logRepo = LogRepository.instance()
        private val edDSA = SolanaEddsa

        fun fromPrivateKey(secretKey: ByteArray): SolKeypair {
            logRepo.log(
                message = "Calculating keypair from encoded secret key string...",
                type = LogEntryType.INFO
            )
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