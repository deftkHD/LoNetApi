package de.deftk.lonet.api.response

import de.deftk.lonet.api.exception.ApiException
import kotlinx.serialization.json.*

object ResponseUtil {

    @Throws(ApiException::class)
    fun getSubResponse(response: JsonElement, id: Int): JsonObject {
        checkSuccess(response)
        response.jsonArray.map { it.jsonObject }.forEach { subResponse ->
            if (subResponse["id"]?.jsonPrimitive?.intOrNull == id)
                return subResponse
        }
        throw ApiException("invalid response (expecting sub response with id $id): $response")
    }

    @Throws(ApiException::class)
    fun getSubResponseResult(response: JsonElement, id: Int): JsonObject {
        return getSubResponse(response, id)["result"]!!.jsonObject
    }

    @Throws(ApiException::class)
    fun getSubResponseResultByMethod(response: JsonElement, method: String): JsonObject {
        checkSuccess(response)
        response.jsonArray.map { it.jsonObject }.forEach { subResponse ->
            val result = subResponse["result"]!!.jsonObject
            if (result["method"]?.jsonPrimitive.toString() == method) {
                return result
            }
        }
        throw ApiException("invalid response (expecting sub response with method $method): $response")
    }

    @Throws(ApiException::class)
    fun checkSuccess(response: JsonElement) {
        response.jsonArray.map { it.jsonObject }.forEach { subResponse ->
            val result = subResponse["result"]!!.jsonObject
            if (result.containsKey("return") && result["return"]!!.jsonPrimitive.toString() != "OK") {
                throw ApiException("Server returned error: ${result["error"]!!.jsonPrimitive}")
            }
        }
    }

}