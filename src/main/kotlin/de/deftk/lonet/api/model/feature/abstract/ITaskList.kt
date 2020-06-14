package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.Task

interface ITaskList {

    fun getTasks(overwriteCache: Boolean = false): List<Task>

}