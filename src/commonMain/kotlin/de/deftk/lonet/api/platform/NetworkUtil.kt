package de.deftk.lonet.api.platform

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object NetworkUtil {

    fun postRequest(requestUrl: String, timeout: Long, contentType: String, data: ByteArray): WebResponse
    fun urlEncodeUTF8(param: String): String

}