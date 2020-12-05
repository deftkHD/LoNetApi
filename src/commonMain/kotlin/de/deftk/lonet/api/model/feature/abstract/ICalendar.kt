package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.calendar.Appointment

interface ICalendar {

    fun getAppointments(): List<Appointment>
    fun addAppointment(title: String, description: String? = null, endDate: Long? = null, endDateIso: String? = null, location: String? = null, rrule: String? = null, startDate: Long? = null, startDateIso: String? = null): Appointment

}