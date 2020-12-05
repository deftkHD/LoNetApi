package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.Quota
import de.deftk.lonet.api.model.feature.courselets.Courselet
import de.deftk.lonet.api.model.feature.courselets.CourseletDownload
import de.deftk.lonet.api.model.feature.courselets.Mapping
import kotlinx.serialization.json.JsonObject

interface ICourselets {

    /**
     * @return Quota and runtimeVersionHash
     */
    fun getCourseletsState(): Pair<Quota, String>
    fun getCourseletsConfiguration(): JsonObject
    fun getCourselets(): List<Courselet>

    fun getCourseletMappings(): List<Mapping>
    fun addCourseletMapping(name: String): Mapping

    fun exportCourseletRuntime(): CourseletDownload

}