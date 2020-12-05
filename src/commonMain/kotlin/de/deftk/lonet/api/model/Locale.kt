package de.deftk.lonet.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Locale(val code: String) {
    @SerialName("en")
    ENGLISH("en"),

    @SerialName("de")
    GERMAN("de"),

    @SerialName("fr")
    FRANCE("fr"),

    @SerialName("es")
    SPANISH("es"),

    @SerialName("it")
    ITALIAN("it"),

    @SerialName("tr")
    TURKEY("tr")
}