package de.deftk.lonet.api.model.abstract

import de.deftk.lonet.api.model.Permission
import de.deftk.lonet.api.model.feature.Quota
import de.deftk.lonet.api.model.feature.Task
import de.deftk.lonet.api.model.feature.abstract.*
import de.deftk.lonet.api.model.feature.calendar.Appointment
import de.deftk.lonet.api.model.feature.contact.Contact
import de.deftk.lonet.api.model.feature.contact.Gender
import de.deftk.lonet.api.model.feature.files.FileStorageSettings
import de.deftk.lonet.api.model.feature.files.OnlineFile
import de.deftk.lonet.api.model.feature.files.filters.FileFilter
import de.deftk.lonet.api.model.feature.files.session.SessionFile
import de.deftk.lonet.api.model.feature.mailbox.EmailFolder
import de.deftk.lonet.api.model.feature.mailbox.EmailSignature
import de.deftk.lonet.api.request.OperatorApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.platform.CryptoUtil
import kotlinx.serialization.json.*

abstract class AbstractOperator(private val login: String, private val name: String, val baseRights: List<Permission?>, val effectiveRights: List<Permission?>, private val type: ManageableType) : IManageable, IMailbox, IFileStorage, ITaskList, IContactHolder, ICalendar {

    abstract fun getContext(): IContext

    override fun getEmailStatus(): Pair<Quota, Int> {
        val request = OperatorApiRequest(this)
        val id = request.addGetEmailStateRequest()[1]
        val response = request.fireRequest(getContext())
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Pair(Json.decodeFromJsonElement(subResponse["quota"]!!.jsonObject), subResponse["unread_messages"]!!.jsonPrimitive.int)
    }

    override fun getEmailQuota(): Quota {
        return getEmailStatus().first
    }

    override fun getUnreadEmailCount(): Int {
        return getEmailStatus().second
    }

    override fun getEmailFolders(): List<EmailFolder> {
        val request = OperatorApiRequest(this)
        val id = request.addGetEmailFoldersRequest()[1]
        val response = request.fireRequest(getContext())
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["folders"]!!.jsonArray.map { EmailFolder.fromJson(it.jsonObject, this) }
    }

    override fun addEmailFolder(name: String) {
        val request = OperatorApiRequest(this)
        request.addAddEmailFolderRequest(name)
        val response = request.fireRequest(getContext())
        ResponseUtil.checkSuccess(response.toJson())
    }

    override fun sendEmail(to: String, subject: String, plainBody: String, text: String?, bcc: String?, cc: String?) {
        val request = OperatorApiRequest(this)
        request.addSendEmailRequest(to, subject, plainBody, text, bcc, cc)
        val response = request.fireRequest(getContext())
        ResponseUtil.checkSuccess(response.toJson())
    }

    override fun getEmailSignature(): EmailSignature {
        val request = OperatorApiRequest(this)
        val id = request.addGetEmailSignatureRequest()[1]
        val response = request.fireRequest(getContext())
        return EmailSignature.fromJson(ResponseUtil.getSubResponseResult(response.toJson(), id)["signature"]!!.jsonObject, this)
    }

    override fun getFileStorageState(): Pair<FileStorageSettings, Quota> {
        val request = OperatorApiRequest(this)
        val id = request.addGetFileStorageStateRequest()[1]
        val response = request.fireRequest(getContext())
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Pair(Json.decodeFromJsonElement(subResponse["settings"]!!.jsonObject), Json.decodeFromJsonElement(subResponse["quota"]!!.jsonObject))
    }

