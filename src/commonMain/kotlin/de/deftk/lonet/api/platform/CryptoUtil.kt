package de.deftk.lonet.api.platform

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object CryptoUtil {

    fun encodeToString(data: ByteArray): String

    fun sha256Hash(str: String): String

}