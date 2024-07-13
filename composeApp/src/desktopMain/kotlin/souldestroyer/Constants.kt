package souldestroyer

object Constants {
    /** 1 Lamport equals 0.000000001 SOL (1e-9)
     *
     * 1 * 10^9 = 1_000_000_000 */
    const val LAMPERTS_PER_SOL: Int = 1_000_000_000

    /** 500 ms */
    const val CONFIRM_POLLING_DELAY: Long = 500

    /** 90_000 ms */
    const val CONFIRM_MAX_POLLING_TIME: Int = 90_000

    /** 3_500 ms */
    const val BLOCKHASH_REFRESH_DELAY: Long = 3_500
}