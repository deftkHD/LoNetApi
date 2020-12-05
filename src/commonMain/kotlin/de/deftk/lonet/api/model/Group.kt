package de.deftk.lonet.api.model

import de.deftk.lonet.api.exception.ApiException
import de.deftk.lonet.api.model.abstract.*
import de.deftk.lonet.api.model.feature.Quota
import de.deftk.lonet.api.model.feature.WikiPage
import de.deftk.lonet.api.model.feature.board.BoardNotification
import de.deftk.lonet.api.model.feature.board.BoardNotificationColor
import de.deftk.lonet.api.model.feature.board.BoardType
import de.deftk.lonet.api.model.feature.courselets.Courselet
import de.deftk.lonet.api.model.feature.courselets.CourseletDownload
import de.deftk.lonet.api.model.feature.courselets.Mapping
import de.deftk.lonet.api.model.feature.files.FileStorageSettings
import de.deftk.lonet.api.model.feature.forum.ForumPost
import de.deftk.lonet.api.model.feature.forum.ForumPostIcon
import de.deftk.lonet.api.model.feature.forum.ForumSettings
import de.deftk.lonet.api.request.GroupApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import kotlinx.serialization.json.*

open class Group(login: String, name: String, type: ManageableType, val baseUser: IManageable?, val fullName: String?, val passwordMustChange: Boolean, baseRights: List<Permission?>, val reducedRights: List<Permission?>, val memberRights: List<Permission?>, effectiveRights: List<Permission?>, private val context: IContext) : AbstractOperator(login, name, baseRights, effectiveRights, type), IGroup {

    companion object {
        fun fromJson(jsonObject: JsonObject, context: IContext): Group {
            val baseRights = mutableListOf<Permission?>()
            jsonObject["base_rights"]?.jsonArray?.forEach { perm ->
                baseRights.add(Permission.getByName(perm.jsonPrimitive.toString()))
            }
            baseRights.add(Permission.SELF)

            return Group(
                    jsonObject["login"]!!.jsonPrimitive.toString(),
                    jsonObject["name_hr"]!!.jsonPrimitive.toString(),
                    ManageableType.getById(jsonObject["type"]!!.jsonPrimitive.int),
                    if (jsonObject.containsKey("base_user")) Json.decodeFromJsonElement(jsonObject["base_user"]!!.jsonObject) else null,
                    jsonObject["fullname"]?.jsonPrimitive?.toString(),
                    jsonObject["password_must_change"]?.jsonPrimitive?.int == 1,
                    baseRights,
                    jsonObject["reduced_rights"]?.jsonArray?.map { Permission.getByName(it.jsonPrimitive.toString()) } ?: emptyList(),
                    jsonObject["member_rights"]?.jsonArray?.map { Permission.getByName(it.jsonPrimitive.toString()) } ?: emptyList(),
                    jsonObject["effective_rights"]?.jsonArray?.map { Permission.getByName(it.jsonPrimitive.toString()) } ?: emptyList(),
                    context
            )
        }
    }

    override fun getMembers(): List<IManageable> {
        val request = GroupApiRequest(this)
        val id = request.addGetMembersRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["users"]?.jsonArray?.map { getContext().getOrCreateManageable(it.jsonObject) } ?: emptyList()
    }

    override fun getBoardNotifications(): List<BoardNotification> {
        val request = GroupApiRequest(this)
        val id = request.addGetBoardNotificationsRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["entries"]?.jsonArray?.map { BoardNotification.fromJson(it.jsonObject, this, BoardType.ALL) } ?: emptyList()
    }

    override fun addBoardNotification(title: String, text: String, color: BoardNotificationColor?, killDate: Long?): BoardNotification {
        val request = GroupApiRequest(this)
        val id = request.addAddBoardNotificationRequest(title, text, color, killDate)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return BoardNotification.fromJson(subResponse["entry"]!!.jsonObject, this, BoardType.ALL)
    }

    override fun getTeacherBoardNotifications(): List<BoardNotification> {
        val request = GroupApiRequest(this)
        val id = request.addGetTeacherBoardNotificationsRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["entries"]?.jsonArray?.map { BoardNotification.fromJson(it.jsonObject, this, BoardType.TEACHER) } ?: emptyList()
    }

    override fun addTeacherBoardNotification(title: String, text: String, color: BoardNotificationColor?, killDate: Long?): BoardNotification {
        val request = GroupApiRequest(this)
        val id = request.addAddTeacherBoardNotificationRequest(title, text, color, killDate)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return BoardNotification.fromJson(subResponse["entry"]!!.jsonObject, this, BoardType.TEACHER)
    }

