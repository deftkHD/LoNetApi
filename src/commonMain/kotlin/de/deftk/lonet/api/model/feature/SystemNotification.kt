package de.deftk.lonet.api.model.feature

import de.deftk.lonet.api.model.User
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.request.UserApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.getApiDate
import de.deftk.lonet.api.utils.getBoolOrNull
import de.deftk.lonet.api.utils.getStringOrNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class SystemNotification(val id: String, val messageType: SystemNotificationType?, val date: Long, val message: String, val data: String, val member: IManageable, val group: IManageable, val fromId: Any?, val read: Boolean, val obj: String?, val user: User) {

    companion object {
        fun fromJson(jsonObject: JsonObject, user: User): SystemNotification {
            return SystemNotification(
                    jsonObject["id"]!!.jsonObject.toString(),
                    SystemNotificationType.getById(jsonObject.getStringOrNull("message")),
                    jsonObject.getApiDate("date"),
                    jsonObject["message_hr"]!!.jsonObject.toString(),
                    jsonObject["data"]!!.jsonObject.toString(),
                    user.getContext().getOrCreateManageable(jsonObject["from_user"]!!.jsonObject),
                    user.getContext().getOrCreateManageable(jsonObject["from_group"]!!.jsonObject),
                    jsonObject.getStringOrNull("from_id"),
                    !jsonObject.getBoolOrNull("is_unread")!!,
                    jsonObject.getStringOrNull("object"),
                    user
            )
        }
    }

    fun delete() {
        val request = UserApiRequest(user)
        request.addDeleteSystemNotificationRequest(this.id.toInt())[1] // response returns id as string, but request wants an integer
        ResponseUtil.checkSuccess(request.fireRequest().toJson())
    }

    override fun toString(): String {
        return messageType.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SystemNotification

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


    @Serializable
    enum class SystemNotificationType(val id: String) {
        @SerialName("7")
        FILE_UPLOAD("7"),

        @SerialName("8")
        FILE_DOWNLOAD("8"),

        @SerialName("29")
        NEW_NOTIFICATION("29"),

        @SerialName("33")
        NEW_TRUST("33"),

        @SerialName("35")
        UNAUTHORIZED_LOGIN_LOCATION("35"),

        @SerialName("46")
        NEW_TASK("46");

        companion object {
            fun getById(id: String?): SystemNotificationType? {
                val type = values().firstOrNull { it.id == id }
                if (type == null)
                    println("Unknown system notification type $id")
                return type
            }
        }
    }

}