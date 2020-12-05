package de.deftk.lonet.api.model.feature.mailbox

import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.request.OperatorApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.getStringOrNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class EmailSignature(var text: String, var positionAnswer: Position?, var positionForward: Position?, val operator: AbstractOperator) {

    companion object {
        fun fromJson(jsonObject: JsonObject, operator: AbstractOperator): EmailSignature {
            val signature = EmailSignature(
                    jsonObject["text"]!!.jsonPrimitive.toString(),
                    null,
                    null,
                    operator
            )
            signature.readFrom(jsonObject)
            return signature
        }
    }

    fun edit(text: String, positionAnswer: Position? = null, positionForward: Position? = null) {
        val request = OperatorApiRequest(operator)
        val id = request.addSetEmailSignatureRequest(text, positionAnswer?.text, positionForward?.text)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse["signature"]!!.jsonObject)
    }

    private fun readFrom(jsonObject: JsonObject) {
        positionAnswer = Position.byTextValue(jsonObject.getStringOrNull("position_answer"))
        positionForward = Position.byTextValue(jsonObject.getStringOrNull("position_forward"))
    }

    @Serializable
    enum class Position(val text: String) {
        @SerialName("beginning")
        BEGINNING("beginning"),

        @SerialName("end")
        END("end"),

        @SerialName("none")
        NONE("none");

        companion object {
            fun byTextValue(text: String?): Position? {
                return values().firstOrNull { it.text == text }
            }
        }
    }

}