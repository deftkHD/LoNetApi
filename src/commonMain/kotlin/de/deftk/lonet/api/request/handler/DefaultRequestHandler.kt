package de.deftk.lonet.api.request.handler

import de.deftk.lonet.api.model.abstract.IContext
import de.deftk.lonet.api.request.ApiRequest
import de.deftk.lonet.api.response.ApiResponse

class DefaultRequestHandler : AbstractRequestHandler() {

    override fun performRequest(request: ApiRequest, context: IContext): ApiResponse {
        return performJsonApiRequestIntern(request, context)
    }

}