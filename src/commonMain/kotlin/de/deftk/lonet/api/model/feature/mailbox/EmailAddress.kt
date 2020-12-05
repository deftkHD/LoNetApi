package de.deftk.lonet.api.model.feature.mailbox

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailAddress(
    @SerialName("addr")
    val address: String,
    val name: String
) {

    override fun toString(): String {
        return address
    }

}