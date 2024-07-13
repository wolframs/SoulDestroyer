package souldestroyer.sol

import org.bitcoinj.core.Base58
import souldestroyer.logs.LogEntryType
import souldestroyer.logs.LogRepository

object WalletCoder {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun encodeJsonFileWalletByteArrayPrivKey(inputString: String, privateKeyString: String): String {
        val privateKeyString1: String
        val bracketsRemovedString = inputString.removeSurrounding("[", "]")
        val intList = bracketsRemovedString.split(",").map { it.trim().toInt() } // Convert to Int first

        val byteList = mutableListOf<Byte>()
        intList.forEach { int ->
            // Convert each integer to a Byte, ensuring it stays within the valid Byte range
            val ubyte = int.toByte()
            byteList.add(ubyte)
        }

        return try {
            val byteArray = byteList.toByteArray()
            privateKeyString1 = Base58.encode(byteArray)
            println("Base58 Encoded Private Key: $privateKeyString1")
            privateKeyString1
        } catch (e: Throwable) {
            LogRepository.instance().log(
                message = e.message ?: "Byte Array Encoding failed.",
                type = LogEntryType.ERROR
            )
            ""
        }
    }
}