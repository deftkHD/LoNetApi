package de.deftk.lonet.api.model.feature.contact

import de.deftk.lonet.api.model.abstract.AbstractOperator
import de.deftk.lonet.api.model.abstract.IManageable
import de.deftk.lonet.api.request.OperatorApiRequest
import de.deftk.lonet.api.response.ResponseUtil
import de.deftk.lonet.api.utils.getApiDate
import de.deftk.lonet.api.utils.getManageable
import de.deftk.lonet.api.utils.getStringOrNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class Contact(
        val id: Int,
        uid: String?,
        categories: String?,
        firstName: String?,
        lastName: String?,
        homeStreet: String?,
        homeStreet2: String?,
        homePostalCode: String?,
        homeCity: String?,
        homeState: String?,
        homeCountry: String?,
        homeCoords: String?,
        homePhone: String?,
        homeFax: String?,
        mobilePhone: String?,
        birthday: String?,
        emailAddress: String?,
        gender: Gender?,
        hobbies: String?,
        notes: String?,
        website: String?,
        company: String?,
        companyType: String?,
        jobTitle: String?,
        creationDate: Long,
        creationMember: IManageable,
        modificationDate: Long,
        modificationMember: IManageable,
        val operator: AbstractOperator
) {

    companion object {
        fun fromJson(jsonObject: JsonObject, operator: AbstractOperator): Contact {
            val createdObject = jsonObject["created"]!!.jsonObject
            val modifiedObject = jsonObject["modified"]!!.jsonObject
            val contact = Contact(
                    jsonObject["id"]!!.jsonPrimitive.int,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    createdObject.getApiDate("date"),
                    createdObject.getManageable("user", operator),
                    modifiedObject.getApiDate("date"),
                    modifiedObject.getManageable("user", operator),
                    operator
            )
            contact.readFrom(jsonObject)
            return contact
        }
    }

    var uid = uid
        private set
    var categories = categories
        private set
    var firstName = firstName
        private set
    var lastName = lastName
        private set
    var homeStreet = homeStreet
        private set
    var homeStreet2 = homeStreet2
        private set
    var homePostalCode = homePostalCode
        private set
    var homeCity = homeCity
        private set
    var homeState = homeState
        private set
    var homeCountry = homeCountry
        private set
    var homeCoords = homeCoords
        private set
    var homePhone = homePhone
        private set
    var homeFax = homeFax
        private set
    var mobilePhone = mobilePhone
        private set
    var birthday = birthday
        private set
    var emailAddress = emailAddress
        private set
    var gender = gender
        private set
    var hobbies = hobbies
        private set
    var notes = notes
        private set
    var website = website
        private set
    var company = company
        private set
    var companyType = companyType
        private set
    var jobTitle = jobTitle
        private set
    var creationDate = creationDate
        private set
    var creationMember = creationMember
        private set
    var modificationDate = modificationDate
        private set
    var modificationMember = modificationMember
        private set

    fun edit(categories: String? = null, firstName: String? = null, lastName: String? = null, homeStreet: String? = null, homeStreet2: String? = null, homePostalCode: String? = null, homeCity: String? = null, homeState: String? = null, homeCountry: String? = null, homeCoords: String? = null, homePhone: String? = null, homeFax: String? = null, mobilePhone: String? = null, birthday: String? = null, email: String? = null, gender: Gender? = null, hobby: String? = null, notes: String? = null, website: String? = null, company: String? = null, companyType: String? = null, jobTitle: String? = null) {
        val request = OperatorApiRequest(operator)
        val id = request.addSetContactRequest(id.toString(), categories, firstName, lastName, homeStreet, homeStreet2, homePostalCode, homeCity, homeState, homeCountry, homeCoords, homePhone, homeFax, mobilePhone, birthday, email, gender, hobby, notes, website, company, companyType, jobTitle)[1]
        val response = request.fireRequest()
        val subResponse = ResponseUtil.getSubResponseResult(response.toJson(), id)
        readFrom(subResponse["entry"]!!.jsonObject)
    }

    private fun readFrom(jsonObject: JsonObject) {
        val createdObject = jsonObject["created"]!!.jsonObject
        val modifiedObject = jsonObject["modified"]!!.jsonObject

        uid = jsonObject.getStringOrNull("uid")
        categories = jsonObject.getStringOrNull("categories")
        firstName = jsonObject.getStringOrNull("firstname")
        lastName = jsonObject.getStringOrNull("lastname")
        homeStreet = jsonObject.getStringOrNull("homestreet")
        homeStreet2 = jsonObject.getStringOrNull("homestreet2")
        homePostalCode = jsonObject.getStringOrNull("homepostalcode")
        homeCity = jsonObject.getStringOrNull("homecity")
        homeState = jsonObject.getStringOrNull("homestate")
        homeCountry = jsonObject.getStringOrNull("homecountry")
        homeCoords = jsonObject.getStringOrNull("homecoords")
        homePhone = jsonObject.getStringOrNull("homephone")
        homeFax = jsonObject.getStringOrNull("homefax")
        mobilePhone = jsonObject.getStringOrNull("mobilephone")
        birthday = jsonObject.getStringOrNull("birthday")
        emailAddress = jsonObject.getStringOrNull("emailaddress")
        gender = Gender.getById(jsonObject.getStringOrNull("gender")?.toInt())
        hobbies = jsonObject.getStringOrNull("hobby")
        notes = jsonObject.getStringOrNull("notes")
        website = jsonObject.getStringOrNull("webpage")
        company = jsonObject.getStringOrNull("company")
        companyType = jsonObject.getStringOrNull("companytype")
        jobTitle = jsonObject.getStringOrNull("jobtitle")
        creationDate = createdObject.getApiDate("date")
        creationMember = createdObject.getManageable("user", operator)
        modificationDate = modifiedObject.getApiDate("date")
        modificationMember = modifiedObject.getManageable("user", operator)
    }

    fun delete() {
        val request = OperatorApiRequest(operator)
        request.addDeleteContactRequest(id.toString())
        val response = request.fireRequest()
        ResponseUtil.checkSuccess(response.toJson())
    }

}