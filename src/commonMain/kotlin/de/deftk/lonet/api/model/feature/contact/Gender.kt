package de.deftk.lonet.api.model.feature.contact

enum class Gender(val id: Int) {
    MALE(1),
    FEMALE(2),
    OTHER(3);

    companion object {
        fun getById(id: Int?): Gender? {
            if (id == null) return null
            val gender = values().firstOrNull { it.id == id }
            if (gender == null && id != -1)
                println("Unknown gender: $id")
            return gender
        }
    }
}