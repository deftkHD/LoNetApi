package de.deftk.lonet.api.model.abstract

enum class ManageableType(val id: Int) {
    REMOTE_USER(0),
    SELF_USER(1),
    BASE_USER(16),
    GROUP(19),
    UNKNOWN(-1)
    ;

    companion object {
        fun getById(id: Int): ManageableType {
            val type = values().firstOrNull { it.id == id } ?: UNKNOWN
            if (type == UNKNOWN && id != -1)
                println("unknown manageable type: $id")
            return type
        }
    }
}