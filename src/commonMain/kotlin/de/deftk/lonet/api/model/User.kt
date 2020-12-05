package de.deftk.lonet.api.model

import de.deftk.lonet.api.LoNet
import de.deftk.lonet.api.model.abstract.*
import de.deftk.lonet.api.model.feature.SystemNotification
import de.deftk.lonet.api.model.feature.Task
import de.deftk.lonet.api.model.feature.board.BoardNotification
import de.deftk.lonet.api.model.feature.board.BoardType
import de.deftk.lonet.api.model.feature.files.session.SessionFile
import de.deftk.lonet.api.request.UserApiRequest
import de.deftk.lonet.api.request.handler.DefaultRequestHandler
import de.deftk.lonet.api.request.handler.IRequestHandler
import de.deftk.lonet.api.response.ApiResponse
import de.deftk.lonet.api.response.ResponseUtil
import kotlinx.serialization.json.*

class User(login: String, name: String, type: ManageableType, val baseUser: IManageable?, val fullName: String?, val groups: List<Group>, val passwordMustChange: Boolean, baseRights: List<Permission?>, effectiveRights: List<Permission?>, val authKey: String, private val context: IContext) : AbstractOperator(login, name, baseRights, effectiveRights, type), IUser {

    companion object {
        fun fromResponse(response: ApiResponse, apiUrl: String, authKey: String): User {
            val jsonResponse = response.toJson().jsonArray
            val loginResponse = ResponseUtil.getSubResponseResultByMethod(jsonResponse, "login")
            val informationResponse = ResponseUtil.getSubResponseResultByMethod(jsonResponse, "get_information")

            val jsonObject = loginResponse["user"]!!.jsonObject

            val baseRights = mutableListOf<Permission?>()
            jsonObject.get("base_rights")?.jsonArray?.forEach { perm ->
                baseRights.add(Permission.getByName(perm.jsonPrimitive.toString()))
            }
            baseRights.add(Permission.SELF)

            val context = UserContext(informationResponse["session_id"]!!.jsonPrimitive.toString(), apiUrl)
            context.user = User(
                    jsonObject["login"]!!.jsonPrimitive.toString(),
                    jsonObject["name_hr"]!!.jsonPrimitive.toString(),
                    ManageableType.getById(jsonObject["type"]!!.jsonPrimitive.int),
                    if (jsonObject.containsKey("base_user")) Json.decodeFromJsonElement(jsonObject["base_user"]!!.jsonObject) else null,
                    jsonObject["fullname"]?.jsonPrimitive?.toString(),
                    loginResponse["member"]!!.jsonArray.map { Group.fromJson(it.jsonObject, context) },
                    jsonObject["password_must_change"]?.jsonPrimitive?.int == 1,
                    baseRights,
                    jsonObject["effective_rights"]!!.jsonArray.map { Permission.getByName(it.jsonPrimitive.toString()) },
                    authKey,
                    context
            )
            return context.getUser()
        }
    }

    override fun getAllTasks(): List<Task> {
        val request = UserApiRequest(this)
        val taskIds = request.addGetAllTasksRequest()
        val response = request.fireRequest().toJson().jsonArray
        val tasks = mutableListOf<Task>()
        val responses = response.filter { taskIds.contains(it.jsonObject["id"]!!.jsonPrimitive.int) }.map { it.jsonObject }
        responses.withIndex().forEach { (index, subResponse) ->
            if (index % 2 == 1) {
                val focus = responses[index - 1]["result"]!!.jsonObject
                check(focus["method"]!!.jsonPrimitive.toString() == "set_focus")
                val memberLogin = focus["user"]!!.jsonObject["login"]!!.jsonPrimitive.toString()
                val member = getContext().getOperator(memberLogin)!!
                subResponse["result"]!!.jsonObject["entries"]!!.jsonArray.forEach { taskResponse ->
                    tasks.add(Task.fromJson(taskResponse.jsonObject, member))
                }
            }
        }
        return tasks
    }

    override fun getAllBoardNotifications(): List<BoardNotification> {
        val request = UserApiRequest(this)
        val notificationIds = request.addGetAllNotificationsRequest()
        val response = request.fireRequest().toJson().jsonArray
        val notifications = mutableListOf<BoardNotification>()
        val responses = response.filter { notificationIds.contains(it.jsonObject["id"]!!.jsonPrimitive.int) }.map { it.jsonObject }
        responses.withIndex().forEach { (index, subResponse) ->
            if (index % 2 == 1) {
                val focus = responses[index - 1]["result"]!!.jsonObject
                check(focus["method"]!!.jsonPrimitive.toString() == "set_focus")
                val memberLogin = focus["user"]!!.jsonObject["login"]!!.jsonPrimitive.toString()
                val member = getContext().getOperator(memberLogin)!!
                subResponse["result"]!!.jsonObject["entries"]!!.jsonArray.forEach { taskResponse ->
                    notifications.add(BoardNotification.fromJson(taskResponse.jsonObject, member, BoardType.ALL))
                }
            }
        }
        return notifications
    }

