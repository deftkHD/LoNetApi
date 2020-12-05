package de.deftk.lonet.api.model.feature.files

import kotlinx.serialization.Serializable

@Serializable
data class FileProxyNonce(val name: String, val size: Long, val nonce: String)