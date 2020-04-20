package de.deftk.lonet.api.model

import com.google.gson.JsonObject
import de.deftk.lonet.api.LoNet
import de.deftk.lonet.api.model.feature.Notification
import de.deftk.lonet.api.model.feature.Quota
import de.deftk.lonet.api.model.feature.SystemNotification
import de.deftk.lonet.api.model.feature.Task
import de.deftk.lonet.api.model.feature.mailbox.EmailFolder
import de.deftk.lonet.api.request.ApiRequest
import de.deftk.lonet.api.response.ApiResponse
import de.deftk.lonet.api.response.ResponseUtil

class User(val username: String, val authKey: String, responsibleHost: String, response: ApiResponse) :
    Member(
        response.toJson().asJsonArray.get(0).asJsonObject.get("result").asJsonObject.get("user").asJsonObject,
        responsibleHost
    ) {

    val sessionId: String
    val memberships: List<Member>

    init {
        val jsonResponse = response.toJson().asJsonArray
        val loginResponse = ResponseUtil.getSubResponseResultByMethod(jsonResponse, "login")
        val informationResponse = ResponseUtil.getSubResponseResultByMethod(jsonResponse, "get_information")

        sessionId = informationResponse.get("session_id").asString
        memberships = loginResponse.get("member").asJsonArray.map { Member(it.asJsonObject, responsibleHost) }
    }

    fun getTasks(overwriteCache: Boolean = false): List<Task> {
        return getTasks(sessionId, overwriteCache)
    }

    override fun getTasks(sessionId: String, overwriteCache: Boolean): List<Task> {
        val tasks = if (Feature.TASKS.isAvailable(permissions))
            super.getTasks(sessionId, overwriteCache).toMutableList()
        else mutableListOf()
        memberships.forEach { membership ->
            if (membership.memberPermissions.contains(Permission.TASKS))
                tasks.addAll(membership.getTasks(sessionId, overwriteCache))
        }
        return tasks
    }

    fun getNotifications(overwriteCache: Boolean = false): List<Notification> {
        val notifications = if (Feature.BOARD.isAvailable(permissions))
            super.getNotifications(sessionId, overwriteCache).toMutableList()
        else mutableListOf()
        memberships.forEach { membership ->
            if (membership.memberPermissions.contains(Permission.BOARD))
                notifications.addAll(membership.getNotifications(sessionId, overwriteCache))
        }
        return notifications
    }

    override fun getNotifications(sessionId: String, overwriteCache: Boolean): List<Notification> {
        return super.getNotifications(sessionId, overwriteCache)
    }

    fun getFileQuota(overwriteCache: Boolean = false): Quota {
        return super.getFileQuota(sessionId, overwriteCache)
    }

    fun logout(removeTrust: Boolean = true, overwriteCache: Boolean = false) {
        val request = ApiRequest(responsibleHost!!)
        request.addSetSessionRequest(sessionId)
        request.addRequest("logout", null)
        if (removeTrust) {
            // WHAT HAVE YOU DONE?!
            val tmpUser = LoNet.loginToken(username, authKey, true)
            tmpUser.logout(false)
        }
        val response = LoNet.requestHandler.performRequest(request, !overwriteCache)
        println(response.raw)
    }

    fun getAutoLoginUrl(): String {
        val request = ApiRequest(responsibleHost!!)
        request.addSetSessionRequest(sessionId)
        request.addSetFocusRequest("trusts", login)
        request.addRequest("get_url_for_autologin", null)
        val response = LoNet.requestHandler.performRequest(request, false)
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), 3)
        return subResponse.get("url").asString
    }

    fun getEmailStatus(overwriteCache: Boolean = false): Pair<Quota, Int> {
        val request = ApiRequest(responsibleHost!!)
        request.addSetSessionRequest(sessionId)
        request.addSetFocusRequest("mailbox", login)
        request.addRequest("get_state", null)
        val response = LoNet.requestHandler.performRequest(request, !overwriteCache)
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), 3)
        return Pair(Quota(subResponse.get("quota").asJsonObject), subResponse.get("unread_messages").asInt)
    }

    fun getEmailQuota(overwriteCache: Boolean = false): Quota {
        return getEmailStatus(overwriteCache).first
    }

    fun getUnreadEmailCount(overwriteCache: Boolean = false): Int {
        return getEmailStatus(overwriteCache).second
    }

    fun getEmailFolders(overwriteCache: Boolean = false): List<EmailFolder> {
        val request = ApiRequest(responsibleHost!!)
        request.addSetSessionRequest(sessionId)
        request.addSetFocusRequest("mailbox")
        request.addRequest("get_folders", null)
        val response = LoNet.requestHandler.performRequest(request, !overwriteCache)
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), 3)
        return subResponse.get("folders").asJsonArray.map { EmailFolder(it.asJsonObject, responsibleHost) }
    }

    //TODO attachments
    fun sendEmail(to: String, subject: String, plainBody: String, text: String? = null, blindCopyTo: String? = null, copyTo: String? = null, overwriteCache: Boolean = false) {
        val request = ApiRequest(responsibleHost!!)
        request.addSetSessionRequest(sessionId)
        request.addSetFocusRequest("mailbox")
        val requestParams = JsonObject()
        requestParams.addProperty("to", to)
        requestParams.addProperty("subject", subject)
        requestParams.addProperty("body_plain", plainBody)
        if (text != null)
            requestParams.addProperty("text", text)
        if (blindCopyTo != null)
            requestParams.addProperty("bcc", blindCopyTo)
        if (copyTo != null)
            requestParams.addProperty("cc", copyTo)
        request.addRequest("send_mail", requestParams)
        val response = LoNet.requestHandler.performRequest(request, !overwriteCache)
        ResponseUtil.checkSuccess(response.toJson())
    }

    fun getSystemNofications(overwriteCache: Boolean = false): List<SystemNotification> {
        val request = ApiRequest(responsibleHost!!)
        request.addSetSessionRequest(sessionId)
        request.addSetFocusRequest("messages")
        request.addRequest("get_messages", null)
        val response = LoNet.requestHandler.performRequest(request, !overwriteCache)
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), 3)
        return subResponse.get("messages")?.asJsonArray?.map { SystemNotification(it.asJsonObject) } ?: emptyList()
    }

}