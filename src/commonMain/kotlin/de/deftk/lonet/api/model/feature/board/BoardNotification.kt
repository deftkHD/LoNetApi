package de.deftk.lonet.api.model.feature.board

import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.request.OperatorApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.*
import kotlinx.serialization.json.*

class BoardNotification(val id: String, title: String?, text: String?, color: BoardNotificationColor?, killDate: Long?, creationDate: Long, creationMember: IManageable, modificationDate: Long, modificationMember: IManageable, val board: BoardType, val operator: AbstractOperator) {

    companion object {
        fun fromJson(jsonObject: JsonObject, operator: AbstractOperator, board: BoardType): BoardNotification {
            val createdObject = jsonObject["created"]!!.jsonObject
            val modifiedObject = jsonObject["modified"]!!.jsonObject
            val notification = BoardNotification(
                    jsonObject["id"]!!.jsonPrimitive.toString(),
                    jsonObject.getStringOrNull("title"),
                    jsonObject.getStringOrNull("text"),
                    Json.decodeFromJsonElement(jsonObject["color"]!!),
                    null,
                    createdObject.getApiDate("date"),
                    createdObject.getManageable("user", operator),
                    modifiedObject.getApiDate("date"),
                    modifiedObject.getManageable("user", operator),
                    board,
                    operator
            )
            notification.readFrom(jsonObject)
            return notification
        }
    }

    var title = title
        private set
    var text = text
        private set
    var color = color
        private set
    var killDate = killDate
        private set
    var creationDate = creationDate
        private set
    var creationMember = creationMember
        private set
    var modificationDate = modificationDate
        private set
    var modificationMember = modificationMember
        private set

    fun edit(title: String? = null, text: String? = null, color: BoardNotificationColor? = null, killDate: Long? = null) {
        val request = OperatorApiRequest(operator)
        val id = when (board) {
            BoardType.ALL -> request.addSetBoardNotificationRequest(id, title, text, color, killDate)[1]
            BoardType.TEACHER -> request.addSetTeacherBoardNotificationRequest(id, title, text, color, killDate)[1]
            BoardType.PUPIL -> request.addSetPupilBoardNotificationRequest(id, title, text, color, killDate)[1]
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse["entry"]!!.jsonObject)
    }

    fun delete() {
        val request = OperatorApiRequest(operator)
        when (board) {
            BoardType.ALL -> request.addDeleteBoardNotificationRequest(id)
            BoardType.TEACHER -> request.addDeleteTeacherBoardNotificationRequest(id)
            BoardType.PUPIL -> request.addDeletePupilBoardNotificationRequest(id)
        }
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    private fun readFrom(jsonObject: JsonObject) {
        title = jsonObject.getStringOrNull("title")
        text = jsonObject.getStringOrNull("text")
        color = BoardNotificationColor.getById(jsonObject.getIntOrNull("color"))
        killDate = jsonObject.getApiDateOrNull("kill_date")

        val createdObject = jsonObject["created"]!!.jsonObject
        val modifiedObject = jsonObject["modified"]!!.jsonObject
        creationDate = createdObject.getApiDate("date")
        creationMember = createdObject.getManageable("user", operator)
        modificationDate = modifiedObject.getApiDate("date")
        modificationMember = modifiedObject.getManageable("user", operator)
    }

    override fun toString(): String {
        return title ?: id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BoardNotification

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}