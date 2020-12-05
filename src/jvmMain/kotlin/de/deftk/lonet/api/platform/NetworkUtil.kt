package de.deftk.lonet.api.platform

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

actual object NetworkUtil {

    actual fun postRequest(requestUrl: String, timeout: Long, contentType: String, data: ByteArray): WebResponse {
        val url = URL(requestUrl)
        val connection = url.openConnection() as HttpsURLConnection
        connection.connectTimeout = 15000
        connection.requestMethod = "POST"
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.doOutput = true
        connection.doInput = true

        connection.outputStream.write(data)
        connection.outputStream.flush()
        connection.outputStream.close()

        val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
        val sb = StringBuilder()
        while (true) {
            val ln = reader.readLine() ?: break
            sb.append("$ln\n")
        }
        return WebResponse(sb.toString(), connection.responseCode)
    }

    actual fun urlEncodeUTF8(param: String): String {
        return URLEncoder.encode(param, "UTF-8")
    }

}