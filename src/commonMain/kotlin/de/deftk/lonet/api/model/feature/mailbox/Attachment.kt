package de.deftk.lonet.api.model.feature.mailbox

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(val id: String, val name: String, val size: Int)