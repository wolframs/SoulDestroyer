package souldestroyer.tokens.domain

import foundation.metaplex.solana.transactions.TransactionInstruction
import foundation.metaplex.solanapublickeys.PublicKey

fun createTokenTransferInstruction(
    fromPublicKey: PublicKey,
    toPublicKey: PublicKey,
    ownerPublicKey: PublicKey,
    amount: Long,
    mint: PublicKey,
    decimals: Int
): TransactionInstruction {
    return TokenProgram.transferChecked(
        fromPublicKey = fromPublicKey,
        toPublicKey = toPublicKey,
        ownerPublicKey = ownerPublicKey,
        amount = amount,
        decimals = decimals,
        mint = mint
    )
}