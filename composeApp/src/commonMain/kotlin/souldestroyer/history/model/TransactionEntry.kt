package souldestroyer.history.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import foundation.metaplex.solana.transactions.TransactionSignature
import kotlinx.datetime.LocalDateTime
import souldestroyer.logs.getLocalDateTimeNow
import souldestroyer.sol.model.ConfirmationStatus

@Entity
data class TransactionEntry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val sentDateTime: LocalDateTime = getLocalDateTimeNow(),
    val signature: TransactionSignature,
    var status: ConfirmationStatus,
    var confirmations: Int,

    )
