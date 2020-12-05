package de.deftk.lonet.api.model.feature.mailbox

import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.request.OperatorApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.getApiDate
import kotlinx.serialization.json.*

class EmailFolder(val id: String, name: String, val type: EmailFolderType, val mDate: Long, val operator: AbstractOperator) {

    companion object {
        fun fromJson(jsonObject: JsonObject, operator: AbstractOperator): EmailFolder {
            val type = when {
                jsonObject["is_inbox"]!!.jsonPrimitive.boolean -> EmailFolderType.INBOX
                jsonObject["is_trash"]!!.jsonPrimitive.boolean -> EmailFolderType.TRASH
                jsonObject["is_drafts"]!!.jsonPrimitive.boolean -> EmailFolderType.DRAFTS
                jsonObject["is_sent"]!!.jsonPrimitive.boolean -> EmailFolderType.SENT
                else -> EmailFolderType.OTHER
            }
            return EmailFolder(
                    jsonObject["id"]!!.jsonPrimitive.toString(),
                    jsonObject["name"]!!.jsonPrimitive.toString(),
                    type,
                    jsonObject.getApiDate("m_date"),
                    operator
            )
        }
    }

    var name = name
        private set

    fun getEmails(limit: Int? = null, offset: Int? = null): List<Email> {
        val request = OperatorApiRequest(operator)
        val id = request.addGetEmailsRequest(id, limit, offset)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["messages"]!!.jsonArray.map { Email.fromJson(it.jsonObject, this, operator) }
    }

    fun edit(name: String) {
        val request = OperatorApiRequest(operator)
        request.addSetEmailFolderRequest(id, name)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
        this.name = name
    }

    //FIXME for some reason fails
    fun delete() {
        val request = OperatorApiRequest(operator)
        request.addDeleteEmailFolderRequest(id)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    override fun toString(): String {
        return "$name:$id"
    }

    enum class EmailFolderType {
        INBOX,
        TRASH,
        DRAFTS,
        SENT,
        OTHER
    }

}