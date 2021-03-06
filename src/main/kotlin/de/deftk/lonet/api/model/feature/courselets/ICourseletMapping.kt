package de.deftk.lonet.api.model.feature.courselets

import de.deftk.lonet.api.model.IRequestContext

interface ICourseletMapping {

    val id: Int

    fun getName(): String

    fun setName(name: String, context: IRequestContext)
    fun delete(context: IRequestContext)

}