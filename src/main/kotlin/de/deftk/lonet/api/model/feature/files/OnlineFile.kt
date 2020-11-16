package de.deftk.lonet.api.model.feature.files

import com.google.gson.JsonObject
import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.model.feature.abstract.IFilePrimitive
import de.deftk.lonet.api.model.feature.files.filters.FileFilter
import de.deftk.lonet.api.request.OperatorApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.*
import java.io.Serializable
import java.util.*


class OnlineFile(id: String, parentId: String?, ordinal: Int?, name: String, description: String?, type: FileType?, size: Long, readable: Boolean?, writable: Boolean?, sparse: Boolean?, mine: Boolean?, shared: Boolean?, creationDate: Date, creationMember: IManageable, modificationDate: Date, modificationMember: IManageable, effectiveRead: Boolean?, effectiveWrite: Boolean?, effectiveModify: Boolean?, effectiveDelete: Boolean?, preview: Boolean?, val operator: AbstractOperator) : IFilePrimitive, Serializable {

    companion object {
        fun fromJson(jsonObject: JsonObject, operator: AbstractOperator): OnlineFile {
            val createdObject = jsonObject.get("created").asJsonObject
            val modifiedObject = jsonObject.get("modified").asJsonObject
            val effectiveObject = jsonObject.get("effective")?.asJsonObject
            return OnlineFile(
                    jsonObject.get("id").asString,
                    jsonObject.getStringOrNull("parent_id"),
                    jsonObject.getIntOrNull("ordinal"),
                    jsonObject.get("name").asString,
                    jsonObject.getStringOrNull("description"),
                    FileType.getById(jsonObject.getStringOrNull("type")),
                    jsonObject.get("size").asLong,
                    jsonObject.getBoolOrNull("readable"),
                    jsonObject.getBoolOrNull("writable"),
                    jsonObject.getBoolOrNull("sparse"),
                    jsonObject.getBoolOrNull("mine"),
                    jsonObject.getBoolOrNull("shared"),
                    createdObject.getApiDate("date"),
                    createdObject.getManageable("user", operator),
                    modifiedObject.getApiDate("date"),
                    modifiedObject.getManageable("user", operator),
                    effectiveObject?.getBoolOrNull("read"),
                    effectiveObject?.getBoolOrNull("write"),
                    effectiveObject?.getBoolOrNull("modify"),
                    effectiveObject?.getBoolOrNull("delete"),
                    jsonObject.getBoolOrNull("preview"),
                    operator
            )
        }
    }

    var id = id
        private set
    var parentId = parentId
        private set
    var ordinal = ordinal
        private set
    var name = name
        private set
    var description = description
        private set
    var type = type
        private set
    var size = size
        private set
    var readable = readable
        private set
    var writable = writable
        private set
    var sparse = sparse
        private set
    var mine = mine
        private set
    var shared = shared
        private set
    var creationDate = creationDate
        private set
    var creationMember = creationMember
        private set
    var modificationDate = modificationDate
        private set
    var modificationMember = modificationMember
        private set
    var effectiveRead = effectiveRead
        private set
    var effectiveWrite = effectiveWrite
        private set
    var effectiveDelete = effectiveDelete
        private set
    var effectiveModify = effectiveModify
        private set
    var preview = preview
        private set

