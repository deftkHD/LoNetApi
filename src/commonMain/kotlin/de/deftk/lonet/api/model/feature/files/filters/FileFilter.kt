package de.deftk.lonet.api.model.feature.files.filters

import de.deftk.lonet.api.model.feature.files.SearchMode
import kotlinx.serialization.Serializable

@Serializable
data class FileFilter(val searchTerm: String, val searchMode: SearchMode)