package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.Quota
import de.deftk.lonet.api.model.feature.mailbox.EmailFolder
import de.deftk.lonet.api.model.feature.mailbox.EmailSignature

interface IMailbox {

    //TODO check what users and what groups are able to do

    fun getEmailStatus(): Pair<Quota, Int>
    fun getEmailQuota(): Quota
    fun getUnreadEmailCount(): Int

    fun getEmailFolders(): List<EmailFolder>
    fun addEmailFolder(name: String)

    fun sendEmail(to: String, subject: String, plainBody: String, text: String? = null, bcc: String? = null, cc: String? = null)

    fun getEmailSignature(): EmailSignature

}