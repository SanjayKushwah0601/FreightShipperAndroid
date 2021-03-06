package com.freight.shipper.core.persistence.network.result

import com.freight.shipper.R
import com.freight.shipper.core.persistence.network.response.ApiResponse
import com.freight.shipper.utils.StringUtil.getString

//    @SerializedName("data")
//    var data: T,
//
//    @SerializedName("isPaginated")
//    private var isPaginated: Boolean?,
//
//    @SerializedName("isLast")
//    private var isLast: Boolean?,
//
//    @SerializedName("count")
//    var itemCount: Int? = 0
sealed class APIResult<T> {
    class Failure<T>(val details: APIError) : APIResult<T>()
    class Success<T>(val response: ApiResponse<T>) : APIResult<T>() {
        constructor(value: T) : this(
            ApiResponse(
                value, false, "", null, null, null
            )
        )
    }

    val value: ApiResponse<T>?
        get() = (this as? Success)?.response

    val error: APIError?
        get() = (this as? Failure)?.details

    fun hasNextPage(): Boolean {
        return when (this) {
            is Success -> this.response.hasNextPage()
            is Failure -> false
        }

    }

    fun <U> map(apply: (T) -> U): APIResult<U> {
        return when (this) {
            is Success -> {
                val data = this.response.data
                var transformed = apply(data)
                Success(
                    ApiResponse(
                        transformed,
                        this.response.isSuccessful(),
                        this.response.getMessage(),
                        this.response.hasNextPage(),
                        !this.response.hasNextPage(),
                        this.response.itemCount
                    )
                )
            }
            is Failure -> {
                Failure(this.details)
            }
        }
    }

    fun withFailure(failure: (APIError) -> Unit): APIResult<T> {
        if (this is Failure) {
            failure(this.details)
        }
        return this
    }
}

sealed class APIErrorType {
    object General : APIErrorType()
    object FalseAPIResponse : APIErrorType()
    object CallNotTried : APIErrorType()
    object Subscription : APIErrorType()
    object Purchase : APIErrorType()
    object SignUpError : APIErrorType()
    class Network(val throwable: Throwable) : APIErrorType()
}

data class APIError(
    val errors: Map<String, String>,
    val type: APIErrorType,
    val code: Int,
    val thrownCause: Throwable?,
    override val message: String?
) : Exception(message ?: thrownCause?.message) {
    companion object {
        val notTried = APIError(hashMapOf(), APIErrorType.CallNotTried, -1, null, getString(R.string.not_implemented))
        val notImplemented = APIError(hashMapOf(), APIErrorType.General, -1, null, getString(R.string.not_implemented))
        val default = APIError(hashMapOf(), APIErrorType.General, -1, null, getString(R.string.default_api_error))
    }
}