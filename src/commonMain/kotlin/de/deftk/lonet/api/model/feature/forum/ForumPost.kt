package de.deftk.lonet.api.model.feature.forum

import de.deftk.lonet.api.model.Group
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.request.GroupApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.getApiDate
import de.deftk.lonet.api.utils.getBoolOrNull
import de.deftk.lonet.api.utils.getIntOrNull
import de.deftk.lonet.api.utils.getManageable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ForumPost(val id: String, val parentId: String, val title: String, val text: String, val icon: ForumPostIcon?, val level: Int, val commentCount: Int, val creationDate: Long, val creationMember: IManageable, val modificationDate: Long, val modificationMember: IManageable, val pinned: Boolean?, val locked: Boolean?, val group: Group) {

    val comments = mutableListOf<ForumPost>()

    companion object {
        fun fromJson(jsonObject: JsonObject, group: Group): ForumPost {
            val creationObject = jsonObject["created"]!!.jsonObject
            val modificationObject = jsonObject["modified"]!!.jsonObject
            return ForumPost(
                    jsonObject["id"]!!.jsonPrimitive.toString(),
                    jsonObject["parent_id"]!!.jsonPrimitive.toString(),
                    jsonObject["title"]!!.jsonPrimitive.toString(),
                    jsonObject["text"]!!.jsonPrimitive.toString(),
                    ForumPostIcon.getById(jsonObject.getIntOrNull("icon")),
                    jsonObject["level"]!!.jsonPrimitive.int,
                    jsonObject["children"]!!.jsonObject["count"]!!.jsonPrimitive.int,
                    //jsonObject.get("files").asJsonArray //seems to be unused (always empty)
                    creationObject.getApiDate("date"),
                    creationObject.getManageable("user", group),
                    modificationObject.getApiDate("date"),
                    modificationObject.getManageable("user", group),
                    jsonObject.getBoolOrNull("pinned"),
                    jsonObject.getBoolOrNull("locked"),
                    group
            )
        }
    }

    fun addComment(title: String, text: String, icon: ForumPostIcon, importSessionFile: String? = null, importSessionFiles: Array<String>? = null, replyNotificationMe: Boolean? = null): ForumPost {
        return group.addForumPost(title, text, icon, id, importSessionFile, importSessionFiles, replyNotificationMe)
    }

    fun delete() {
        val request = GroupApiRequest(group)
        request.addDeleteForumPostRequest(id)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

}