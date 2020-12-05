package de.deftk.lonet.api.model.feature.courselets

import de.deftk.lonet.api.model.Group
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.request.GroupApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.getApiDate
import de.deftk.lonet.api.utils.getBoolOrNull
import de.deftk.lonet.api.utils.getManageable
import kotlinx.serialization.json.*

class Courselet(val id: Int, val title: String, val mapping: String, val isLink: Boolean, val isVisible: Boolean, val isTemplate: Boolean, val size: Int, val creationDate: Long, val creationMember: IManageable, val modificationDate: Long, val modificationMember: IManageable, val group: Group) {

    companion object {
        fun fromJson(jsonObject: JsonObject, group: Group): Courselet {
            val createdObject = jsonObject["created"]!!.jsonObject
            val modifiedObject = jsonObject["modified"]!!.jsonObject
            return Courselet(
                    jsonObject["id"]!!.jsonPrimitive.int,
                    jsonObject["title"]!!.jsonPrimitive.toString(),
                    jsonObject["mapping"]!!.jsonPrimitive.toString(),
                    jsonObject.getBoolOrNull("is_link")!!,
                    jsonObject.getBoolOrNull("is_visible")!!,
                    jsonObject.getBoolOrNull("is_template")!!,
                    jsonObject["size"]!!.jsonPrimitive.int,
                    createdObject.getApiDate("date"),
                    createdObject.getManageable("user", group),
                    modifiedObject.getApiDate("date"),
                    modifiedObject.getManageable("user", group),
                    group
            )
        }
    }

    fun setSuspendData(suspendData: String, ifLatest: Int) {
        val request = GroupApiRequest(group)
        request.addSetCourseletSuspendDataRequest(id, suspendData, ifLatest)[1]
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    fun getResults(): CourseletData {
        val request = GroupApiRequest(group)
        val id = request.addGetCourseletResultsRequest(id)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return CourseletData.fromJson(subResponse["courselet"]!!.jsonObject, group, this)
    }

    fun deleteResults() {
        val request = GroupApiRequest(group)
        request.addDeleteCourseletResultsRequest(id)[1]
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    fun addBookmark() {
        val request = GroupApiRequest(group)
        request.addAddCourseletBookmarkRequest(id)[1]
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    fun deleteBookmark() {
        val request = GroupApiRequest(group)
        request.addDeleteCourseletBookmarkRequest(id)[1]
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    fun export(pkg: TemplatePackage? = null): CourseletDownload {
        val request = GroupApiRequest(group)
        val id = request.addExportCourseletRequest(this.id.toString(), pkg)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Json.decodeFromJsonElement(subResponse["file"]!!.jsonObject)
    }

    fun delete() {
        val request = GroupApiRequest(group)
        request.addDeleteCourseletRequest(this.id.toString())
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }


}