package de.deftk.lonet.api.model.feature.abstract

import de.deftk.lonet.api.model.feature.WikiPage

interface IWiki {

    fun getWikiPage(name: String?): WikiPage?

}