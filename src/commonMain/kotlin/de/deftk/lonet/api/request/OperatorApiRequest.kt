@file:Suppress("DuplicatedCode")

package de.deftk.lonet.api.request

import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.model.feature.board.BoardNotificationColor
import de.deftk.lonet.api.model.feature.contact.Gender
import de.deftk.lonet.api.model.feature.files.SearchMode
import de.deftk.lonet.api.response.ApiResponse
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

open class OperatorApiRequest(val operator: AbstractOperator) : ApiRequest() {

    fun addGetFileStorageStateRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("get_state", null)
        )
    }

    //TODO implement
    fun addGetFileStorageSettingsRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("get_settings", null)
        )
    }

    //TODO implement
    fun addSetFileStorageSettingsRequest(uploadNotificationAddLogin: String?, uploadNotificationDeleteLogin: String?, selfUploadNotification: Boolean? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            if (uploadNotificationAddLogin != null)
                put("upload_notification_add_login", uploadNotificationAddLogin)
            if (uploadNotificationDeleteLogin != null)
                put("upload_notification_delete_login", uploadNotificationDeleteLogin)
            if (selfUploadNotification != null)
                put("upload_notification_me", asApiBoolean(selfUploadNotification))
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("set_settings", requestProperties)
        )
    }

    fun addGetFileStorageFilesRequest(folderId: String? = null, getFiles: Boolean? = null, getFolder: Boolean? = null, getFolders: Boolean? = null, getRoot: Boolean? = null, limit: Int? = null, offset: Int? = null, recursive: Boolean? = null, searchMode: SearchMode? = null, searchString: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            if (folderId != null)
                put("folder_id", folderId)
            if (getFiles != null)
                put("get_files", asApiBoolean(getFiles))
            if (getFolder != null)
                put("get_folder", asApiBoolean(getFolder))
            if (getFolders != null)
                put("get_folders", asApiBoolean(getFolders))
            if (getRoot != null)
                put("get_root", asApiBoolean(getRoot))
            if (limit != null)
                put("limit", limit)
            if (offset != null)
                put("offset", offset)
            if (recursive != null)
                put("recursive", asApiBoolean(recursive))
            if (searchMode != null)
                put("search_option", searchMode.id)
            if (searchString != null)
                put("search_string", searchString)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("get_entries", requestParams)
        )
    }

    fun addGetFileRequest(id: String, limit: Int? = null, offset: Int? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (limit != null)
                put("limit", limit)
            if (offset != null)
                put("offset", offset)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("get_file", requestParams)
        )
    }

    fun addGetPreviewDownloadUrlRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("get_preview_download_url", requestProperties)
        )
    }

    fun addGetFileDownloadUrlRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("get_file_download_url", requestProperties)
        )
    }

    fun addGetFileProxyNonceRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("get_file_proxy_nonce", requestProperties)
        )
    }

    fun addAddFileRequest(data: String, folderId: String, name: String, description: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("data", data)
            put("folder_id", folderId)
            put("name", name)
            if (description != null)
                put("description", description)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("add_file", requestParams)
        )
    }

    fun addAddSparseFileRequest(folderId: String, name: String, size: Int, description: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("folder_id", folderId)
            put("name", name)
            put("size", size)
            if (description != null)
                put("description", description)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("add_sparse_file", requestProperties)
        )
    }

    fun addImportSessionFileRequest(id: String, createCopy: Boolean? = null, description: String? = null, fileId: String? = null, folderId: String? = null, sparseKey: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
            if (createCopy != null)
                put("create_copy", createCopy)
            if (description != null)
                put("description", description)
            if (fileId != null)
                put("file_id", fileId)
            if (folderId != null)
                put("folder_id", folderId)
            if (sparseKey != null)
                put("sparse_key", sparseKey)
        }
        
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("import_session_file", requestProperties)
        )
    }

    fun addExportSessionFileRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("export_session_file", requestProperties)
        )
    }

    fun addDeleteFileRequest(fileId: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", fileId)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("delete_file", requestProperties)
        )
    }

    fun addAddFolderRequest(folderId: String, name: String, description: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("folder_id", folderId)
            put("name", name)
            if (description != null) put("description", description)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("add_folder", requestProperties)
        )
    }

    fun addDeleteFolderRequest(folderId: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", folderId)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("delete_folder", requestProperties)
        )
    }

    fun addSetFileRequest(fileId: String, description: String? = null, downloadNotificationAddLogin: String? = null, downloadNotificationDeleteLogin: String? = null, downloadNotificationMe: Boolean? = null, name: String? = null, folderId: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", fileId)
            if (description != null) put("description", description)
            if (downloadNotificationAddLogin != null) put("download_notification_add_login", downloadNotificationAddLogin)
            if (downloadNotificationDeleteLogin != null) put("download_notification_delete_login", downloadNotificationDeleteLogin)
            if (downloadNotificationMe != null) put("download_notification_me", asApiBoolean(downloadNotificationMe))
            if (name != null) put("name", name)
            if (folderId != null) put("folder_id", name)
            if (downloadNotificationMe != null) put("download_notification_me", asApiBoolean(downloadNotificationMe))
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("set_file", requestProperties)
        )
    }

    fun addSetFolderRequest(folderId: String, description: String? = null, name: String? = null, readable: Boolean? = null, uploadNotificationAddLogin: String? = null, uploadNotificationDeleteLogin: String? = null, uploadNotificationMe: Boolean? = null, writable: Boolean? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", folderId)
            if (description != null) put("description", description)
            if (name != null) put("name", name)
            if (readable != null) put("readable", asApiBoolean(readable))
            if (uploadNotificationAddLogin != null) put("upload_notification_add_login", uploadNotificationAddLogin)
            if (uploadNotificationDeleteLogin != null) put("upload_notification_delete_login", uploadNotificationDeleteLogin)
            if (uploadNotificationMe != null) put("upload_notification_me", asApiBoolean(uploadNotificationMe))
            if (writable != null) put("writable", asApiBoolean(writable))
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("set_folder", requestProperties)
        )
    }

    fun addGetTrashRequest(limit: Int? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            if (limit != null)
                put("limit", limit)
        }
        return listOf(
                addSetFocusRequest("files", login),
                addRequest("get_trash", requestParams)
        )
    }

    fun addGetEmailStateRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("get_state", null)
        )
    }

    fun addSendEmailRequest(to: String, subject: String, plainBody: String, text: String?, bcc: String?, cc: String?, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("to", to)
            put("subject", subject)
            put("body_plain", plainBody)
            if (text != null)
                put("text", text)
            if (bcc != null)
                put("bcc", bcc)
            if (cc != null)
                put("cc", cc)
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("send_mail", requestParams)
        )
    }

    fun addAddEmailFolderRequest(name: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("name", name)
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("add_folder", requestParams)
        )
    }

    fun addSetEmailFolderRequest(folderId: String, name: String?, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            if (name != null)
                put("name", name)
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("set_folder", requestParams)
        )
    }

    fun addDeleteEmailFolderRequest(folderId: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("delete_folder", requestParams)
        )
    }

    fun addGetEmailFoldersRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("get_folders", null)
        )
    }

    fun addGetEmailsRequest(folderId: String, limit: Int?, offset: Int?, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            if (limit != null)
                put("limit", limit)
            if (offset != null)
                put("offset", offset)
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("get_messages", requestParams)
        )
    }

    fun addReadEmailRequest(folderId: String, messageId: Int, peek: Boolean?, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            put("message_id", messageId)
            if (peek != null)
                put("peek", asApiBoolean(peek))
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("read_message", requestParams)
        )
    }

    fun addSetEmailRequest(folderId: String, messageId: Int, isFlagged: Boolean?, isUnread: Boolean?, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            put("message_id", messageId)
            if (isFlagged != null)
                put("is_flagged", asApiBoolean(isFlagged))
            if (isUnread != null)
                put("is_unread", asApiBoolean(isUnread))
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("set_message", requestParams)
        )
    }

    fun addMoveEmailRequest(folderId: String, messageId: Int, destinationFolderId: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            put("message_id", messageId)
            put("target_folder_id", destinationFolderId)
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("move_message", requestParams)
        )
    }

    fun addDeleteEmailRequest(folderId: String, messageId: Int, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            put("message_id", messageId)
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("delete_message", requestParams)
        )
    }

    fun addGetEmailSignatureRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("get_signature", null)
        )
    }

    fun addSetEmailSignatureRequest(text: String, positionAnswer: String?, positionForward: String?, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("text", text)
            if (positionAnswer != null)
                put("position_answer", positionAnswer)
            if (positionForward != null)
                put("position_forward", positionForward)
        }
        return listOf(
                addSetFocusRequest("mailbox", login),
                addRequest("set_signature", requestParams)
        )
    }

    fun addGetTasksRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("tasks", login),
                addRequest("get_entries", null)
        )
    }

    fun addAddTaskRequest(title: String, completed: Boolean? = null, description: String? = null, dueDate: Long? = null, startDate: Long? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("title", title)
            if (completed != null)
                put("completed", asApiBoolean(completed))
            if (description != null)
                put("description", description)
            if (dueDate != null)
                put("due_date", dueDate / 1000)
            if (startDate != null)
                put("start_date", startDate / 1000)
        }
        return listOf(
                addSetFocusRequest("tasks", login),
                addRequest("add_entry", requestParams)
        )
    }

    fun addSetTaskRequest(id: String, completed: Boolean? = null, description: String? = null, dueDate: Long? = null, startDate: Long? = null, title: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (completed != null)
                put("completed", asApiBoolean(completed))
            if (description != null)
                put("description", description)
            if (dueDate != null)
                put("due_date", dueDate / 1000)
            if (startDate != null)
                put("start_date", startDate / 1000)
            if (title != null)
                put("title", title)
        }
        return listOf(
                addSetFocusRequest("tasks", login),
                addRequest("set_entry", requestParams)
        )
    }

    fun addDeleteTaskRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("tasks", login),
                addRequest("delete_entry", requestParams)
        )
    }

    fun addGetContactsRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("addresses", login),
                addRequest("get_entries", null)
        )
    }

    fun addAddContactRequest(categories: String? = null, firstName: String? = null, lastName: String? = null, homeStreet: String? = null, homeStreet2: String? = null, homePostalCode: String? = null, homeCity: String? = null, homeState: String? = null, homeCountry: String? = null, homeCoords: String? = null, homePhone: String? = null, homeFax: String? = null, mobilePhone: String? = null, birthday: String? = null, email: String? = null, gender: Gender? = null, hobby: String? = null, notes: String? = null, website: String? = null, company: String? = null, companyType: String? = null, jobTitle: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            if (categories != null) put("categories", categories)
            if (firstName != null) put("firstname", firstName)
            if (lastName != null) put("lastname", lastName)
            if (homeStreet != null) put("homestreet", homeStreet)
            if (homeStreet2 != null) put("homestreet2", homeStreet2)
            if (homePostalCode != null) put("homepostalcode", homePostalCode)
            if (homeCity != null) put("homecity", homeCity)
            if (homeState != null) put("homestate", homeState)
            if (homeCountry != null) put("homecountry", homeCountry)
            if (homeCoords != null) put("homecoords", homeCoords)
            if (homePhone != null) put("homephone", homePhone)
            if (homeFax != null) put("homefax", homeFax)
            if (mobilePhone != null) put("mobilephone", mobilePhone)
            if (birthday != null) put("birthday", birthday)
            if (email != null) put("emailaddress", email)
            if (gender != null) put("gender", gender.id)
            if (hobby != null) put("hobby", hobby)
            if (notes != null) put("notes", notes)
            if (website != null) put("webpage", website)
            if (company != null) put("company", company)
            if (companyType != null) put("companytype", companyType)
            if (jobTitle != null) put("jobtitle", jobTitle)
        }
        return listOf(
                addSetFocusRequest("addresses", login),
                addRequest("add_entry", requestProperties)
        )
    }

    //TODO not sure allowing to change the uid
    //TODO seems like there are more properties?
    fun addSetContactRequest(id: String, categories: String? = null, firstName: String? = null, lastName: String? = null, homeStreet: String? = null, homeStreet2: String? = null, homePostalCode: String? = null, homeCity: String? = null, homeState: String? = null, homeCountry: String? = null, homeCoords: String? = null, homePhone: String? = null, homeFax: String? = null, mobilePhone: String? = null, birthday: String? = null, email: String? = null, gender: Gender? = null, hobby: String? = null, notes: String? = null, website: String? = null, company: String? = null, companyType: String? = null, jobTitle: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
            if (categories != null) put("categories", categories)
            if (firstName != null) put("firstname", firstName)
            if (lastName != null) put("lastname", lastName)
            if (homeStreet != null) put("homestreet", homeStreet)
            if (homeStreet2 != null) put("homestreet2", homeStreet2)
            if (homePostalCode != null) put("homepostalcode", homePostalCode)
            if (homeCity != null) put("homecity", homeCity)
            if (homeState != null) put("homestate", homeState)
            if (homeCountry != null) put("homecountry", homeCountry)
            if (homeCoords != null) put("homecoords", homeCoords)
            if (homePhone != null) put("homephone", homePhone)
            if (homeFax != null) put("homefax", homeFax)
            if (mobilePhone != null) put("mobilephone", mobilePhone)
            if (birthday != null) put("birthday", birthday)
            if (email != null) put("emailaddress", email)
            if (gender != null) put("gender", gender.id)
            if (hobby != null) put("hobby", hobby)
            if (notes != null) put("notes", notes)
            if (website != null) put("webpage", website)
            if (company != null) put("company", company)
            if (companyType != null) put("companytype", companyType)
            if (jobTitle != null) put("jobtitle", jobTitle)
        }
        return listOf(
                addSetFocusRequest("addresses", login),
                addRequest("set_entry", requestProperties)
        )
    }

    fun addDeleteContactRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("addresses", login),
                addRequest("delete_entry", requestProperties)
        )
    }

    fun addGetAppointmentsRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("calendar", login),
                addRequest("get_entries", null)
        )
    }

    fun addAddAppointmentRequest(title: String, description: String? = null, endDate: Long? = null, endDateIso: String? = null, location: String? = null, rrule: String? = null, startDate: Long? = null, startDateIso: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("title", title)
            if (description != null) put("description", description)
            if (endDate != null) put("end_date", endDate / 1000)
            if (endDateIso != null) put("end_date_iso", endDateIso)
            if (location != null) put("location", location)
            if (rrule != null) put("rrule", rrule)
            if (startDate != null) put("start_date", startDate / 1000)
            if (startDateIso != null) put("start_date_iso", startDateIso)
        }
        return listOf(
                addSetFocusRequest("calendar", login),
                addRequest("add_entry", requestProperties)
        )
    }

    fun addSetAppointmentRequest(id: String, title: String? = null, description: String? = null, endDate: Long? = null, endDateIso: String? = null, location: String? = null, rrule: String? = null, startDate: Long? = null, startDateIso: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
            if (title != null) put("title", title)
            if (description != null) put("description", description)
            if (endDate != null) put("end_date", endDate / 1000)
            if (endDateIso != null) put("end_date_iso", endDateIso)
            if (location != null) put("location", location)
            if (rrule != null) put("rrule", rrule)
            if (startDate != null) put("start_date", startDate / 1000)
            if (startDateIso != null) put("start_date_iso", startDateIso)
        }
        return listOf(
                addSetFocusRequest("calendar", login),
                addRequest("set_entry", requestProperties)
        )
    }

    fun addDeleteAppointmentRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("calendar", login),
                addRequest("delete_entry", requestProperties)
        )
    }

    fun addGetTeacherBoardNotificationsRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("board_teacher", login),
                addRequest("get_entries", null)
        )
    }

    fun addAddTeacherBoardNotificationRequest(title: String, text: String, color: BoardNotificationColor? = null, killDate: Long? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("title", title)
            put("text", text)
            if (color != null)
                put("color", color.serialId)
            if (killDate != null)
                put("kill_date", killDate / 1000)
        }
        return listOf(
                addSetFocusRequest("board_teacher", login),
                addRequest("add_entry", requestParams)
        )
    }

    fun addSetTeacherBoardNotificationRequest(id: String, title: String? = null, text: String? = null, color: BoardNotificationColor? = null, killDate: Long? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (title != null)
                put("title", title)
            if (text != null)
                put("text", text)
            if (color != null)
                put("color", color.serialId)
            if (killDate != null)
                put("kill_date", killDate / 1000)
        }
        return listOf(
                addSetFocusRequest("board_teacher", login),
                addRequest("set_entry", requestParams)
        )
    }

    fun addDeleteTeacherBoardNotificationRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("board_teacher", login),
                addRequest("delete_entry", requestParams)
        )
    }

    fun addGetPupilBoardNotificationsRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("board_pupil", login),
                addRequest("get_entries", null)
        )
    }

    fun addAddPupilBoardNotificationRequest(title: String, text: String, color: BoardNotificationColor? = null, killDate: Long? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("title", title)
            put("text", text)
            if (color != null)
                put("color", color.serialId)
            if (killDate != null)
                put("kill_date", killDate / 1000)
        }
        return listOf(
                addSetFocusRequest("board_pupil", login),
                addRequest("add_entry", requestParams)
        )
    }

    fun addSetPupilBoardNotificationRequest(id: String, title: String? = null, text: String? = null, color: BoardNotificationColor? = null, killDate: Long? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (title != null)
                put("title", title)
            if (text != null)
                put("text", text)
            if (color != null)
                put("color", color.serialId)
            if (killDate != null)
                put("kill_date", killDate / 1000)
        }
        return listOf(
                addSetFocusRequest("board_pupil", login),
                addRequest("set_entry", requestParams)
        )
    }

    fun addDeletePupilBoardNotificationRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("board_pupil", login),
                addRequest("delete_entry", requestParams)
        )
    }

    fun addGetBoardNotificationsRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("board", login),
                addRequest("get_entries", null)
        )
    }

    fun addAddBoardNotificationRequest(title: String, text: String, color: BoardNotificationColor? = null, killDate: Long? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("title", title)
            put("text", text)
            if (color != null)
                put("color", color.serialId)
            if (killDate != null)
                put("kill_date", killDate / 1000)
        }
        return listOf(
                addSetFocusRequest("board", login),
                addRequest("add_entry", requestParams)
        )
    }

    fun addSetBoardNotificationRequest(id: String, title: String? = null, text: String? = null, color: BoardNotificationColor? = null, killDate: Long? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (title != null)
                put("title", title)
            if (text != null)
                put("text", text)
            if (color != null)
                put("color", color.serialId)
            if (killDate != null)
                put("kill_date", killDate / 1000)
        }
        return listOf(
                addSetFocusRequest("board", login),
                addRequest("set_entry", requestParams)
        )
    }

    fun addDeleteBoardNotificationRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("board", login),
                addRequest("delete_entry", requestParams)
        )
    }

    fun fireRequest(): ApiResponse {
        return fireRequest(operator.getContext())
    }

}