package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.Task

interface ITaskList {

    fun getTasks(): List<Task>
    fun addTask(title: String, completed: Boolean? = null, description: String? = null, dueDate: Long? = null, startDate: Long? = null): Task

}