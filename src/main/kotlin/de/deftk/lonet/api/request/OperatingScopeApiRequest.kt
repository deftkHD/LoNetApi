package de.deftk.lonet.api.request

import de.deftk.lonet.api.LoNetClient
import de.deftk.lonet.api.model.IRequestContext
import de.deftk.lonet.api.model.feature.contacts.Gender
import de.deftk.lonet.api.model.feature.filestorage.filter.SearchMode
import de.deftk.lonet.api.model.feature.mailbox.SignaturePosition
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put

open class OperatingScopeApiRequest(context: IRequestContext): ScopedApiRequest(context) {

    fun addGetFileStorageStateRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("get_state", null)
        )
    }

    //TODO implement
    fun addGetFileStorageSettingsRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("get_settings", null)
        )
    }

    //TODO implement
    fun addSetFileStorageSettingsRequest(uploadNotificationAddLogin: String?, uploadNotificationDeleteLogin: String?, selfUploadNotification: Boolean? = null, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("set_settings", requestProperties)
        )
    }

    fun addGetFileStorageFilesRequest(folderId: String? = null, getFiles: Boolean? = null, getFolder: Boolean? = null, getFolders: Boolean? = null, getRoot: Boolean? = null, limit: Int? = null, offset: Int? = null, recursive: Boolean? = null, searchMode: SearchMode? = null, searchString: String? = null, login: String = context.login): List<Int> {
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
                put("search_option", LoNetClient.json.encodeToString(searchMode))
            if (searchString != null)
                put("search_string", searchString)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("get_entries", requestParams)
        )
    }

    fun addGetFileRequest(id: String, limit: Int? = null, offset: Int? = null, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (limit != null)
                put("limit", limit)
            if (offset != null)
                put("offset", offset)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("get_file", requestParams)
        )
    }

    fun addGetPreviewDownloadUrlRequest(id: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("get_preview_download_url", requestProperties)
        )
    }

    fun addGetFileDownloadUrlRequest(id: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("get_file_download_url", requestProperties)
        )
    }

    fun addGetFileProxyNonceRequest(id: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("get_file_proxy_nonce", requestProperties)
        )
    }

    fun addAddFileRequest(data: String, folderId: String, name: String, description: String? = null, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("data", data)
            put("folder_id", folderId)
            put("name", name)
            if (description != null)
                put("description", description)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("add_file", requestParams)
        )
    }

    fun addAddSparseFileRequest(folderId: String, name: String, size: Int, description: String? = null, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("folder_id", folderId)
            put("name", name)
            put("size", size)
            if (description != null)
                put("description", description)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("add_sparse_file", requestProperties)
        )
    }

    fun addImportSessionFileRequest(id: String, createCopy: Boolean? = null, description: String? = null, fileId: String? = null, folderId: String? = null, sparseKey: String? = null, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("import_session_file", requestProperties)
        )
    }

    fun addExportSessionFileRequest(id: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("export_session_file", requestProperties)
        )
    }

    fun addDeleteFileRequest(fileId: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", fileId)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("delete_file", requestProperties)
        )
    }

    fun addAddFolderRequest(folderId: String, name: String, description: String? = null, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("folder_id", folderId)
            put("name", name)
            if (description != null) put("description", description)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("add_folder", requestProperties)
        )
    }

    fun addDeleteFolderRequest(folderId: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", folderId)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("delete_folder", requestProperties)
        )
    }

    fun addSetFileRequest(fileId: String, description: String? = null, downloadNotificationAddLogin: String? = null, downloadNotificationDeleteLogin: String? = null, downloadNotificationMe: Boolean? = null, name: String? = null, folderId: String? = null, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("set_file", requestProperties)
        )
    }

    fun addSetFolderRequest(folderId: String, description: String? = null, name: String? = null, readable: Boolean? = null, uploadNotificationAddLogin: String? = null, uploadNotificationDeleteLogin: String? = null, uploadNotificationMe: Boolean? = null, writable: Boolean? = null, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("set_folder", requestProperties)
        )
    }

    fun addGetTrashRequest(limit: Int? = null, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            if (limit != null)
                put("limit", limit)
        }
        return listOf(
            addSetFocusRequest(Focusable.FILES, login),
            addRequest("get_trash", requestParams)
        )
    }

    fun addGetEmailStateRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("get_state", null)
        )
    }

    fun addSendEmailRequest(to: String, subject: String, plainBody: String, text: String?, bcc: String?, cc: String?, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("send_mail", requestParams)
        )
    }

    fun addAddEmailFolderRequest(name: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("name", name)
        }
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("add_folder", requestParams)
        )
    }

    fun addSetEmailFolderRequest(folderId: String, name: String?, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            if (name != null)
                put("name", name)
        }
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("set_folder", requestParams)
        )
    }

    fun addDeleteEmailFolderRequest(folderId: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
        }
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("delete_folder", requestParams)
        )
    }

    fun addGetEmailFoldersRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("get_folders", null)
        )
    }

    fun addGetEmailsRequest(folderId: String, limit: Int?, offset: Int?, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            if (limit != null)
                put("limit", limit)
            if (offset != null)
                put("offset", offset)
        }
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("get_messages", requestParams)
        )
    }

    fun addReadEmailRequest(folderId: String, messageId: Int, peek: Boolean?, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            put("message_id", messageId)
            if (peek != null)
                put("peek", asApiBoolean(peek))
        }
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("read_message", requestParams)
        )
    }

    fun addSetEmailRequest(folderId: String, messageId: Int, isFlagged: Boolean?, isUnread: Boolean?, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("set_message", requestParams)
        )
    }

    fun addMoveEmailRequest(folderId: String, messageId: Int, destinationFolderId: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            put("message_id", messageId)
            put("target_folder_id", destinationFolderId)
        }
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("move_message", requestParams)
        )
    }

    fun addDeleteEmailRequest(folderId: String, messageId: Int, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("folder_id", folderId)
            put("message_id", messageId)
        }
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("delete_message", requestParams)
        )
    }

    fun addExportEmailSessionFileRequest(fileId: String, folderId: String, messageId: Int, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val params = buildJsonObject {
            put("file_id", fileId)
            put("folder_id", folderId)
            put("message_id", messageId)
        }
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("export_session_file", params)
        )
    }

    fun addGetEmailSignatureRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("get_signature", null)
        )
    }

    fun addSetEmailSignatureRequest(text: String, positionAnswer: SignaturePosition?, positionForward: SignaturePosition?, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("text", text)
            if (positionAnswer != null)
                put("position_answer", LoNetClient.json.encodeToJsonElement(positionAnswer))
            if (positionForward != null)
                put("position_forward", LoNetClient.json.encodeToJsonElement(positionForward))
        }
        return listOf(
            addSetFocusRequest(Focusable.MAILBOX, login),
            addRequest("set_signature", requestParams)
        )
    }

    fun addGetTasksRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.TASKS, login),
            addRequest("get_entries", null)
        )
    }

    fun addAddTaskRequest(title: String, completed: Boolean? = null, description: String? = null, dueDate: Long? = null, startDate: Long? = null, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.TASKS, login),
            addRequest("add_entry", requestParams)
        )
    }

    fun addSetTaskRequest(id: String, completed: Boolean? = null, description: String? = null, dueDate: Long? = null, startDate: Long? = null, title: String? = null, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.TASKS, login),
            addRequest("set_entry", requestParams)
        )
    }

    fun addDeleteTaskRequest(id: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
            addSetFocusRequest(Focusable.TASKS, login),
            addRequest("delete_entry", requestParams)
        )
    }

    fun addGetContactsRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.ADDRESSES, login),
            addRequest("get_entries", null)
        )
    }

    fun addAddContactRequest(birthday: String? = null, businessCity: String? = null, businessCoords: String? = null, businessCountry: String? = null, businessFax: String? = null, businessPhone: String? = null, businessPostalCode: String? = null, businessState: String? = null, businessStreet: String? = null, businessStreet2: String? = null, businessStreet3: String? = null, categories: String? = null, company: String? = null, companyType: String? = null, email2Address: String? = null, email3Address: String? = null, emailAddress: String? = null, firstName: String? = null, fullName: String? = null, gender: Gender? = null, hobby: String? = null, homeCity: String? = null, homeCoords: String? = null, homeCountry: String? = null, homeFax: String? = null, homePhone: String? = null, homePostalCode: String? = null, homeState: String? = null, homeStreet: String? = null, homeStreet2: String? = null, homeStreet3: String? = null, jobTitle: String? = null, jobTitle2: String? = null, lastName: String? = null, middleName: String? = null, mobilePhone: String? = null, nickName: String? = null, notes: String? = null, subjects: String? = null, suffix: String? = null, title: String? = null, uid: String? = null, webPage: String? = null, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            if (birthday != null) put("birthday", birthday)
            if (businessCity != null) put("businesscity", businessCity)
            if (businessCoords != null) put("businesscoords", businessCoords)
            if (businessCountry != null) put("businesscountry", businessCountry)
            if (businessFax != null) put("businessfax", businessFax)
            if (businessPhone != null) put("businessphone", businessPhone)
            if (businessPostalCode != null) put("businesspostalcode", businessPostalCode)
            if (businessState != null) put("businessstate", businessState)
            if (businessStreet != null) put("businessstreet", businessStreet)
            if (businessStreet2 != null) put("businessstreet2", businessStreet2)
            if (businessStreet3 != null) put("businesstreet3", businessStreet3)
            if (categories != null) put("categories", categories)
            if (company != null) put("company", company)
            if (companyType != null) put("companytype", companyType)
            if (emailAddress != null) put("emailaddress", emailAddress)
            if (email2Address != null) put("email2address", email2Address)
            if (email3Address != null) put("email3address", email3Address)
            if (firstName != null) put("firstname", firstName)
            if (fullName != null) put("fullname", fullName)
            if (gender != null) put("gender", LoNetClient.json.encodeToString(gender))
            if (hobby != null) put("hobby", hobby)
            if (homeCity != null) put("homecity", homeCity)
            if (homeCoords != null) put("homecoords", homeCoords)
            if (homeCountry != null) put("homecountry", homeCountry)
            if (homeFax != null) put("homefax", homeFax)
            if (homePhone != null) put("homephone", homePhone)
            if (homePostalCode != null) put("homepostalcode", homePostalCode)
            if (homeState != null) put("homestate", homeState)
            if (homeStreet != null) put("homestreet", homeStreet)
            if (homeStreet2 != null) put("homestreet2", homeStreet2)
            if (homeStreet3 != null) put("homestreet3", homeStreet3)
            if (jobTitle != null) put("jobtitle", jobTitle)
            if (jobTitle2 != null) put("jobtitle2", jobTitle2)
            if (lastName != null) put("lastname", lastName)
            if (middleName != null) put("middlename", middleName)
            if (mobilePhone != null) put("mobilephone", mobilePhone)
            if (nickName != null) put("nickname", nickName)
            if (notes != null) put("notes", notes)
            if (subjects != null) put("subjects", subjects)
            if (suffix != null) put("suffix", suffix)
            if (title != null) put("title", title)
            if (uid != null) put("uid", uid)
            if (webPage != null) put("webpage", webPage)
        }
        return listOf(
            addSetFocusRequest(Focusable.ADDRESSES, login),
            addRequest("add_entry", requestProperties)
        )
    }

    fun addSetContactRequest(id: String, birthday: String? = null, businessCity: String? = null, businessCoords: String? = null, businessCountry: String? = null, businessFax: String? = null, businessPhone: String? = null, businessPostalCode: String? = null, businessState: String? = null, businessStreet: String? = null, businessStreet2: String? = null, businessStreet3: String? = null, categories: String? = null, company: String? = null, companyType: String? = null, email2Address: String? = null, email3Address: String? = null, emailAddress: String? = null, firstName: String? = null, fullName: String? = null, gender: Gender? = null, hobby: String? = null, homeCity: String? = null, homeCoords: String? = null, homeCountry: String? = null, homeFax: String? = null, homePhone: String? = null, homePostalCode: String? = null, homeState: String? = null, homeStreet: String? = null, homeStreet2: String? = null, homeStreet3: String? = null, jobTitle: String? = null, jobTitle2: String? = null, lastName: String? = null, middleName: String? = null, mobilePhone: String? = null, nickName: String? = null, notes: String? = null, subjects: String? = null, suffix: String? = null, title: String? = null, uid: String? = null, webPage: String? = null, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
            if (birthday != null) put("birthday", birthday)
            if (businessCity != null) put("businesscity", businessCity)
            if (businessCoords != null) put("businesscoords", businessCoords)
            if (businessCountry != null) put("businesscountry", businessCountry)
            if (businessFax != null) put("businessfax", businessFax)
            if (businessPhone != null) put("businessphone", businessPhone)
            if (businessPostalCode != null) put("businesspostalcode", businessPostalCode)
            if (businessState != null) put("businessstate", businessState)
            if (businessStreet != null) put("businessstreet", businessStreet)
            if (businessStreet2 != null) put("businessstreet2", businessStreet2)
            if (businessStreet3 != null) put("businesstreet3", businessStreet3)
            if (categories != null) put("categories", categories)
            if (company != null) put("company", company)
            if (companyType != null) put("companytype", companyType)
            if (emailAddress != null) put("emailaddress", emailAddress)
            if (email2Address != null) put("email2address", email2Address)
            if (email3Address != null) put("email3address", email3Address)
            if (firstName != null) put("firstname", firstName)
            if (fullName != null) put("fullname", fullName)
            if (gender != null) put("gender", LoNetClient.json.encodeToString(gender))
            if (hobby != null) put("hobby", hobby)
            if (homeCity != null) put("homecity", homeCity)
            if (homeCoords != null) put("homecoords", homeCoords)
            if (homeCountry != null) put("homecountry", homeCountry)
            if (homeFax != null) put("homefax", homeFax)
            if (homePhone != null) put("homephone", homePhone)
            if (homePostalCode != null) put("homepostalcode", homePostalCode)
            if (homeState != null) put("homestate", homeState)
            if (homeStreet != null) put("homestreet", homeStreet)
            if (homeStreet2 != null) put("homestreet2", homeStreet2)
            if (homeStreet3 != null) put("homestreet3", homeStreet3)
            if (jobTitle != null) put("jobtitle", jobTitle)
            if (jobTitle2 != null) put("jobtitle2", jobTitle2)
            if (lastName != null) put("lastname", lastName)
            if (middleName != null) put("middlename", middleName)
            if (mobilePhone != null) put("mobilephone", mobilePhone)
            if (nickName != null) put("nickname", nickName)
            if (notes != null) put("notes", notes)
            if (subjects != null) put("subjects", subjects)
            if (suffix != null) put("suffix", suffix)
            if (title != null) put("title", title)
            if (uid != null) put("uid", uid)
            if (webPage != null) put("webpage", webPage)
        }
        return listOf(
            addSetFocusRequest(Focusable.ADDRESSES, login),
            addRequest("set_entry", requestProperties)
        )
    }

    fun addDeleteContactRequest(id: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
            addSetFocusRequest(Focusable.ADDRESSES, login),
            addRequest("delete_entry", requestProperties)
        )
    }

    fun addGetAppointmentsRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.CALENDAR, login),
            addRequest("get_entries", null)
        )
    }

    fun addAddAppointmentRequest(title: String, description: String? = null, endDate: Long? = null, endDateIso: String? = null, location: String? = null, rrule: String? = null, startDate: Long? = null, startDateIso: String? = null, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.CALENDAR, login),
            addRequest("add_entry", requestProperties)
        )
    }

    fun addSetAppointmentRequest(id: String, title: String? = null, description: String? = null, endDate: Long? = null, endDateIso: String? = null, location: String? = null, rrule: String? = null, startDate: Long? = null, startDateIso: String? = null, login: String = context.login): List<Int> {
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
            addSetFocusRequest(Focusable.CALENDAR, login),
            addRequest("set_entry", requestProperties)
        )
    }

    fun addDeleteAppointmentRequest(id: String, login: String = context.login): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
            addSetFocusRequest(Focusable.CALENDAR, login),
            addRequest("delete_entry", requestProperties)
        )
    }

    fun addGetTeacherBoardNotificationsRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.BOARD_TEACHER, login),
            addRequest("get_entries", null)
        )
    }

    fun addGetPupilBoardNotificationsRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.BOARD_PUPIL, login),
            addRequest("get_entries", null)
        )
    }

    fun addGetBoardNotificationsRequest(login: String = context.login): List<Int> {
        ensureCapacity(2)
        return listOf(
            addSetFocusRequest(Focusable.BOARD, login),
            addRequest("get_entries", null)
        )
    }

}