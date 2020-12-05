package de.deftk.lonet.api.model.feature.forum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ForumSettings(
    @SerialName("create_threads")
    val createThreads: String,
    @SerialName("alternate_view")
    val alternateView: Int
)