package de.deftk.lonet.api

import de.deftk.lonet.api.exception.ApiException
import de.deftk.lonet.api.model.User
import de.deftk.lonet.api.platform.NetworkUtil
import de.deftk.lonet.api.request.AuthRequest
import de.deftk.lonet.api.response.ResponseUtil
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object LoNet {

    fun login(credentials: Credentials): User {
        return when {
            credentials.password != null -> login(credentials.username, credentials.password)
            credentials.token != null -> loginToken(credentials.username, credentials.token)
            else -> throw IllegalArgumentException("Password or token must be set")
        }
    }

    fun login(username: String, password: String): User {
        val responsibleHost = getApiUrl(username)
        val authRequest = AuthRequest(responsibleHost)
        authRequest.addLoginPasswordRequest(username, password)
        authRequest.addGetInformationRequest()
        val response = authRequest.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
        return User.fromResponse(response, responsibleHost, password)
    }

    fun loginCreateTrust(username: String, password: String, title: String, ident: String): User {
        val responsibleHost = getApiUrl(username)
        val authRequest = AuthRequest(responsibleHost)
        authRequest.addLoginPasswordRequest(username, password)
        authRequest.addSetFocusRequest("trusts", null)
        authRequest.addRegisterMasterRequest(title, ident)
        authRequest.addGetInformationRequest()
        val response = authRequest.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
        return User.fromResponse(
                response,
                responsibleHost,
                ResponseUtil.getSubResponseResultByMethod(response.toJson(), "register_master")["trust"]!!.jsonObject["token"]!!.jsonPrimitive.toString()
        )
    }

    fun loginToken(username: String, token: String, removeTrust: Boolean = false): User {
        val responsibleHost = getApiUrl(username)
        val nonceRequest = AuthRequest(responsibleHost)
        nonceRequest.addGetNonceRequest()
        val nonceResponse = nonceRequest.fireRequest()
        val nonceJson =
                nonceResponse.toJson().jsonArray[0].jsonObject["result"]!!.jsonObject["nonce"]!!.jsonObject
        val nonceId = nonceJson["id"]!!.jsonPrimitive.toString()
        val nonceKey = nonceJson["key"]!!.jsonPrimitive.toString()

        val authRequest = AuthRequest(responsibleHost)
        authRequest.addLoginNonceRequest(username, token, nonceId, nonceKey)
        if (removeTrust) {
            authRequest.addSetFocusRequest("trusts", null)
            authRequest.addUnregisterMasterRequest()
        }
        authRequest.addGetInformationRequest()
        val response = authRequest.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
        return User.fromResponse(response, responsibleHost, token)
    }

    fun getApiUrl(address: String): String {
        val request = AuthRequest("https://fork.webweaver.de/service/get_responsible_host.php")
        val response = NetworkUtil.postRequest(
            request.requestUrl,
            15000L,
            "application/x-www-form-urlencoded",
            serializeContent(mapOf(Pair("login", address)))
        ).response
        val indexOf: Int = response.indexOf("<host>") + 6
        val indexOf2: Int = response.indexOf("</host>")
        if (indexOf >= 0 && indexOf2 >= 0) {
            val urlBuilder = StringBuilder()
            urlBuilder.append("https://")
            urlBuilder.append(response.substring(indexOf, indexOf2))
            urlBuilder.append("/jsonrpc.php")
            return urlBuilder.toString()
        }
        throw ApiException("invalid get_responsible_host response")
    }

    private fun serializeContent(params: Map<String, String>): ByteArray {
        val sb = StringBuilder()
        var addAndOperator = false
        params.forEach { (key, value) ->
            if (addAndOperator)
                sb.append("&")
            else
                addAndOperator = true

            sb.append(NetworkUtil.urlEncodeUTF8(key))
            sb.append("=")
            sb.append(NetworkUtil.urlEncodeUTF8(value))
        }
        return sb.toString().encodeToByteArray()
    }

}