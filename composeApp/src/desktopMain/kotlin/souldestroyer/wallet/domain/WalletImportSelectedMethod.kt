package souldestroyer.wallet.domain

enum class WalletImportSelectedMethod(val string: String) {
    MNEMONIC("From Word List"),
    PRIVATE_KEY("From Private Key"),
    BYTE_ARRAY("From Byte Secret")
}