    override fun getFiles(filter: FileFilter?): List<OnlineFile> {
        val request = OperatorApiRequest(this)
        val id = request.addGetFileStorageFilesRequest(
                "/",
                recursive = false,
                getFiles = true,
                getFolders = true,
                searchString = filter?.searchTerm,
                searchMode = filter?.searchMode
        )[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["entries"]?.jsonArray?.map { OnlineFile.fromJson(it.jsonObject, this) }
                ?: emptyList()
    }

    override fun addFile(name: String, data: ByteArray, description: String?): OnlineFile {
        val request = OperatorApiRequest(this)
        val id = request.addAddFileRequest(CryptoUtil.encodeToString(data), "/", name, description)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return OnlineFile.fromJson(subResponse["file"]!!.jsonObject, this)
    }

    override fun addSparseFile(name: String, size: Int, description: String?): OnlineFile {
        val request = OperatorApiRequest(this)
        val id = request.addAddSparseFileRequest("/", name, size, description, login)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return OnlineFile.fromJson(subResponse["file"]!!.jsonObject, this)
    }

    override fun importSessionFile(sessionFile: SessionFile, createCopy: Boolean?, description: String?): OnlineFile {
        val request = OperatorApiRequest(this)
        val id = request.addImportSessionFileRequest(sessionFile.id, createCopy, description, folderId = "/")[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return OnlineFile.fromJson(subResponse["file"]!!.jsonObject, this)
    }

    override fun addFolder(name: String, description: String?): OnlineFile {
        val request = OperatorApiRequest(this)
        val id = request.addAddFolderRequest("/", name, description)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return OnlineFile.fromJson(subResponse["folder"]!!.jsonObject, this)
    }

    override fun getTrash(limit: Int?): List<OnlineFile> {
        val request = OperatorApiRequest(this)
        val id = request.addGetTrashRequest(limit)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["files"]!!.jsonArray.map { OnlineFile.fromJson(it.jsonObject, this) }
    }

    override fun setReadable(readable: Boolean) {
        val request = OperatorApiRequest(this)
        request.addSetFolderRequest("/", readable = readable)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    override fun setWritable(writable: Boolean) {
        val request = OperatorApiRequest(this)
        request.addSetFolderRequest("/", writable = writable)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    override fun getTasks(): List<Task> {
        val request = OperatorApiRequest(this)
        val id = request.addGetTasksRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["entries"]?.jsonArray?.map { Task.fromJson(it.jsonObject, this) } ?: emptyList()
    }

    override fun addTask(title: String, completed: Boolean?, description: String?, dueDate: Long?, startDate: Long?): Task {
        val request = OperatorApiRequest(this)
        val id = request.addAddTaskRequest(title, completed, description, dueDate, startDate)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Task.fromJson(subResponse["entry"]!!.jsonObject, this)
    }

    override fun getContacts(): List<Contact> {
        val request = OperatorApiRequest(this)
        val id = request.addGetContactsRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["entries"]?.jsonArray?.map { Contact.fromJson(it.jsonObject, this) } ?: emptyList()
    }

    override fun addContact(categories: String?, firstName: String?, lastName: String?, homeStreet: String?, homeStreet2: String?, homePostalCode: String?, homeCity: String?, homeState: String?, homeCountry: String?, homeCoords: String?, homePhone: String?, homeFax: String?, mobilePhone: String?, birthday: String?, email: String?, gender: Gender?, hobby: String?, notes: String?, website: String?, company: String?, companyType: String?, jobTitle: String?): Contact {
        val request = OperatorApiRequest(this)
        val id = request.addAddContactRequest(categories, firstName, lastName, homeStreet, homeStreet2, homePostalCode, homeCity, homeState, homeCountry, homeCoords, homePhone, homeFax, mobilePhone, birthday, email, gender, hobby, notes, website, company, companyType, jobTitle)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Contact.fromJson(subResponse["entry"]!!.jsonObject, this)
    }

    override fun getAppointments(): List<Appointment> {
        val request = OperatorApiRequest(this)
        val id = request.addGetAppointmentsRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["entries"]!!.jsonArray.map { Appointment.fromJson(it.jsonObject, this) }
    }

    override fun addAppointment(title: String, description: String?, endDate: Long?, endDateIso: String?, location: String?, rrule: String?, startDate: Long?, startDateIso: String?): Appointment {
        val request = OperatorApiRequest(this)
        val id = request.addAddAppointmentRequest(title, description, endDate, endDateIso, location, rrule, startDate, startDateIso, login)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Appointment.fromJson(subResponse["entry"]!!.jsonObject, this)
    }

    override fun getLogin(): String {
        return login
    }

    override fun getName(): String {
        return name
    }

    override fun getType(): ManageableType {
        return type
    }

}