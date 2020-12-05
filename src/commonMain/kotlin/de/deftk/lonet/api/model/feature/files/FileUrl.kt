package de.deftk.lonet.api.model.feature.files

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

data class FileUrl(val name: String, val size: Long, val url: String) {

    companion object {
        fun fromDownloadJson(jsonObject: JsonObject): FileUrl {
            return FileUrl(
                jsonObject["name"]!!.jsonPrimitive.toString(),
                jsonObject["size"]!!.jsonPrimitive.long,
                jsonObject["download_url"]!!.jsonPrimitive.toString()
            )
        }

        fun fromPreviewJson(jsonObject: JsonObject): FileUrl {
            return FileUrl(
                jsonObject["name"]!!.jsonPrimitive.toString(),
                jsonObject["size"]!!.jsonPrimitive.long,
                jsonObject["preview_download_url"]!!.jsonPrimitive.toString()
            )
        }

        fun fromUploadJson(jsonObject: JsonObject): FileUrl {
            return FileUrl(
                jsonObject["name"]!!.jsonPrimitive.toString(),
                -1,
                jsonObject["upload_url"]!!.jsonPrimitive.toString()
            )
        }
    }

}