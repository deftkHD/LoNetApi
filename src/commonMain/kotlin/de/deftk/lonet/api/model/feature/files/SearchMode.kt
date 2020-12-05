package de.deftk.lonet.api.model.feature.files

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SearchMode(val id: String) {
    @SerialName("word_equals")
    WORD_EQUALS("word_equals"),

    @SerialName("word_starts_with")
    WORD_STARTS_WITH("word_starts_with"),

    @SerialName("word_contains")
    WORD_CONTAINS("word_contains"),

    @SerialName("phrase")
    PHRASE("phrase");
}