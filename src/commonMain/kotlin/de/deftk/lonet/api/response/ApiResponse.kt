package de.deftk.lonet.api.response

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

class ApiResponse(val raw: String, val code: Int) {

    fun toJson(): JsonElement {
        return Json.encodeToJsonElement(raw)
    }

}