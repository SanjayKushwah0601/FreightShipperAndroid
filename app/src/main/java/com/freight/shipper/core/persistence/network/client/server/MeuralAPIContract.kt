package com.freight.shipper.core.persistence.network.client.server

import com.freight.shipper.core.persistence.network.result.APIResult
import com.freight.shipper.model.Category
import com.freight.shipper.model.Login
import com.freight.shipper.model.Token
import com.freight.shipper.ui.authentication.signup.CompanySignup

abstract class MeuralAPIContract {

    companion object {
        private const val MAX_PAGE_SIZE = 10
    }

    // region - Auth
//    abstract suspend fun login(email: String, password: String): APIResult<Token>
    abstract suspend fun login(email: String, password: String): APIResult<Login>

    abstract suspend fun signupAsCompany(model: CompanySignup): APIResult<Login>

    abstract suspend fun register(
        firstName: String, lastName: String, email: String,
        password: String, confirmPassword: String, countryCode: String,
        isReceiveCommunications: Boolean, isSecurityToken: Boolean
    ): APIResult<Token>
    // endregion

    // region - Category
    abstract suspend fun getCategory(id: Long): APIResult<Category>
    // endregion
}