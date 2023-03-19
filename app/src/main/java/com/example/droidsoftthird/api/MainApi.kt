package com.example.droidsoftthird.api

import com.example.droidsoftthird.model.infra_model.json.UserJson
import com.example.droidsoftthird.model.infra_model.json.request.*
import com.example.droidsoftthird.model.infra_model.json.response.*
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


    fun postNewUser(@Body request: PostSignUpJson.Request): Response<UserJson>

    @POST("users")
    fun postUser(userId: String, toJson: PostUserDetailJson): MessageResponse


    @POST("groups")
    suspend fun createGroup(@Body request: PostGroupJson): Response<MessageResponse>

    @GET("groups/{id}")
    suspend fun fetchGroup(@Path("id") groupId: String): Response<GetGroupDetailJson>

    @GET("groups")
    suspend fun fetchGroups(
            @Query("page") page: Int? = null,
            @Query("user_id") userId: String? = null
    ): Response<List<GetGroupJson>>

    @PATCH("groups/{id}/participate")
    suspend fun putUserToGroup(
            @Path("id") groupId: String,
            @Body request: PutUserToGroupJson
    ): Response<MessageResponse>

    @GET("maps/search_individual")
    suspend fun getIndividualPlace(
            @Query("input") query: String,
            @Query("language") language: String,
            @Query("north_lat") northLat: Double,
            @Query("east_lng") eastLng: Double,
            @Query("south_lat") southLat: Double,
            @Query("west_lng") westLng: Double,
    ): Response<List<GetPlaceJson>>

    @GET("maps/search_by_text")
    suspend fun getPlacesByText(
            @Query("input") query: String,
            @Query("type") type: String,
            @Query("language") language: String,
            @Query("region") region: String,
            @Query("center_lat") centerLat: Double,
            @Query("center_lng")centerLng: Double,
            @Query("radius") radius: String,
    ): Response<List<GetPlaceJson>>

    @GET("maps/search_nearby")
    suspend fun getPlacesByPoi(
            @Query("type") type: String,
            @Query("language") language: String,
            @Query("center_lat") centerLat: Double,
            @Query("center_lng")centerLng: Double,
            @Query("radius") radius: String,
    ): Response<List<GetPlaceJson>>

    @GET("maps/place_detail")
    suspend fun getPlaceDetail(
            @Query("place_id") placeId: String,
            @Query("language") language: String,
    ): Response<GetPlaceDetailJson>

    @GET("events")
    suspend fun getEvents(
            @Query("user_id") userId: String
    ) : List<GetItemEventJson>

    @GET("events/{id}")
    suspend fun getEventDetail(
            @Path("id") eventId: String
    ) : GetEventDetailJson

    @POST("events")
    suspend fun postEvent(
            @Body request: PostEventJson
    ): MessageResponse
}
