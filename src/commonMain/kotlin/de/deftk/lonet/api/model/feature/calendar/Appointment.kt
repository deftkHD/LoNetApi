package de.deftk.lonet.api.model.feature.calendar

import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.request.OperatorApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.getApiDate
import de.deftk.lonet.api.utils.getApiDateOrNull
import de.deftk.lonet.api.utils.getManageable
import de.deftk.lonet.api.utils.getStringOrNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class Appointment(val id: String, uid: String?, title: String, description: String?, endDate: Long?, endDateIso: String?, location: String?, tzid: String?, rrule: String?, startDate: Long?, startDateIso: String?, creationDate: Long?, creationMember: IManageable?, modificationDate: Long?, modificationMember: IManageable?, val operator: AbstractOperator) {

    companion object {
        fun fromJson(jsonObject: JsonObject, operator: AbstractOperator): Appointment {
            val createdObject = jsonObject["created"]!!.jsonObject
            val modifiedObject = jsonObject["modified"]!!.jsonObject
            val appointment = Appointment(
                    jsonObject["id"]!!.jsonPrimitive.toString(),
                    null,
                    "",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    createdObject.getApiDate("date"),
                    createdObject.getManageable("user", operator),
                    modifiedObject.getApiDate("date"),
                    modifiedObject.getManageable("user", operator),
                    operator
            )
            appointment.readFrom(jsonObject)
            return appointment
        }
    }

    var uid = uid
        private set
    var title = title
        private set
    var description = description
        private set
    var endDate = endDate
        private set
    var endDateIso = endDateIso
        private set
    var location = location
        private set
    var tzid = tzid
        private set
    var rrule = rrule
        private set
    var startDate = startDate
        private set
    var startDateIso = startDateIso
        private set
    var creationDate = creationDate
        private set
    var creationMember = creationMember
        private set
    var modificationDate = modificationDate
        private set
    var modificationMember = modificationMember
        private set

    fun edit(title: String? = null, description: String? = null, endDate: Long? = null, endDateIso: String? = null, location: String? = null, rrule: String? = null, startDate: Long? = null, startDateIso: String? = null) {
        val request = OperatorApiRequest(operator)
        val id = request.addSetAppointmentRequest(id, title, description, endDate, endDateIso, location, rrule, startDate, startDateIso)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse["entry"]!!.jsonObject)
    }

    fun delete() {
        val request = OperatorApiRequest(operator)
        request.addDeleteAppointmentRequest(id)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    private fun readFrom(jsonObject: JsonObject) {
        uid = jsonObject.getStringOrNull("uid")
        title = jsonObject["title"]!!.jsonPrimitive.toString()
        description = jsonObject.getStringOrNull("description")
        endDate = jsonObject.getApiDateOrNull("end_date")
        endDateIso = jsonObject.getStringOrNull("end_date_iso")
        location = jsonObject.getStringOrNull("location")
        tzid = jsonObject.getStringOrNull("tzid")
        rrule = jsonObject.getStringOrNull("rrule")
        startDate = jsonObject.getApiDateOrNull("start_date")
        startDateIso = jsonObject.getStringOrNull("start_date_iso")

        val createdObject = jsonObject["created"]!!.jsonObject
        creationDate = createdObject.getApiDate("date")
        creationMember = createdObject.getManageable("user", operator)

        val modifiedObject = jsonObject["modified"]!!.jsonObject
        modificationDate = modifiedObject.getApiDate("date")
        modificationMember = modifiedObject.getManageable("user", operator)
    }

}