    override fun getAllTeacherBoardNotifications(): List<BoardNotification> {
        val request = UserApiRequest(this)
        val notificationIds = request.addGetAllTeacherNotificationsRequest()
        val response = request.fireRequest().toJson().jsonArray
        val notifications = mutableListOf<BoardNotification>()
        val responses = response.filter { notificationIds.contains(it.jsonObject["id"]!!.jsonPrimitive.int) }.map { it.jsonObject }
        responses.withIndex().forEach { (index, subResponse) ->
            if (index % 2 == 1) {
                val focus = responses[index - 1]["result"]!!.jsonObject
                check(focus["method"]!!.jsonPrimitive.toString() == "set_focus")
                val memberLogin = focus["user"]!!.jsonObject["login"]!!.jsonPrimitive.toString()
                val member = getContext().getOperator(memberLogin)!!
                subResponse["result"]!!.jsonObject["entries"]!!.jsonArray.forEach { taskResponse ->
                    notifications.add(BoardNotification.fromJson(taskResponse.jsonObject, member, BoardType.TEACHER))
                }
            }
        }
        return notifications
    }

    override fun getAllPupilBoardNotifications(): List<BoardNotification> {
        val request = UserApiRequest(this)
        val notificationIds = request.addGetAllPupilNotificationsRequest()
        val response = request.fireRequest().toJson().jsonArray
        val notifications = mutableListOf<BoardNotification>()
        val responses = response.filter { notificationIds.contains(it.jsonObject["id"]!!.jsonPrimitive.int) }.map { it.jsonObject }
        responses.withIndex().forEach { (index, subResponse) ->
            if (index % 2 == 1) {
                val focus = responses[index - 1]["result"]!!.jsonObject
                check(focus["method"]!!.jsonPrimitive.toString() == "set_focus")
                val memberLogin = focus["user"]!!.jsonObject["login"]!!.jsonPrimitive.toString()
                val member = getContext().getOperator(memberLogin)!!
                subResponse["result"]!!.jsonObject["entries"]!!.jsonArray.forEach { taskResponse ->
                    notifications.add(BoardNotification.fromJson(taskResponse.jsonObject, member, BoardType.PUPIL))
                }
            }
        }
        return notifications
    }

    override fun logout(removeTrust: Boolean) {
        val request = UserApiRequest(this)
        request.addRequest("logout", null)
        if (removeTrust) {
            val tmpUser = LoNet.loginToken(getLogin(), authKey, true)
            tmpUser.logout(false)
        }
        val response = request.fireRequest(getContext())
        ResponseUtil.checkSuccess(response.toJson())
    }

    override fun getAutoLoginUrl(): String {
        val request = UserApiRequest(this)
        val id = request.addGetAutoLoginUrlRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["url"]!!.jsonPrimitive.toString()
    }

    override fun getSystemNotifications(): List<SystemNotification> {
        val request = UserApiRequest(this)
        val id = request.addGetSystemNotificationsRequest()[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return subResponse["messages"]?.jsonArray?.map { SystemNotification.fromJson(it.jsonObject, this) }
                ?: emptyList()
    }

    override fun checkSession(): Boolean {
        val request = UserApiRequest(this)
        val response = request.fireRequest()
        return runCatching { ResponseUtil.checkSuccess(response.toJson()) }.isSuccess
    }

    override fun registerService(type: IUser.ServiceType, token: String, application: String?, generateSecret: String?, isOnline: Boolean?, managedObjects: String?, unmanagedPriority: Int?) {
        val request = UserApiRequest(this)
        request.addRegisterServiceRequest(type, token, application, generateSecret, isOnline, managedObjects, unmanagedPriority)[1]
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    override fun unregisterService(type: IUser.ServiceType, token: String) {
        val request = UserApiRequest(this)
        request.addUnregisterServiceRequest(type, token)[1]
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

    override fun addSessionFile(name: String, data: ByteArray): SessionFile {
        val request = UserApiRequest(this)
        val id = request.addAddSessionFileRequest(name, data)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        return SessionFile.fromJson(subResponse["file"]!!.jsonObject, this)
    }

    override fun getContext(): IContext {
        return context
    }

    class UserContext(private var sessionId: String, private val requestUrl: String): IContext {

        internal lateinit var user: User
        private var requestHandler: IRequestHandler = DefaultRequestHandler()

        override fun getSessionId(): String {
            return sessionId
        }

        override fun setSessionId(sessionId: String) {
            this.sessionId = sessionId
        }

        override fun getUser(): User {
            return user
        }

        override fun getGroups(): List<Group> {
            return user.groups
        }

        override fun getOperator(login: String): AbstractOperator? {
            if (login == user.getLogin()) return user
            return getGroups().firstOrNull { it.getLogin() == login }
        }

        override fun getOrCreateManageable(jsonObject: JsonObject): IManageable {
            return getOperator(jsonObject["login"]!!.jsonPrimitive.toString()) ?: Json.decodeFromJsonElement(jsonObject)
        }

        override fun getRequestUrl(): String {
            return requestUrl
        }

        override fun getRequestHandler(): IRequestHandler {
            return requestHandler
        }

        override fun setRequestHandler(requestHandler: IRequestHandler) {
            this.requestHandler = requestHandler
        }

    }

}