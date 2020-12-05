package de.deftk.lonet.api.model.feature

import kotlinx.serialization.Serializable

@Serializable
class WikiPage(val name: String, val exists: String, val source: String)