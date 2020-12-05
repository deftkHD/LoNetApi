package de.deftk.lonet.api.model.feature

import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.request.OperatorApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class Task(val id: String, title: String?, description: String?, startDate: Long?, endDate: Long?, completed: Boolean, creationDate: Long, creationMember: IManageable, modificationDate: Long, modificationMember: IManageable, val operator: AbstractOperator) {

    companion object {
        fun fromJson(jsonObject: JsonObject, operator: AbstractOperator): Task {
            val createdObject = jsonObject["created"]!!.jsonObject
            val modifiedObject = jsonObject["modified"]!!.jsonObject
            val task = Task(
                    jsonObject["id"]!!.jsonObject.toString(),
                    null,
                    null,
                    null,
                    null,
                    jsonObject.getBoolOrNull("completed")!!,
                    createdObject.getApiDate("date"),
                    createdObject.getManageable("user", operator),
                    modifiedObject.getApiDate("date"),
                    modifiedObject.getManageable("user", operator),
                    operator
            )
            task.readFrom(jsonObject)
            return task
        }
    }

    var title = title
        private set
    var description = description
        private set
    var startDate = startDate
        private set
    var endDate = endDate
        private set
    var completed = completed
        private set
    var creationDate = creationDate
        private set
    var creationMember = creationMember
        private set
    var modificationDate = modificationDate
        private set
    var modificationMember = modificationMember
        private set


    fun edit(completed: Boolean? = null, description: String? = null, dueDate: Long? = null, startDate: Long? = null, title: String? = null) {
        val request = OperatorApiRequest(operator)
        val id = request.addSetTaskRequest(id, completed, description, dueDate, startDate, title)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse["entry"]!!.jsonObject)
    }

    fun delete() {
        val request = OperatorApiRequest(operator)
        request.addDeleteTaskRequest(id)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    private fun readFrom(jsonObject: JsonObject) {
        title = jsonObject.getStringOrNull("title")
        description = jsonObject.getStringOrNull("description")
        startDate = jsonObject.getApiDateOrNull("start_date")
        endDate = jsonObject.getApiDateOrNull("due_date")
        completed = jsonObject.getBoolOrNull("completed")!!

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

        other as Task

        if (id != other.id) return false
        if (operator != other.operator) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + operator.hashCode()
        return result
    }


}