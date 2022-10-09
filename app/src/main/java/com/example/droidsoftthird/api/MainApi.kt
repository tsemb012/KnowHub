package com.example.droidsoftthird.api

import com.example.droidsoftthird.model.json.UserJson
import com.example.droidsoftthird.model.request.PostGroup
import com.example.droidsoftthird.model.request.PostSignUp
import com.example.droidsoftthird.model.request.PutUserToGroup
import com.example.droidsoftthird.model.response.GetGroup
import com.example.droidsoftthird.model.response.GetGroupDetail
import com.example.droidsoftthird.model.response.GetUserDetail
import com.example.droidsoftthird.model.response.MessageResponse
import retrofit2.Response
import retrofit2.http.*

interface MainApi {

    @GET("signup")
    suspend fun postTokenId(
            @HeaderMap headers: Map<String, String>,
    ) : Response<Any>

    @GET("users/{user_id}")
    fun fetchUser(@Path("user_id") userId: String): GetUserDetail


    @POST("users")
    fun postNewUser(@Body request: PostSignUp.Request): Response<UserJson>

    @POST("groups")
    suspend fun createGroup(@Body request: PostGroup): Response<MessageResponse>

    @GET("groups/{id}")
    suspend fun fetchGroup(@Path("id") groupId: String): Response<GetGroupDetail>

    @GET("groups")
    suspend fun fetchGroups(
            @Query("page") page: Int? = null,
            @Query("user_id") userId: String? = null
    ): Response<List<GetGroup>>

    @PATCH("groups/{id}/participate")
    suspend fun putUserToGroup(
            @Path("id") groupId: String,
            @Body request: PutUserToGroup
    ): Response<MessageResponse>
}
