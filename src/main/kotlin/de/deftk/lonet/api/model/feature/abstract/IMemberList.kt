package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.abstract.IManageable

interface IMemberList {

    fun getMembers(overwriteCache: Boolean = false): List<IManageable>

}