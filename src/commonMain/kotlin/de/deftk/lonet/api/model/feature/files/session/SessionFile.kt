package de.deftk.lonet.api.model.feature.files.session

import de.deftk.lonet.api.model.User
import de.deftk.lonet.api.model.feature.files.FileChunk
import de.deftk.lonet.api.model.feature.files.FileUrl
import de.deftk.lonet.api.request.UserApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import kotlinx.serialization.json.*

class SessionFile(val id: String, val name: String, size: Int, downloadUrl: String, val user: User) {

    companion object {
        fun fromJson(jsonObject: JsonObject, user: User): SessionFile {
            return SessionFile(
                    jsonObject["id"]!!.jsonPrimitive.toString(),
                    jsonObject["name"]!!.jsonPrimitive.toString(),
                    jsonObject["size"]!!.jsonPrimitive.int,
                    jsonObject["download_url"]!!.jsonPrimitive.toString(),
                    user
            )
        }
    }

    var size = size
        private set
    var downloadUrl = downloadUrl
        private set

    fun appendData(data: ByteArray) {
        val request = UserApiRequest(user)
        val id = request.addAppendSessionFileRequest(id, data)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse["file"]!!.jsonObject)
    }

    fun delete() {
        val request = UserApiRequest(user)
        request.addDeleteSessionFileRequest(id)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    fun download(limit: Int? = null, offset: Int? = null): FileChunk {
        val request = UserApiRequest(user)
        val id = request.addGetSessionFileRequest(id, limit, offset)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Json.decodeFromJsonElement(subResponse["file"]!!.jsonObject)
    }

    fun getDownloadUrl(): FileUrl {
        val request = UserApiRequest(user)
        val id = request.addGetSessionFileDownloadUrlRequest(id)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return FileUrl.fromDownloadJson(subResponse["file"]!!.jsonObject)
    }

    fun getUploadUrl(): FileUrl {
        val request = UserApiRequest(user)
        val id = request.addGetSessionFileUploadUrlRequest(id)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return FileUrl.fromUploadJson(subResponse["file"]!!.jsonObject)
    }

    private fun readFrom(jsonObject: JsonObject) {
        size = jsonObject["size"]!!.jsonPrimitive.int
        downloadUrl = jsonObject["download_url"]!!.jsonPrimitive.toString()
    }

}