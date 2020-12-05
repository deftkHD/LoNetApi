package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.files.session.SessionFile

interface ISessionFileHolder {

    fun addSessionFile(name: String, data: ByteArray): SessionFile

}