    override fun getFiles(filter: FileFilter?): List<OnlineFile> {
        check(type == FileType.FOLDER) { "File can't have children!" }
        val request = OperatorApiRequest(operator)
        val id = request.addGetFileStorageFilesRequest(
                id,
                recursive = false,
                getFiles = true,
                getFolders = true,
                searchString = filter?.searchTerm,
                searchMode = filter?.searchMode
        )[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse.get("entries")?.asJsonArray?.map { fromJson(it.asJsonObject, operator) } ?: emptyList()
    }

    fun download(limit: Int? = null, offset: Int? = null): FileChunk {
        check(type == FileType.FILE) { "Download only available for files" }
        val request = OperatorApiRequest(operator)
        val id = request.addGetFileRequest(id, limit, offset)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return FileChunk.fromJson(subResponse.get("file").asJsonObject)
    }

    fun getPreviewDownloadUrl(): FileDownloadUrl {
        check(type == FileType.FILE) { "Preview only available for files" }
        val request = OperatorApiRequest(operator)
        val id = request.addGetPreviewDownloadUrlRequest(id)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return FileDownloadUrl.fromPreviewJson(subResponse.get("file").asJsonObject)
    }

    fun getTempDownloadUrl(): FileDownloadUrl {
        check(type == FileType.FILE) { "Download urls are only available for files" }
        val request = OperatorApiRequest(operator)
        val id = request.addGetFileDownloadUrlRequest(id)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return FileDownloadUrl.fromJson(subResponse.get("file").asJsonObject)
    }

    fun getFileProxyNonce(): FileProxyNonce {
        check(type == FileType.FILE) { "Proxies are only available for files" }
        val request = OperatorApiRequest(operator)
        val id = request.addGetFileProxyNonceRequest(id)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return FileProxyNonce.fromJson(subResponse.get("file").asJsonObject)
    }

    fun addFile(name: String, data: ByteArray, description: String? = null): OnlineFile {
        check(type == FileType.FOLDER) { "Uploading is only available for folders" }
        val request = OperatorApiRequest(operator)
        val id = request.addAddFileRequest(Base64.getEncoder().encodeToString(data), id, name, description)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return fromJson(subResponse.get("file").asJsonObject, operator)
    }

    fun delete() {
        val request = OperatorApiRequest(operator)
        when (type) {
            FileType.FILE -> request.addDeleteFileRequest(id)[1]
            FileType.FOLDER -> request.addDeleteFolderRequest(id)[1]
            else -> error("Can't delete object")
        }
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    override fun addFolder(name: String, description: String?): OnlineFile {
        val request = OperatorApiRequest(operator)
        val id = request.addAddFolderRequest(id, name, description)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return fromJson(subResponse.get("folder").asJsonObject, operator)
    }

    fun setName(name: String) {
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFileRequest(id, name = name)[1]
            FileType.FOLDER -> request.addSetFolderRequest(id, name = name)[1]
            else -> error("Can't update name")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    fun setDescription(description: String) {
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFileRequest(id, description = description)[1]
            FileType.FOLDER -> request.addSetFolderRequest(id, description = description)[1]
            else -> error("Can't update description")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    fun setDownloadNotificationAddLogin(login: String) {
        check(type == FileType.FILE) { "Download notifications only available for files" }
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFileRequest(id, downloadNotificationAddLogin = login)[1]
            else -> error("Can't add download logins")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    fun setDownloadNotificationDeleteLogin(login: String) {
        check(type == FileType.FILE) { "Download notifications only available for files" }
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFileRequest(id, downloadNotificationDeleteLogin = login)[1]
            else -> error("Can't delete download logins")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    fun setDownloadNotificationMe(receive: Boolean) {
        check(type == FileType.FILE) { "Download notifications only available for files" }
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFileRequest(id, downloadNotificationMe = receive)[1]
            else -> error("Can't update receive update notification state")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    fun setUploadNotificationAddLogin(login: String) {
        check(type == FileType.FOLDER) { "Upload notifications only available for folders" }
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFolderRequest(id, uploadNotificationAddLogin = login)[1]
            else -> error("Can't delete add logins")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    fun setUploadNotificationDeleteLogin(login: String) {
        check(type == FileType.FOLDER) { "Upload notifications only available for folders" }
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFolderRequest(id, uploadNotificationDeleteLogin = login)[1]
            else -> error("Can't delete upload logins")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    fun setUploadNotificationMe(receive: Boolean) {
        check(type == FileType.FOLDER) { "Upload notifications only available for folders" }
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFolderRequest(id, uploadNotificationMe = receive)[1]
            else -> error("Can't update receive update notification state")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    fun setReadable(readable: Boolean) {
        check(type == FileType.FOLDER) { "Function only available for folders" }
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFolderRequest(id, readable = readable)[1]
            else -> error("Can't update readable state")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    fun setWritable(writable: Boolean) {
        check(type == FileType.FOLDER) { "Function only available for folders" }
        val request = OperatorApiRequest(operator)
        val id = when (type) {
            FileType.FILE -> request.addSetFolderRequest(id, writable = writable)[1]
            else -> error("Can't update writable state")
        }
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse.get("file").asJsonObject)
    }

    private fun readFrom(jsonObject: JsonObject) {
        id = jsonObject.get("id").asString
        parentId = jsonObject.getStringOrNull("parent_id")
        ordinal = jsonObject.getIntOrNull("ordinal")
        name = jsonObject.get("name").asString
        description = jsonObject.getStringOrNull("description")
        type = FileType.getById(jsonObject.getStringOrNull("type"))
        size = jsonObject.get("size").asLong
        readable = jsonObject.getBoolOrNull("readable")
        writable = jsonObject.getBoolOrNull("writable")
        sparse = jsonObject.getBoolOrNull("sparse")
        mine = jsonObject.getBoolOrNull("mine")
        shared = jsonObject.getBoolOrNull("shared")

        val createdObject = jsonObject.get("created").asJsonObject
        creationDate = createdObject.getApiDate("date")
        creationMember = createdObject.getManageable("user", operator)

        val modifiedObject = jsonObject.get("modified").asJsonObject
        modificationDate = modifiedObject.getApiDate("date")
        modificationMember = modifiedObject.getManageable("user", operator)

        val effectiveObject = jsonObject.get("effective")?.asJsonObject
        effectiveRead = effectiveObject?.getBoolOrNull("read")
        effectiveWrite = effectiveObject?.getBoolOrNull("write")
        effectiveModify = effectiveObject?.getBoolOrNull("modify")
        effectiveDelete = effectiveObject?.getBoolOrNull("delete")

        preview = jsonObject.getBoolOrNull("preview")
    }

    override fun getTrash(limit: Int?): List<OnlineFile> {
        val request = OperatorApiRequest(operator)
        val id = request.addGetTrashRequest(limit)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse.get("files").asJsonArray.map { fromJson(it.asJsonObject, operator) }
    }

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OnlineFile

        if (operator != other.operator) return false
        if (id != other.id) return false
        if (parentId != other.parentId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = operator.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + parentId.hashCode()
        return result
    }

    enum class FileType(val id: String) : Serializable {
        FILE("file"),
        FOLDER("folder");

        companion object {
            fun getById(id: String?): FileType? {
                return values().firstOrNull { it.id == id }
            }
        }
    }

}