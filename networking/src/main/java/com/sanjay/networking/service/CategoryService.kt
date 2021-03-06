package com.sanjay.networking.service

import com.sanjay.networking.constraint.Filter
import com.sanjay.networking.constraint.Sort
import com.sanjay.networking.response.ApiResponse
import com.sanjay.networking.response.model.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Contract for Category-related REST API calls
 *
 * A Retrofit object will create the actual implementation of this Service
 */

interface CategoryService {
    @GET("/v0/categories")
    fun getCategories(@Query("page") page: Int? = 1,
                      @Query("count") count: Int? = Int.MAX_VALUE,
                      @Query("sort") sort: Sort?,
                      @Query("filter") filter: Filter?): Call<ApiResponse<List<Category>>>

    @GET("/v0/categories/{categoryId}")
    fun getCategory(@Path("categoryId") categoryId: Long): Call<ApiResponse<Category>>
}