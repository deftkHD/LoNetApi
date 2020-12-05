package de.deftk.lonet.api.model.feature.courselets

import de.deftk.lonet.api.model.Group
import de.deftk.lonet.api.utils.getApiDate
import de.deftk.lonet.api.utils.getBoolOrNull
import de.deftk.lonet.api.utils.getStringOrNull
import kotlinx.serialization.json.*

class CourseletConnection(val exists: Boolean, val creationDate: Long?, val modificationDate: Long?, val progressed: JsonObject?, val lastPageId: String?, val result: JsonObject?, val suspendData: String?, val isBookmarked: Boolean?, val isCorrected: Boolean?, val isConversation: Boolean?, val isUnread: Boolean?, val pages: List<CourseletPage>?, val courselet: Courselet, val group: Group) {

    companion object {
        fun fromJson(jsonObject: JsonObject, group: Group, courselet: Courselet): CourseletConnection {
            val createdObject = jsonObject["created"]!!.jsonObject
            val modifiedObject = jsonObject["modified"]!!.jsonObject
            if (jsonObject["exists"]!!.jsonPrimitive.boolean) {
                return CourseletConnection(
                        true,
                        createdObject.getApiDate("date"),
                        modifiedObject.getApiDate("date"),
                        jsonObject["progressed"]!!.jsonObject,
                        jsonObject["last_page_id"]!!.jsonPrimitive.toString(),
                        jsonObject["result"]!!.jsonObject,
                        jsonObject.getStringOrNull("suspend_data"),
                        jsonObject.getBoolOrNull("is_bookmarked"),
                        jsonObject.getBoolOrNull("is_corrected"),
                        jsonObject.getBoolOrNull("is_conversation"),
                        jsonObject.getBoolOrNull("is_unread"),
                        jsonObject["pages"]!!.jsonArray.map { CourseletPage.fromJson(it.jsonObject, group, courselet) },
                        courselet,
                        group
                )
            } else {
                return CourseletConnection(
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        courselet,
                        group
                )
            }

        }
    }

}