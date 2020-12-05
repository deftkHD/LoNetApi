package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.Quota
import de.deftk.lonet.api.model.feature.forum.ForumPost
import de.deftk.lonet.api.model.feature.forum.ForumPostIcon
import de.deftk.lonet.api.model.feature.forum.ForumSettings

interface IForum {

    fun getForumState(): Pair<Quota, ForumSettings>
    fun getForumPosts(parentId: String? = null): List<ForumPost>
    fun addForumPost(title: String, text: String, icon: ForumPostIcon, parentId: String = "0", importSessionFile: String? = null, importSessionFiles: Array<String>? = null, replyNotificationMe: Boolean? = null): ForumPost

}