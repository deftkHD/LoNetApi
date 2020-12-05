package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.contact.Contact
import de.deftk.lonet.api.model.feature.contact.Gender

interface IContactHolder {

    fun getContacts(): List<Contact>
    fun addContact(categories: String? = null, firstName: String? = null, lastName: String? = null, homeStreet: String? = null, homeStreet2: String? = null, homePostalCode: String? = null, homeCity: String? = null, homeState: String? = null, homeCountry: String? = null, homeCoords: String? = null, homePhone: String? = null, homeFax: String? = null, mobilePhone: String? = null, birthday: String? = null, email: String? = null, gender: Gender? = null, hobby: String? = null, notes: String? = null, website: String? = null, company: String? = null, companyType: String? = null, jobTitle: String? = null): Contact

}