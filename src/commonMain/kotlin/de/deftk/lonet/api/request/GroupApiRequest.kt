package de.deftk.lonet.api.request

import de.deftk.lonet.api.model.Group
import de.deftk.lonet.api.model.feature.courselets.TemplatePackage
import de.deftk.lonet.api.model.feature.forum.ForumPostIcon
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

open class GroupApiRequest(val group: Group) : OperatorApiRequest(group) {

    fun addGetMembersRequest(login: String = group.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("members", login),
                addRequest("get_users", null)
        )
    }

    fun addGetForumStateRequest(login: String = group.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("forum", login),
                addRequest("get_state", null)
        )
    }

    fun addGetForumPostRequest(id: String? = null, login: String = group.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("forum", login),
                addRequest("get_entry", requestProperties)
        )
    }

    fun addGetForumPostsRequest(parentId: String? = null, login: String = group.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestProperties = buildJsonObject {
            if (parentId != null)
                put("parent_id", parentId)
        }
        return listOf(
                addSetFocusRequest("forum", login),
                addRequest("get_entries", requestProperties)
        )
    }

    fun addAddForumPostRequest(title: String, text: String, icon: ForumPostIcon, parentId: String = "0", importSessionFile: String? = null, importSessionFiles: Array<String>? = null, replyNotificationMe: Boolean? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("title", title)
            put("text", text)
            put("icon", icon.id)
            put("parent_id", parentId)
            if (importSessionFile != null) put("import_session_file", importSessionFile)
            if (importSessionFiles != null) {
                val sessionFiles = buildJsonArray {
                    importSessionFiles.forEach { add(it) }
                }
                put("import_session_files", sessionFiles)
            }
            if (replyNotificationMe != null) put("reply_notification_me", replyNotificationMe)
        }
        return listOf(
                addSetFocusRequest("forum", login),
                addRequest("add_entry", requestParams)
        )
    }

    fun addDeleteForumPostRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("forum", login),
                addRequest("delete_entry", requestParams)
        )
    }

    fun addForumExportSessionFileRequest(fileId: String, id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("file_id", fileId)
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("forum", login),
                addRequest("export_session_file", requestParams)
        )
    }

    fun addGetWikiPageRequest(name: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            if (name != null)
                put("name", name)
        }
        return listOf(
                addSetFocusRequest("wiki", login),
                addRequest("get_page", requestParams)
        )
    }

    fun addGetCourseletsStateRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("get_state", null)
        )
    }

    fun addGetCourseletsConfigurationRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("get_configuration", null)
        )
    }

    fun addGetCourseletProgressRequest(ids: List<Int>? = null, since: Long? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            if (ids != null) {
                val array = buildJsonArray {
                    ids.forEach { add(it) }
                }
                put("ids", array)
            }
            if (since != null)
                put("since", since / 1000)
        }

        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("get_progress", requestParams)
        )
    }

    fun addAddCourseletResultRequest(id: Int, pageId: String, score: Int? = null, time: Long? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            put("page_id", pageId)
            if (score != null)
                put("score", score)
            if (time != null)
                put("time", time / 1000)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("add_result", requestParams)
        )
    }

    fun addSetCourseletSuspendDataRequest(id: Int, suspendData: String, ifLatest: Int? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            put("suspend_data", suspendData)
            if (ifLatest != null)
                put("if_latest", ifLatest)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("set_suspend_data", requestParams)
        )
    }

    fun addGetCourseletResultsRequest(id: Int, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("get_results", requestParams)
        )
    }

    fun addDeleteCourseletResultsRequest(id: Int, pageId: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (pageId != null)
                put("page_id", pageId)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("delete_results", requestParams)
        )
    }

    fun addAddCourseletBookmarkRequest(id: Int, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("add_bookmark", requestParams)
        )
    }

    fun addDeleteCourseletBookmarkRequest(id: Int, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("delete_bookmark", requestParams)
        )
    }

    fun addExportCourseletRuntimeRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("export_runtime", null)
        )
    }

    fun addGetTemplatesRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("get_templates", null)
        )
    }

    fun addExportTemplateRequest(id: String, pkg: TemplatePackage? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (pkg != null)
                put("package", pkg.serialId)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("export_template", requestParams)
        )
    }

    fun addGetCourseletMappingsRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("get_mappings", null)
        )
    }

    fun addAddCourseletMappingRequest(name: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("name", name)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("add_mapping", requestParams)
        )
    }

    fun addSetCourseletMappingRequest(id: Int, name: String? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            put("name", name)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("set_mapping", requestParams)
        )
    }

    fun addDeleteCourseletMappingRequest(id: Int, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("delete_mapping", requestParams)
        )
    }

    fun addGetCourseletsRequest(login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("get_courselets", null)
        )
    }

    fun addImportCourseletRequest(id: String, isVisible: Boolean? = null, mapping: Int? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (isVisible != null)
                put("is_visible", asApiBoolean(isVisible))
            if (mapping != null)
                put("mapping", mapping)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("import_courselet", requestParams)
        )
    }

    fun addExportCourseletRequest(id: String, pkg: TemplatePackage? = null, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
            if (pkg != null)
                put("pkg", pkg.serialId)
        }

        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("export_courselet", requestParams)
        )
    }

    fun addDeleteCourseletRequest(id: String, login: String = operator.getLogin()): List<Int> {
        ensureCapacity(2)
        val requestParams = buildJsonObject {
            put("id", id)
        }
        return listOf(
                addSetFocusRequest("courselets", login),
                addRequest("delete_courselet", requestParams)
        )
    }


}