package de.deftk.lonet.api.request

import de.deftk.lonet.api.model.Feature
import de.deftk.lonet.api.model.Locale
import de.deftk.lonet.api.model.User
import de.deftk.lonet.api.model.abstract.IUser
import de.deftk.lonet.api.platform.CryptoUtil
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class UserApiRequest(private val user: User) : OperatorApiRequest(user) {

    fun addGetAutoLoginUrlRequest(disableLogout: Boolean? = null, disableReceptionOfQuickMessages: Boolean? = null, enslaveSession: Boolean? = null, locale: Locale? = null, pingMaster: Boolean? = null, sessionTimeout: Int? = null, targetData: Any? = null, targetIframes: Boolean? = null, targetUrlPath: String? = null): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            if (disableLogout != null) put("disable_logout", disableLogout)
            if (disableReceptionOfQuickMessages != null) put("disable_reception_of_quick_messages", disableReceptionOfQuickMessages)
            if (enslaveSession != null) put("enslave_session", enslaveSession)
            if (locale != null) put("locale", locale.code)
            if (pingMaster != null) put("ping_master", pingMaster)
            if (sessionTimeout != null) put("session_timeout", sessionTimeout)
            if (targetData != null) TODO("don't know how to handle mixed data")
            if (targetIframes != null) put("target_iframes", targetIframes)
            if (targetUrlPath != null) put("target_url_path", targetUrlPath)
        }
        return listOf(
                addSetFocusRequest("trusts", user.getLogin()),
                addRequest("get_url_for_autologin", requestParams)
        )
    }

    fun addGetSystemNotificationsRequest(startId: Int? = null): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            if (startId != null) put("start_id", startId)
        }
        return listOf(
                addSetFocusRequest("messages", user.getLogin()),
                addRequest("get_messages", requestParams)
        )
    }

    fun addDeleteSystemNotificationRequest(id: Int): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("messages", user.getLogin()),
                addRequest("delete_message", requestParams)
        )
    }

    fun addGetAllTasksRequest(): List<Int> {
        val ids = mutableListOf<Int>()
        if (Feature.TASKS.isAvailable(user.effectiveRights)) {
            ids.addAll(addGetTasksRequest())
        }
        user.getContext().getGroups().forEach { membership ->
            if (Feature.TASKS.isAvailable(membership.effectiveRights)) {
                ids.addAll(addGetTasksRequest(membership.getLogin()))
            }
        }
        return ids
    }

    fun addGetAllNotificationsRequest(): List<Int> {
        val ids = mutableListOf<Int>()
        if (Feature.BOARD.isAvailable(user.effectiveRights)) {
            ids.addAll(addGetBoardNotificationsRequest())
        }
        user.getContext().getGroups().forEach { membership ->
            if (Feature.BOARD.isAvailable(membership.effectiveRights)) {
                ids.addAll(addGetBoardNotificationsRequest(membership.getLogin()))
            }
        }
        return ids
    }

    fun addGetAllTeacherNotificationsRequest(): List<Int> {
        val ids = mutableListOf<Int>()
        if (Feature.BOARD.isAvailable(user.effectiveRights)) {
            ids.addAll(addGetTeacherBoardNotificationsRequest())
        }
        user.getContext().getGroups().forEach { membership ->
            if (Feature.BOARD.isAvailable(membership.effectiveRights)) {
                ids.addAll(addGetTeacherBoardNotificationsRequest(membership.getLogin()))
            }
        }
        return ids
    }

    fun addGetAllPupilNotificationsRequest(): List<Int> {
        val ids = mutableListOf<Int>()
        if (Feature.BOARD.isAvailable(user.effectiveRights)) {
            ids.addAll(addGetPupilBoardNotificationsRequest())
        }
        user.getContext().getGroups().forEach { membership ->
            if (Feature.BOARD.isAvailable(membership.effectiveRights)) {
                ids.addAll(addGetPupilBoardNotificationsRequest(membership.getLogin()))
            }
        }
        return ids
    }

    fun addAddSessionFileRequest(name: String, data: ByteArray): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("name", name)
            put("data", CryptoUtil.encodeToString(data))
        }
        return listOf(
                addSetFocusRequest("session_files", user.getLogin()),
                addRequest("add_file", requestParams)
        )
    }

    fun addAppendSessionFileRequest(id: String, data: ByteArray): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            put("data", CryptoUtil.encodeToString(data))
        }
        return listOf(
                addSetFocusRequest("session_files", user.getLogin()),
                addRequest("append_file", requestParams)
        )
    }

    fun addDeleteSessionFileRequest(id: String): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("session_files", user.getLogin()),
                addRequest("delete_file", requestParams)
        )
    }

    fun addGetSessionFileRequest(id: String, limit: Int? = null, offset: Int? = null): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (limit != null)
                put("limit", limit)
            if (offset != null)
                put("offset", offset)
        }
        return listOf(
                addSetFocusRequest("session_files", user.getLogin()),
                addRequest("get_file", requestParams)
        )
    }

    fun addGetSessionFileDownloadUrlRequest(id: String): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("session_files", user.getLogin()),
                addRequest("get_file_download_url", requestParams)
        )
    }

    fun addGetSessionFileUploadUrlRequest(name: String): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("name", name)
        }
        return listOf(
                addSetFocusRequest("session_files", user.getLogin()),
                addRequest("get_file_upload_url", requestParams)
        )
    }

    fun addRegisterServiceRequest(type: IUser.ServiceType, token: String, application: String? = null, generateSecret: String? = null, isOnline: Boolean? = null, managedObjects: String? = null, unmanagedPriority: Int? = null): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("service", type.id)
            put("token", token)
            if (application != null)
                put("application", application)
            if (generateSecret != null)
                put("generate_secret", generateSecret)
            if (isOnline != null)
                put("is_online", isOnline)
            if (managedObjects != null)
                put("managed_objects", managedObjects)
            if (unmanagedPriority != null)
                put("unmanaged_priority", unmanagedPriority)
        }
        return listOf(
                addSetFocusRequest("settings", user.getLogin()),
                addRequest("register_service", requestParams)
        )
    }

    fun addUnregisterServiceRequest(type: IUser.ServiceType, token: String): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("service", type.id)
            put("token", token)
        }
        return listOf(
                addSetFocusRequest("settings", user.getLogin()),
                addRequest("unregister_service", requestParams)
        )
    }

}