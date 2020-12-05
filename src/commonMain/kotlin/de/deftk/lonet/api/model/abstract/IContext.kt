package de.deftk.lonet.api.model.abstract

import de.deftk.lonet.api.model.Group
import de.deftk.lonet.api.model.User
import de.deftk.lonet.api.request.handler.IRequestHandler
import kotlinx.serialization.json.JsonObject

interface IContext {

    fun getSessionId(): String
    fun setSessionId(sessionId: String)

    fun getUser(): User
    fun getGroups(): List<Group>
    fun getOperator(login: String): AbstractOperator?
    fun getOrCreateManageable(jsonObject: JsonObject): IManageable

    fun getRequestUrl(): String

    fun getRequestHandler(): IRequestHandler
    fun setRequestHandler(requestHandler: IRequestHandler)

}