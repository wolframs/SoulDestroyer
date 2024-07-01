package souldestroyer.sol

import foundation.metaplex.solana.programs.MemoProgram
import foundation.metaplex.solana.transactions.SolanaTransactionBuilder
import foundation.metaplex.solana.transactions.Transaction
import foundation.metaplex.solanapublickeys.PublicKey
import souldestroyer.SoulDestroyer
import souldestroyer.logs.LogRepository

object Transactioneer {
    private val logRepo = LogRepository.instance()
    private val rpc = SoulDestroyer.instance().solana.rpc

    suspend fun buildMemoTransaction(
        memo: String,
        publicKey: PublicKey,
        signer: HotSigner
    ): Transaction {
        val recentBlockHash = rpc.getLatestBlockhash(null).blockhash
        return SolanaTransactionBuilder()
            .addInstruction(
                MemoProgram.writeUtf8(
                    publicKey, //PublicKey(wfWallet.publicKey),
                    memo
                )
            )
            .setRecentBlockHash(recentBlockHash)
            .setSigners(
                listOf(signer)
            )
            .build()
    }
}