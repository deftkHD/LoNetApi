package de.deftk.lonet.api.request

import de.deftk.lonet.api.model.Group
import de.deftk.lonet.api.model.User
import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.model.abstract.IContext
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.request.handler.DefaultRequestHandler
import de.deftk.lonet.api.request.handler.IRequestHandler
import de.deftk.lonet.api.response.ApiResponse
import de.deftk.lonet.api.platform.CryptoUtil
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.random.Random

open class AuthRequest(val requestUrl: String) : ApiRequest() {

    fun addChangePasswordRequest(str: String) {
        val obj = buildJsonObject {
            put("password", str)
        }
        addRequest("change_password", obj)
    }

    fun addLoginPasswordRequest(login: String, password: String) {
        val obj = buildJsonObject {
            put("login", login)
            put("password", password)
            put("get_miniature", true)
        }
        addRequest("login", obj)
    }

    fun addRegisterMasterRequest(title: String, ident: String) {
        val obj = buildJsonObject {
            put("remote_application", "wwa")
            put("remote_title", title)
            put("remote_ident", ident)
        }
        addRequest("register_master", obj)
    }

    fun addUnregisterMasterRequest() {
        addRequest("unregister_master", null)
    }

    //TODO add all parameters
    fun addGetUrlForAutologinRequest(targetUrlPath: String?) {
        val obj = buildJsonObject {
            put("enslave_session", true)
            put("disable_reception_of_quick_messages", true)
            put("disable_logout", true)
            put("ping_master", 1)
            if (targetUrlPath?.isNotEmpty() == true)
                put("target_url_path", targetUrlPath)
        }
        addRequest("get_url_for_autologin", obj)
    }

    fun addLoginNonceRequest(login: String, token: String, nonceId: String, nonceKey: String) {
        val chars = "0123456789qwertyuiopasdfghjklzxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
        val salt = StringBuilder()
        val size = Random.nextInt(5) + 8
        for (i in 0 until size) {
            salt.append(chars[Random.nextInt(62)])
        }
        val obj = buildJsonObject {
            put("application", "wwa")
            put("hash", CryptoUtil.sha256Hash("$nonceKey$salt$token"))
            put("salt", salt.toString())
            put("nonce_id", nonceId)
            put("algorithm", "sha256")
            put("login", login)
            put("get_miniature", true)
        }

        addRequest("login", obj)
    }

    fun addGetNonceRequest() {
        addRequest("get_nonce", null)
    }

    fun addGetProfileRequest() {
        val obj = buildJsonObject {
            put("export_image", 1)
        }
        addRequest("get_profile", obj)
    }

    override fun fireRequest(context: IContext): ApiResponse {
        error("Operation not supported!")
    }

    fun fireRequest(): ApiResponse {
        return super.fireRequest(AuthContext(requestUrl))
    }

    class AuthContext(private val apiUrl: String) : IContext {

        private var requestHandler: IRequestHandler = DefaultRequestHandler()

        override fun getSessionId(): String {
            error("Operation not supported!")
        }

        override fun setSessionId(sessionId: String) {
            error("Operation not supported!")
        }

        override fun getUser(): User {
            error("Operation not supported!")
        }

        override fun getGroups(): List<Group> {
            error("Operation not supported!")
        }

        override fun getOperator(login: String): AbstractOperator {
            error("Operation not supported!")
        }

        override fun getOrCreateManageable(jsonObject: JsonObject): IManageable {
            error("Operation not supported!")
        }

        override fun getRequestUrl(): String {
            return apiUrl
        }

        override fun getRequestHandler(): IRequestHandler {
            return requestHandler
        }

        override fun setRequestHandler(requestHandler: IRequestHandler) {
            this.requestHandler = requestHandler
        }
    }

}