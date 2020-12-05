package de.deftk.lonet.api.model.feature.courselets

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseletDownload(
    val id: String,
    val name: String,
    val size: Long,
    @SerialName("download_url") val downloadUrl: String
)