package de.deftk.lonet.api.model.abstract

interface IManageable {

    fun getLogin(): String
    fun getName(): String
    fun getType(): ManageableType

}