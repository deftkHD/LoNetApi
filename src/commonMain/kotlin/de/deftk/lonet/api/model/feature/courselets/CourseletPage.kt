package de.deftk.lonet.api.model.feature.courselets

import de.deftk.lonet.api.model.Group
import de.deftk.lonet.api.request.GroupApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import kotlinx.serialization.json.*

class CourseletPage(val courselet: Courselet, val pageId: String, val title: String, val resultExists: Boolean, val maxScore: Int, val group: Group) {

    companion object {
        fun fromJson(jsonObject: JsonObject, group: Group, courselet: Courselet): CourseletPage {
            val resultObject = jsonObject["result"]!!.jsonObject
            return CourseletPage(
                    courselet,
                    jsonObject["page_id"]!!.jsonPrimitive.toString(),
                    jsonObject["title"]!!.jsonPrimitive.toString(),
                    resultObject["exists"]!!.jsonPrimitive.boolean,
                    resultObject["score_max"]!!.jsonPrimitive.int,
                    group
            )
        }
    }

    fun addResult(score: Int? = null, time: Long? = null) {
        val request = GroupApiRequest(group)
        request.addAddCourseletResultRequest(courselet.id, pageId, score, time)[1]
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    fun deleteResults() {
        val request = GroupApiRequest(group)
        request.addDeleteCourseletResultsRequest(courselet.id, pageId)[1]
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

}