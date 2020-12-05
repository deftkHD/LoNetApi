package de.deftk.lonet.api.model.feature.files

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileChunk(val name: String, val size: Long, @SerialName("data_encoding") val encoding: String, val data: String)