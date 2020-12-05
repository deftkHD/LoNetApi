package de.deftk.lonet.api.model.feature.files

import de.deftk.lonet.api.utils.BooleanFromIntSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileStorageSettings(
    @SerialName("hide_old_versions")
    @Serializable(with = BooleanFromIntSerializer::class)
    val hideOldVersions: Boolean
)