package de.deftk.lonet.api.model.feature.courselets

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TemplatePackage(val serialId: String) {
    @SerialName("offline")
    OFFLINE("offline"),

    @SerialName("scorm")
    SCORM("scorm")
}