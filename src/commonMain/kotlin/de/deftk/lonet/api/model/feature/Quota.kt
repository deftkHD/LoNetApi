package de.deftk.lonet.api.model.feature

import kotlinx.serialization.Serializable

@Serializable
data class Quota(val usage: Long, val free: Long, val limit: Long, val sequence: Int, val updated: Long, val version: Int)