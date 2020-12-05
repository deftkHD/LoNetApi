package de.deftk.lonet.api.utils

import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.model.abstract.IManageable
import kotlinx.serialization.json.*

fun JsonObject.getStringOrNull(key: String): String? {
    if (containsKey(key)) {
        val obj = get(key)!!
        return if (obj is JsonNull) null else obj.jsonPrimitive.toString()
    }
    return null
}

fun JsonObject.getIntOrNull(key: String): Int? {
    if (containsKey(key)) {
        val obj = get(key)!!
        return if (obj is JsonNull) null else obj.jsonPrimitive.int
    }
    return null
}

fun JsonObject.getApiDateOrNull(key: String): Long? {
    if (containsKey(key)) {
        val obj = get(key)!!
        return if (obj is JsonNull) null else obj.jsonPrimitive.long * 1000L
    }
    return null
}

fun JsonObject.getApiDate(key: String): Long {
    return get(key)!!.jsonPrimitive.long * 1000L
}

fun JsonObject.getManageable(key: String, operator: AbstractOperator): IManageable {
    return operator.getContext().getOrCreateManageable(get(key)!!.jsonObject)
}

fun JsonObject.getBoolOrNull(key: String): Boolean? {
    if (containsKey(key)) {
        val obj = get(key)!!
        if (obj is JsonNull)
            return null
        check(obj is JsonPrimitive)
        return obj.jsonPrimitive.booleanOrNull ?: obj.jsonPrimitive.intOrNull?.equals(1)
        ?: with(obj.toString()) { if (length > 1) this == "true" else this == "1" }
    }
    return null
}