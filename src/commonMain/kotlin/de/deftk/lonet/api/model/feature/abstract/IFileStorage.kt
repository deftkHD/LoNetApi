package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.Quota
import de.deftk.lonet.api.model.feature.files.FileStorageSettings
import de.deftk.lonet.api.model.feature.files.OnlineFile

interface IFileStorage: IFilePrimitive {

    fun getFileStorageState(): Pair<FileStorageSettings, Quota>
    fun getTrash(limit: Int? = null): List<OnlineFile>

}