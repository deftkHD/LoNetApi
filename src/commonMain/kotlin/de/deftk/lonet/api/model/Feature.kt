package de.deftk.lonet.api.model

import de.deftk.lonet.api.model.Permission.*

enum class Feature(val permissions: List<Permission>) {
    STATISTICS(listOf(SYSADMIN)),

    ADMINISTRATION_GROUP(listOf(ADMIN, ADMIN_WRITE, ADMIN_ADMIN)),
    ADMINISTRATION_USER(listOf(ADMIN, ADMIN_WRITE, ADMIN_ADMIN)),
    ADMINISTRATION_ROOT(listOf(ADMIN, ADMIN_WRITE, ADMIN_ADMIN)),

    MESSENGER(listOf(Permission.MESSENGER, MESSENGER_WRITE, MESSENGER_ADMIN)),
    MAILBOX(listOf(Permission.MAILBOX, MAILBOX_WRITE, MAILBOX_ADMIN)),
    MEMBERS(listOf(Permission.MEMBERS, MEMBERS_WRITE, MEMBERS_ADMIN)),
    ADDRESSES(listOf(Permission.ADDRESSES, ADDRESSES_WRITE, ADDRESSES_ADMIN)),
    CALENDAR(listOf(Permission.CALENDAR, CALENDAR_WRITE, CALENDAR_ADMIN)),
    NOTES(listOf(Permission.NOTES, NOTES_WRITE, NOTES_ADMIN)),
    BOARD(listOf(Permission.BOARD, BOARD_WRITE, BOARD_ADMIN)),
    BOARD_PUPIL(listOf(Permission.BOARD_PUPIL, BOARD_PUPIL_WRITE, BOARD_PUPIL_ADMIN)),
    TASKS(listOf(Permission.TASKS, TASKS_WRITE, TASKS_ADMIN)),
    BOOKS(listOf(Permission.BOOKS, BOOKS_WRITE, BOOKS_ADMIN)),
    BOOKMARKS(listOf(Permission.BOOKMARKS, BOOKMARKS_WRITE , BOOKMARKS_ADMIN)),
    COURSELETS(listOf(Permission.COURSELETS, COURSELETS_WRITE, COURSELETS_ADMIN)),
    FORUM(listOf(Permission.FORUM, FORUM_WRITE, FORUM_ADMIN)),
    WIKI(listOf(Permission.WIKI, WIKI_WRITE, WIKI_ADMIN)),
    RESOURCEMANAGEMENT(listOf(Permission.RESOURCEMANAGEMENT, RESOURCEMANAGEMENT_WRITE, RESOURCEMANAGEMENT_ADMIN)),
    FILES(listOf(Permission.FILES, FILES_WRITE, FILES_ADMIN)),
    CHAT(listOf(Permission.CHAT, CHAT_WRITE, CHAT_ADMIN)),
    BLOG(listOf(Permission.BLOG, BLOG_WRITE, BLOG_ADMIN)),
    SHOWCASE(listOf(Permission.SHOWCASE, SHOWCASE_WRITE, SHOWCASE_ADMIN)),
    WEBSITE(listOf(Permission.WEBSITE, WEBSITE_WRITE, WEBSITE_ADMIN)),
    WS_GEN(listOf(Permission.WS_GEN, WS_GEN_WRITE, WS_GEN_ADMIN)),
    POLL(listOf(Permission.POLL, POLL_WRITE, POLL_ADMIN)),
    LEARNING_PLAN(listOf(Permission.LEARNING_PLAN, LEARNING_PLAN_WRITE, LEARNING_PLAN_ADMIN)),
    TIMETABLE(listOf(Permission.TIMETABLE, TIMETABLE_WRITE, TIMETABLE_ADMIN)),
    MAILING_LIST(listOf(MAILING_LISTS, MAILING_LISTS_WRITE, MAILING_LISTS_ADMIN)),
    META(listOf(Permission.META, META_WRITE, META_ADMIN)),

    PROFILE(listOf(SELF)),
    SESSION_FILES(listOf(SELF)),
    LICENSES(listOf(SELF)),
    MESSAGES(listOf(SELF)),
    TRUSTS(listOf(SELF)),
    SETTINGS(listOf(SELF)),
    PROXY(listOf(SELF)),

    PASSWORD(listOf(Permission.PASSWORD)),
    CLIENT(listOf(Permission.CLIENT));

    fun isAvailable(permissions: List<Permission?>): Boolean {
        this.permissions.forEach { permission ->
            if (permissions.contains(permission))
                return true
        }
        return false
    }

    companion object {
        fun getAvailableFeatures(permission: Permission): List<Feature> {
            return values().filter { it.permissions.contains(permission) }
        }
    }
}