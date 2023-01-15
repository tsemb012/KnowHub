package com.example.droidsoftthird.api

import com.example.droidsoftthird.model.json.PlaceJson
import com.example.droidsoftthird.model.json.UserJson
import com.example.droidsoftthird.model.json.ViewPortJson
import com.example.droidsoftthird.model.request.PostGroup
import com.example.droidsoftthird.model.request.PostSignUp
import com.example.droidsoftthird.model.request.PostUserDetailJson
import com.example.droidsoftthird.model.request.PutUserToGroup
import com.example.droidsoftthird.model.response.GetGroup
import com.example.droidsoftthird.model.response.GetGroupDetail
import com.example.droidsoftthird.model.response.GetUserDetailJson
import com.example.droidsoftthird.model.response.MessageResponse
import retrofit2.Response
import retrofit2.http.*

interface MainApi {

    @GET("signup")
    suspend fun postTokenId(
            @HeaderMap headers: Map<String, String>,
            @Query("user_id") userId: String,//TODO ここをpathに書き換える必要がある。
    ) : Response<Any>

    @GET("users/{user_id}")
    suspend fun fetchUser(
            @Path("user_id") userId: String
    ): GetUserDetailJson

    @PATCH("users/{user_id}")
    suspend fun putUserDetail(
            @Path("user_id") userId: String,
            @Body user: PostUserDetailJson
    ): MessageResponse


    fun postNewUser(@Body request: PostSignUp.Request): Response<UserJson>

    @POST("users")
    fun postUser(userId: String, toJson: PostUserDetailJson): MessageResponse


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

    @GET("maps/search_query")
    suspend fun getPlaces(
            @Query("input") query: String,
            @Query("viewport") viewPortJson: ViewPortJson,
            @Query("language") language: String,
    ): Response<List<PlaceJson>>
}
