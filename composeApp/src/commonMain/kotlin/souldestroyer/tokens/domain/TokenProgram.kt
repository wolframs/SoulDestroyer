package souldestroyer.tokens.domain

import foundation.metaplex.solana.programs.Program
import foundation.metaplex.solana.transactions.AccountMeta
import foundation.metaplex.solana.transactions.TransactionInstruction
import foundation.metaplex.solanapublickeys.PublicKey
import souldestroyer.kborsh.kborsh.BorshEncoder

// **
//  * Generated based on SolanaKMP's SystemProgram.kt
// */
object TokenProgram : Program() {
    val PROGRAM_ID = PublicKey("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA")

    const val PROGRAM_INDEX_TRANSFER = 3
    const val PROGRAM_INDEX_TRANSFER_CHECKED = 12

    fun transferChecked(
        fromPublicKey: PublicKey,
        toPublicKey: PublicKey,
        ownerPublicKey: PublicKey,
        amount: Long,
        decimals: Int,
        mint: PublicKey
    ): TransactionInstruction {
        val keys = ArrayList<AccountMeta>()
        keys.add(AccountMeta(fromPublicKey, isSigner = false, isWritable = true))
        keys.add(AccountMeta(toPublicKey, isSigner = false, isWritable = true))
        keys.add(AccountMeta(ownerPublicKey, isSigner = true, isWritable = false))
        keys.add(AccountMeta(mint, isSigner = false, isWritable = false))

        // 4 bytes instruction index + 8 bytes amount + 1 byte decimals
        val data = BorshEncoder()
        data.encodeInt(PROGRAM_INDEX_TRANSFER_CHECKED)
        data.encodeLong(amount)
        data.encodeByte(decimals.toByte())

        return createTransactionInstruction(PROGRAM_ID, keys, data.borshEncodedBytes)
    }
}