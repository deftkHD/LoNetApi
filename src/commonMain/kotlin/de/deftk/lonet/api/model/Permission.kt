package de.deftk.lonet.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Permission(val id: String) {

    @SerialName("sysadmin") SYSADMIN("sysadmin"),

    @SerialName("admin") ADMIN("admin"),
    @SerialName("admin_write") ADMIN_WRITE("admin_write"),
    @SerialName("admin_admin") ADMIN_ADMIN("admin_admin"),

    @SerialName("messenger") MESSENGER("messenger"),
    @SerialName("messenger_write") MESSENGER_WRITE("messenger_write"),
    @SerialName("messenger_admin") MESSENGER_ADMIN("messenger_admin"),

    @SerialName("mail") MAILBOX("mail"),
    @SerialName("mail_write") MAILBOX_WRITE("mail_write"),
    @SerialName("mail_admin") MAILBOX_ADMIN("mail_admin"),

    @SerialName("mailing_lists") MAILING_LISTS("mailing_lists"),
    @SerialName("mailing_lists_write") MAILING_LISTS_WRITE("mailing_lists_write"),
    @SerialName("mailing_lists_admin") MAILING_LISTS_ADMIN("mailing_lists_admin"),

    @SerialName("members") MEMBERS("members"),
    @SerialName("members_write") MEMBERS_WRITE("members_write"),
    @SerialName("members_admin") MEMBERS_ADMIN("members_admin"),

    @SerialName("address") ADDRESSES("address"),
    @SerialName("address_write") ADDRESSES_WRITE("address_write"),
    @SerialName("address_admin") ADDRESSES_ADMIN("address_admin"),

    @SerialName("calendar") CALENDAR("calendar"),
    @SerialName("calendar_write") CALENDAR_WRITE("calendar_write"),
    @SerialName("calendar_admin") CALENDAR_ADMIN("calendar_admin"),

    @SerialName("chat") CHAT("chat"),
    @SerialName("chat_write") CHAT_WRITE("chat_write"),
    @SerialName("chat_admin") CHAT_ADMIN("chat_admin"),

    @SerialName("notes") NOTES("notes"),
    @SerialName("notes_write") NOTES_WRITE("notes_write"),
    @SerialName("notes_admin") NOTES_ADMIN("notes_admin"),

    @SerialName("books") BOOKS("books"),
    @SerialName("books_write") BOOKS_WRITE("books_write"),
    @SerialName("books_admin") BOOKS_ADMIN("books_admin"),

    @SerialName("bookmarks") BOOKMARKS("bookmarks"),
    @SerialName("bookmarks_write") BOOKMARKS_WRITE("bookmarks_write"),
    @SerialName("bookmarks_admin") BOOKMARKS_ADMIN("bookmarks_admin"),

    @SerialName("board") BOARD("board"),
    @SerialName("board_write") BOARD_WRITE("board_write"),
    @SerialName("board_admin") BOARD_ADMIN("board_admin"),

    @SerialName("board_pupil") BOARD_PUPIL("board_pupil"),
    @SerialName("board_pupil_write") BOARD_PUPIL_WRITE("board_pupil_write"),
    @SerialName("board_pupil_admin") BOARD_PUPIL_ADMIN("board_pupil_admin"),

    @SerialName("tasks") TASKS("tasks"),
    @SerialName("tasks_write") TASKS_WRITE("tasks_write"),
    @SerialName("tasks_admin") TASKS_ADMIN("tasks_admin"),

    @SerialName("courselets") COURSELETS("courselets"),
    @SerialName("courselets_write") COURSELETS_WRITE("courselets_write"),
    @SerialName("courselets_admin") COURSELETS_ADMIN("courselets_admin"),

    @SerialName("forum") FORUM("forum"),
    @SerialName("forum_write") FORUM_WRITE("forum_write"),
    @SerialName("forum_admin") FORUM_ADMIN("forum_admin"),

    @SerialName("wiki") WIKI("wiki"),
    @SerialName("wiki_write") WIKI_WRITE("wiki_write"),
    @SerialName("wiki_admin") WIKI_ADMIN("wiki_admin"),

    @SerialName("resourcemanagement") RESOURCEMANAGEMENT("resourcemanagement"),
    @SerialName("resourcemanagement_write") RESOURCEMANAGEMENT_WRITE("resourcemanagement_write"),
    @SerialName("resourcemanagement_admin") RESOURCEMANAGEMENT_ADMIN("resourcemanagement_admin"),

    @SerialName("files") FILES("files"),
    @SerialName("files_write") FILES_WRITE("files_write"),
    @SerialName("files_admin") FILES_ADMIN("files_admin"),

    @SerialName("poll") POLL("poll"),
    @SerialName("poll_write") POLL_WRITE("poll_write"),
    @SerialName("poll_admin") POLL_ADMIN("poll_admin"),

    @SerialName("timetable") TIMETABLE("timetable"),
    @SerialName("timetable_write") TIMETABLE_WRITE("timetable_write"),
    @SerialName("timetable_admin") TIMETABLE_ADMIN("timetable_admin"),

    @SerialName("learning_plan") LEARNING_PLAN("learning_plan"),
    @SerialName("learning_plan_write") LEARNING_PLAN_WRITE("learning_plan_write"),
    @SerialName("learning_plan_admin") LEARNING_PLAN_ADMIN("learning_plan_admin"),

    @SerialName("ws_gen") WS_GEN("ws_gen"),
    @SerialName("ws_gen_write") WS_GEN_WRITE("ws_gen_write"),
    @SerialName("ws_gen_admin") WS_GEN_ADMIN("ws_gen_admin"),

    @SerialName("website") WEBSITE("website"),
    @SerialName("website_write") WEBSITE_WRITE("website_write"),
    @SerialName("website_admin") WEBSITE_ADMIN("website_admin"),

    @SerialName("showcase") SHOWCASE("showcase"),
    @SerialName("showcase_write") SHOWCASE_WRITE("showcase_write"),
    @SerialName("showcase_admin") SHOWCASE_ADMIN("showcase_admin"),

    @SerialName("blog") BLOG("blog"),
    @SerialName("blog_write") BLOG_WRITE("blog_write"),
    @SerialName("blog_admin") BLOG_ADMIN("blog_admin"),

    @SerialName("meta") META("meta"),
    @SerialName("meta_write") META_WRITE("meta_write"),
    @SerialName("meta_admin") META_ADMIN("meta_admin"),

    @SerialName("client") CLIENT("client"),

    @SerialName("self") SELF("self"),

    @SerialName("password") PASSWORD("password");

    companion object {
        fun getByName(name: String): Permission? {
            return values().firstOrNull { it.id == name }
        }
    }
}