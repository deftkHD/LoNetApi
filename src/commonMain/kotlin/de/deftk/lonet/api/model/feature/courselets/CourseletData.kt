package de.deftk.lonet.api.model.feature.courselets

import de.deftk.lonet.api.model.Group
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.utils.getManageable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class CourseletData(val id: Int, val title: String, val user: IManageable, val connection: CourseletConnection, val courselet: Courselet, val group: Group) {

    companion object {
        fun fromJson(jsonObject: JsonObject, group: Group, courselet: Courselet): CourseletData {
            return CourseletData(
                    jsonObject["id"]!!.jsonPrimitive.int,
                    jsonObject["title"]!!.jsonPrimitive.toString(),
                    jsonObject.getManageable("user", group),
                    CourseletConnection.fromJson(jsonObject["connection"]!!.jsonObject, group, courselet),
                    courselet,
                    group
            )
        }
    }

}