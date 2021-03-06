package de.deftk.lonet.api.model.feature.systemnotification

import de.deftk.lonet.api.model.IRequestContext
import de.deftk.lonet.api.model.RemoteScope
import java.util.*

interface ISystemNotification {

    val id: String
    val messageType: SystemNotificationType
    val date: Date
    val message: String
    val data: String?
    val member: RemoteScope
    val group: RemoteScope
    val fromId: Int?
    val isUnread: Boolean
    val obj: String?

    fun delete(context: IRequestContext)

}