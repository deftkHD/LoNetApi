package de.deftk.lonet.api.model.feature.courselets

import de.deftk.lonet.api.model.Group
import de.deftk.lonet.api.request.GroupApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class Mapping(val id: Int, name: String, val group: Group) {

    companion object {
        fun fromJson(jsonObject: JsonObject, group: Group): Mapping {
            return Mapping(
                    jsonObject["id"]!!.jsonPrimitive.int,
                    jsonObject["name"]!!.jsonPrimitive.toString(),
                    group
            )
        }
    }

    var name = name
        private set

    fun edit(name: String) {
        val request = GroupApiRequest(group)
        val id = request.addSetCourseletMappingRequest(id, name)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse["mapping"]!!.jsonObject)
    }

    fun delete() {
        val request = GroupApiRequest(group)
        request.addDeleteCourseletMappingRequest(id)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    private fun readFrom(jsonObject: JsonObject) {
        name = jsonObject["name"]!!.jsonPrimitive.toString()
    }

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Mapping

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }


}