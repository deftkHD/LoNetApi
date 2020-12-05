package de.deftk.lonet.api.platform

import java.security.MessageDigest
import java.util.*

actual object CryptoUtil {

    actual fun encodeToString(data: ByteArray): String {
        return Base64.getEncoder().encodeToString(data)
    }

    actual fun sha256Hash(str: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.reset()
            digest.update(str.toByteArray(Charsets.UTF_8))
            return byteToHex(digest.digest() ?: ByteArray(0))
        } catch (e: Exception) {
            e.printStackTrace()
            str
        }
    }

    private fun byteToHex(bytes: ByteArray): String {
        val formatter = Formatter()
        bytes.forEach { byte ->
            formatter.format("%02x", byte)
        }
        val formatted = formatter.toString()
        formatter.close()
        return formatted
    }

}