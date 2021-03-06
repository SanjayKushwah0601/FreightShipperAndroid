package com.sanjay.networking.client.server

import com.sanjay.networking.response.model.Category
import com.sanjay.networking.response.model.Login
import com.sanjay.networking.response.model.Token
import com.sanjay.networking.result.APIResult

abstract class MeuralAPIContract {

    companion object {
        private const val MAX_PAGE_SIZE = 10
    }

    // region - Auth
//    abstract suspend fun login(email: String, password: String): APIResult<Token>
    abstract suspend fun login(email: String, password: String): APIResult<Login>

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