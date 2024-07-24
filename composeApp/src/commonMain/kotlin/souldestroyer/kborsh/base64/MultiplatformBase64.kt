/*
 * Multiplatform Base64
 * kBorsh
 *
 * Created by Funkatronics on 08/29/2023
 */

package souldestroyer.kborsh.base64

import com.funkatronics.encoders.Base64

object Base64Factory {
    fun createEncoder(): Base64Encoder = MultiMultBase64Encoder()
}

internal class MultiMultBase64Encoder : Base64Encoder {
    override fun decode(src: ByteArray): ByteArray = Base64.decode(src.decodeToString())
    override fun encode(src: ByteArray): ByteArray = Base64.encode(src)
}