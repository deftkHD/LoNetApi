package de.deftk.lonet.api.request.handler

import de.deftk.lonet.api.Credentials
import de.deftk.lonet.api.LoNet
import de.deftk.lonet.api.exception.ApiException
import de.deftk.lonet.api.model.User
import de.deftk.lonet.api.model.abstract.IContext
import de.deftk.lonet.api.request.ApiRequest
import de.deftk.lonet.api.response.ApiResponse
import de.deftk.lonet.api.response.ResponseUtil
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

class AutoLoginRequestHandler(private val handler: LoginHandler) : AbstractRequestHandler() {

    override fun performRequest(request: ApiRequest, context: IContext): ApiResponse {
        return try {
            val response = performJsonApiRequestIntern(request, context)
            ResponseUtil.checkSuccess(response.toJson())
            response
        } catch (e: ApiException) {
            if (e.message?.contains("Session key not valid") == true) {
                val user = LoNet.login(handler.getCredentials())
                performJsonApiRequestIntern(ApiRequest(request.requests.map {  methodList ->
                    val targetMethods = methodList.filter { it.jsonObject["method"]?.jsonPrimitive?.toString() == "set_session" }
                    targetMethods.map { setSessionMethod ->
                        buildJsonObject {
                            setSessionMethod.jsonObject["params"]!!.jsonObject.entries.forEach { (key, value ) ->
                                if (key == "session_id") {
                                    put("session_id", user.getContext().getSessionId())
                                } else {
                                    put(key, value)
                                }
                            }
                        }
                    }.toMutableList()
                }.toMutableList()), user.getContext())
            } else throw e
        }
    }

    interface LoginHandler {
        fun getCredentials(): Credentials
        fun onLogin(user: User)
    }

}