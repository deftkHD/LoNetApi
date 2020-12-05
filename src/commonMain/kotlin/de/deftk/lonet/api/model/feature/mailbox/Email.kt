package de.deftk.lonet.api.model.feature.mailbox

import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.request.OperatorApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.getApiDate
import de.deftk.lonet.api.utils.getBoolOrNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

class Email(val id: Int, val subject: String, unread: Boolean?, flagged: Boolean?, answered: Boolean?, deleted: Boolean?, val date: Long, val size: Long, val from: List<EmailAddress>?, val to: List<EmailAddress>?, folder: EmailFolder, val operator: AbstractOperator) {

    companion object {
        fun fromJson(jsonObject: JsonObject, folder: EmailFolder, operator: AbstractOperator): Email {
            val email = Email(
                    jsonObject["id"]!!.jsonPrimitive.int,
                    jsonObject["subject"]!!.jsonPrimitive.toString(),
                    null,
                    null,
                    null,
                    null,
                    jsonObject.getApiDate("date"),
                    jsonObject.get("size")!!.jsonPrimitive.long,
                    jsonObject.get("from")?.jsonArray?.map { Json.decodeFromJsonElement(it.jsonObject) },
                    jsonObject.get("to")?.jsonArray?.map { Json.decodeFromJsonElement(it.jsonObject) },
                    folder,
                    operator
            )
            email.readFrom(jsonObject)
            return email
        }
    }

    var unread = unread
        private set

    var flagged = flagged
        private set

    var answered = answered
        private set

    var deleted = deleted
        private set

    var folder = folder
        private set

    fun read(peek: Boolean? = null): EmailContent {
        val request = OperatorApiRequest(operator)
        val id = request.addReadEmailRequest(folder.id, id, peek)[1]
        val response = request.fireRequest(operator.getContext())
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Json.decodeFromJsonElement(subResponse["message"]!!.jsonObject)
    }

    fun edit(isFlagged: Boolean? = null, isUnread: Boolean? = null) {
        val request = OperatorApiRequest(operator)
        request.addSetEmailRequest(folder.id, id, isFlagged, isUnread)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
        if (isFlagged != null)
            this.flagged = isFlagged
        if (isUnread != null)
            this.unread = isUnread
    }

    fun move(to: EmailFolder) {
        val request = OperatorApiRequest(operator)
        request.addMoveEmailRequest(folder.id, id, to.id)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
        this.folder = to
    }

    fun delete() {
        val request = OperatorApiRequest(operator)
        request.addDeleteEmailRequest(folder.id, id)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
        deleted = true
    }

    private fun readFrom(jsonObject: JsonObject) {
        unread = jsonObject.getBoolOrNull("is_unread")
        flagged = jsonObject.getBoolOrNull("is_flagged")
        answered = jsonObject.getBoolOrNull("is_answered")
        deleted = jsonObject.getBoolOrNull("is_deleted")
    }

    override fun toString(): String {
        return subject
    }


    @Serializable
    data class EmailContent(
        @SerialName("body_plain")
        val plainBody: String,
        val text: String? = null,
        val to: List<EmailAddress>,
        val files: List<Attachment> = emptyList()
    )

}