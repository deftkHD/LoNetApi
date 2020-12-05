package de.deftk.lonet.api.request.handler

import de.deftk.lonet.api.exception.ApiException
import de.deftk.lonet.api.model.abstract.IContext
import de.deftk.lonet.api.platform.NetworkUtil
import de.deftk.lonet.api.request.ApiRequest
import de.deftk.lonet.api.response.ApiResponse
import kotlinx.serialization.json.*

abstract class AbstractRequestHandler: IRequestHandler {

    protected fun performJsonApiRequestIntern(request: ApiRequest, context: IContext): ApiResponse {
        val responses = mutableListOf<ApiResponse>()
        request.requests.forEach { requestBlock ->
            val serverUrl = context.getRequestUrl()
            val requestStr = requestBlock.toString()

            val response = NetworkUtil.postRequest(
                serverUrl,
                15000L,
                "application/json",
                requestStr.encodeToByteArray()
            )
            responses.add(ApiResponse(response.response, response.code))
        }
        val remappedResponses = responses.withIndex().map { (index, response) ->
            val responseJson = response.toJson()
            if (responseJson !is JsonArray) {
                val errorObject = responseJson.jsonObject["error"]?.jsonObject
                if (errorObject != null) {
                    throw ApiException("Internal error (${errorObject["code"]?.jsonPrimitive?.int}): ${errorObject["message"]?.jsonPrimitive}")
                } else {
                    throw ApiException("Internal error: No error object, but failure")
                }
            }
            JsonArray(responseJson.jsonArray.map { json ->
                // remap id
                val obj = json.jsonObject
                val newId = index * (ApiRequest.SUB_REQUESTS_PER_REQUEST + 1) + obj["id"]!!.jsonPrimitive.int
                buildJsonObject {
                    obj.forEach { (key, value) ->
                        if (key != "id") {
                            put(key, value)
                        } else {
                            put("id", newId)
                        }
                    }
                }
            })
        }.flatten()
        val dstResponse = buildJsonArray {
            remappedResponses.forEach { response ->
                add(response)
            }
        }
        return ApiResponse(dstResponse.toString(), responses.last().code)
    }

}