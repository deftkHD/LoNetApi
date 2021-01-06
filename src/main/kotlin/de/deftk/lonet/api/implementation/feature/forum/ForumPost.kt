package de.deftk.lonet.api.implementation.feature.forum

import de.deftk.lonet.api.model.IRequestContext
import de.deftk.lonet.api.model.feature.Modification
import de.deftk.lonet.api.model.feature.forum.ForumPostIcon
import de.deftk.lonet.api.model.feature.forum.IForumPost
import de.deftk.lonet.api.request.GroupApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.serialization.BooleanFromIntSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class ForumPost(
    private val id: String,
    @SerialName("parent_id")
    private val parentId: String,
    private val title: String,
    private val text: String,
    private val icon: ForumPostIcon? = null,
    private val level: Int,
    @SerialName("comment_count")
    private val commentCount: Int? = null,
    @Serializable(with = BooleanFromIntSerializer::class)
    private val pinned: Boolean? = null,
    @Serializable(with = BooleanFromIntSerializer::class)
    private val locked: Boolean? = null,
    private val children: ChildrenData? = null,
    private val files: List<JsonElement> = emptyList(),
    private val created: Modification,
    private val modified: Modification
): IForumPost {

    private val comments = mutableListOf<IForumPost>()

    var deleted = false
        private set

    override fun getCreated(): Modification = created
    override fun getModified(): Modification = modified
    override fun getId(): String = id
    override fun getParentId(): String = parentId
    override fun getTitle(): String = title
    override fun getText(): String = text
    override fun getIcon(): ForumPostIcon? = icon
    override fun getLevel(): Int = level
    override fun getCommentCount(): Int? = commentCount
    override fun isPinned(): Boolean? = pinned
    override fun isLocked(): Boolean? = locked
    override fun getComments(): MutableList<IForumPost> = comments

    override fun delete(context: IRequestContext) {
        val request = GroupApiRequest(context)
        request.addDeleteForumPostRequest(id)
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
        deleted = true
    }

    @Serializable
    data class ChildrenData(val count: Int, val recent: Modification? = null)

}