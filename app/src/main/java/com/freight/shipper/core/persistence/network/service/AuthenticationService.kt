package com.freight.shipper.core.persistence.network.service

import com.freight.shipper.core.persistence.network.response.ApiResponse
import com.freight.shipper.core.persistence.network.response.MasterConfig
import com.freight.shipper.core.persistence.network.response.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Contract for Authentication-related REST api calls
 *
 * A Retrofit object will create the actual implementation of this Service
 */

interface AuthenticationService {

    @FormUrlEncoded
    @POST("webservices?param=login&type=shipper")
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Call<ApiResponse<User>>

    @FormUrlEncoded
    @POST("webservices?param=shipperRegister&shipper_type=c")
    fun signupAsCompany(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("address") address: String,
        @Field("country_id") country: String,
        @Field("state") state: String,
        @Field("city") city: String,
        @Field("postcode") postcode: String,
        @Field("password") password: String,
        @Field("company_name") companyName: String,
        @Field("registration_no") regNumber: String
    ): Call<ApiResponse<User>>

    @FormUrlEncoded
    @POST("webservices?param=shipperRegister&shipper_type=i")
    fun signupIndividual(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("address") address: String,
        @Field("country_id") country: String,
        @Field("state") state: String,
        @Field("city") city: String,
        @Field("postcode") postcode: String,
        @Field("password") password: String
    ): Call<ApiResponse<User>>

    @FormUrlEncoded
    @POST("webservices")
    fun forgotPassword(
        @Field("email") email: String,
        @Field("param") param: String = "forgotPassword"
    ): Call<ApiResponse<User>>

    @POST("webservices?param=getMasterData")
    fun getMasterConfig(): Call<ApiResponse<MasterConfig>>
}