package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.files.OnlineFile
import de.deftk.lonet.api.model.feature.files.filters.FileFilter
import de.deftk.lonet.api.model.feature.files.session.SessionFile

interface IFilePrimitive {

    fun getName(): String
    fun getFiles(filter: FileFilter? = null): List<OnlineFile>
    fun addFile(name: String, data: ByteArray, description: String? = null): OnlineFile
    fun addSparseFile(name: String, size: Int, description: String? = null): OnlineFile
    fun importSessionFile(sessionFile: SessionFile, createCopy: Boolean? = null, description: String? = null): OnlineFile
    fun addFolder(name: String, description: String? = null): OnlineFile

    fun setReadable(readable: Boolean)
    fun setWritable(writable: Boolean)

}