    override fun getPupilBoardNotifications(): List<BoardNotification> {
        val request = GroupApiRequest(this)
        val id = request.addGetPupilBoardNotificationsRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["entries"]?.jsonArray?.map { BoardNotification.fromJson(it.jsonObject, this, BoardType.PUPIL) } ?: emptyList()
    }

    override fun addPupilBoardNotification(title: String, text: String, color: BoardNotificationColor?, killDate: Long?): BoardNotification {
        val request = GroupApiRequest(this)
        val id = request.addAddPupilBoardNotificationRequest(title, text, color, killDate)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return BoardNotification.fromJson(subResponse["entry"]!!.jsonObject, this, BoardType.PUPIL)
    }

    override fun getFileStorageState(): Pair<FileStorageSettings, Quota> {
        val request = GroupApiRequest(this)
        val id = request.addGetFileStorageStateRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Pair(Json.decodeFromJsonElement(subResponse["settings"]!!.jsonObject), Json.decodeFromJsonElement(subResponse["quota"]!!.jsonObject))
    }

    override fun getForumState(): Pair<Quota, ForumSettings> {
        val request = GroupApiRequest(this)
        val id = request.addGetForumStateRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Pair(Json.decodeFromJsonElement(subResponse["quota"]!!.jsonObject), Json.decodeFromJsonElement(subResponse["settings"]!!.jsonObject))
    }

    override fun getForumPosts(parentId: String?): List<ForumPost> {
        val request = GroupApiRequest(this)
        val id = request.addGetForumPostsRequest(parentId = parentId)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        val allPosts = subResponse["entries"]!!.jsonArray.map { ForumPost.fromJson(it.jsonObject, this) }
        // build tree structure
        val rootPosts = mutableListOf<ForumPost>()
        val tmpPosts = mutableMapOf<String, ForumPost>()
        allPosts.forEach { post -> tmpPosts[post.id] = post }
        allPosts.forEach { post ->
            if (post.parentId != "0") {
                val parent = tmpPosts[post.parentId] ?: throw ApiException("Comment has invalid parent!")
                parent.comments.add(post)
            } else {
                rootPosts.add(post)
            }
        }
        return rootPosts
    }

    override fun addForumPost(title: String, text: String, icon: ForumPostIcon, parentId: String, importSessionFile: String?, importSessionFiles: Array<String>?, replyNotificationMe: Boolean?): ForumPost {
        val request = GroupApiRequest(this)
        val id = request.addAddForumPostRequest(title, text, icon, parentId, importSessionFile, importSessionFiles, replyNotificationMe)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return ForumPost.fromJson(subResponse["entry"]!!.jsonObject, this)
    }

    override fun getWikiPage(name: String?): WikiPage? {
        val request = GroupApiRequest(this)
        val id = request.addGetWikiPageRequest(name)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Json.decodeFromJsonElement(subResponse["page"]!!.jsonObject)
    }

    override fun getCourseletsState(): Pair<Quota, String> {
        val request = GroupApiRequest(this)
        val id = request.addGetCourseletsStateRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Pair(Json.decodeFromJsonElement(subResponse["quota"]!!.jsonObject), subResponse["runtime_version_hash"]!!.jsonPrimitive.toString())
    }

    override fun getCourseletsConfiguration(): JsonObject {
        val request = GroupApiRequest(this)
        val id = request.addGetCourseletsConfigurationRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["configuration"]!!.jsonObject
    }

    override fun getCourselets(): List<Courselet> {
        val request = GroupApiRequest(this)
        val id = request.addGetCourseletsRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["courselets"]!!.jsonArray.map { Courselet.fromJson(it.jsonObject, this) }
    }

    override fun getCourseletMappings(): List<Mapping> {
        val request = GroupApiRequest(this)
        val id = request.addGetCourseletMappingsRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["mappings"]!!.jsonArray.map { Mapping.fromJson(it.jsonObject, this) }
    }

    override fun addCourseletMapping(name: String): Mapping {
        val request = GroupApiRequest(this)
        val id = request.addAddCourseletMappingRequest(name)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Mapping.fromJson(subResponse["mapping"]!!.jsonObject, this)
    }

    override fun exportCourseletRuntime(): CourseletDownload {
        val request = GroupApiRequest(this)
        val id = request.addExportCourseletRuntimeRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return Json.decodeFromJsonElement(subResponse["file"]!!.jsonObject)
    }

    override fun getContext(): IContext {
        return context
    }

    override fun toString(): String {
        return getLogin()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Group

        if (getLogin() != other.getLogin()) return false
        return true
    }

    override fun hashCode(): Int {
        return getLogin().hashCode()
    }

}