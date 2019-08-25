package com.freight.shipper.core.persistence.network.service

import com.freight.shipper.core.persistence.network.response.ApiResponse
import com.freight.shipper.model.ActiveLoad
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * @CreatedBy Sanjay Kushwah on 8/15/2019.
 * sanjaykushwah0601@gmail.com
 */
interface LoadService {
    @FormUrlEncoded
    @POST("webservices")
    fun getLoad(
        @Field("pick_date") pick_date: String?,
        @Field("param") param: String = "getLoad"
    ): Call<ApiResponse<List<ActiveLoad>>>
}