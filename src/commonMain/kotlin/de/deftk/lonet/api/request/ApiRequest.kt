package de.deftk.lonet.api.request

import de.deftk.lonet.api.model.abstract.IContext
import de.deftk.lonet.api.response.ApiResponse
import kotlinx.serialization.json.*

open class ApiRequest(var requests: MutableList<MutableList<JsonObject>> = mutableListOf(mutableListOf())) {

    companion object {
        const val SUB_REQUESTS_PER_REQUEST = 30
    }

    private fun addRequest(requestName: String, id: Int, request: JsonObject): Int {
        //TODO try to avoid double setFocus, setSession, ... requests
        val obj = buildJsonObject {
            put("jsonrpc", "2.0")
            put("method", requestName)
            put("id", id)
            put("params", request)
        }
        currentRequest().add(obj)
        return (requests.size - 1) * (SUB_REQUESTS_PER_REQUEST + 1) + id
    }

    fun addRequest(requestName: String, request: JsonObject?): Int {
        return addRequest(requestName, currentRequest().size + 2 /* place holder for setSession request */, request ?: buildJsonObject { })
    }

    private fun addSetSessionRequest(sessionId: String): Int {
        val obj = buildJsonObject {
            put("session_id", sessionId)
        }
        return addRequest("set_session", currentRequest().size + 1, obj)
    }

    fun addSetFocusRequest(scope: String?, login: String?): Int {
        val obj = buildJsonObject {
            if (scope != null)
                put("object", scope)
            if (login != null)
                put("login", login)
        }
        return addRequest("set_focus", obj)
    }

    fun addIdRequest(id: String, requestName: String): Int {
        val obj = buildJsonObject {
            put("id", id)
        }
        return addRequest(requestName, obj)
    }

    fun addGetInformationRequest(): Int {
        return addRequest("get_information", 999, buildJsonObject {  })
    }

    open fun fireRequest(context: IContext): ApiResponse {
        if (context !is AuthRequest.AuthContext) {
            val requests = this.requests
            this.requests = mutableListOf()
            requests.forEach { request ->
                this.requests.add(mutableListOf())
                addSetSessionRequest(context.getSessionId())
                currentRequest().addAll(request)
            }
        }
        return context.getRequestHandler().performRequest(this, context)
    }

    protected fun asApiBoolean(boolean: Boolean): Int {
        return if (boolean) 1 else 0
    }

    private fun currentRequest(): MutableList<JsonObject>  {
        return requests.last()
    }

    protected fun ensureCapacity(size: Int) {
        if (currentRequest().size + size > SUB_REQUESTS_PER_REQUEST) {
            requests.add(mutableListOf())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ApiRequest

        if (requests != other.requests) return false

        return true
    }

    override fun hashCode(): Int {
        return requests.hashCode()
    }

}