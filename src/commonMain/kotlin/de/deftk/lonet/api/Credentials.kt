package de.deftk.lonet.api

data class Credentials(val username: String, val password: String?, val token: String?) {

    companion object {
        fun fromPassword(username: String, password: String) = Credentials(username, password, null)
        fun fromToken(username: String, token: String) = Credentials(username, null, token)
    }

}