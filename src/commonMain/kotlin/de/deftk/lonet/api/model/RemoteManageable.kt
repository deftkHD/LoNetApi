package de.deftk.lonet.api.model

import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.model.abstract.ManageableType
import de.deftk.lonet.api.utils.BooleanFromIntSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteManageable(
    private val login: String,

    @SerialName("name_hr")
    private val name: String,

    private val type: ManageableType,

    @SerialName("is_online")
    @Serializable(with=BooleanFromIntSerializer::class)
    val isOnline: Boolean = false
) : IManageable {

    override fun getLogin() = login
    override fun getName() = name
    override fun getType() = type

}