package souldestroyer.sol.model

enum class ConfirmationStatus(val description: String) {
    ERROR("error"),
    PROCESSED("processed"),
    CONFIRMED("confirmed"),
    FINALIZED("finalized")
}