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

    @GET("users/{user_id}") //TODO
    suspend fun fetchUser(
            @Path("user_id") userId: String
    ): GetUserDetailJson

    @GET("users/{user_id}/groups")
    suspend fun fetchUserJoinedGroups(
            @Path("user_id") userId: String
    ): Response<List<GetGroupJson>>

    @GET("users/{user_id}/groups/ids")
    suspend fun fetchUserJoinedGroupIds(
        @Path("user_id") userId: String
    ): List<String>

    @GET("users/{user_id}/groups/simple")
    suspend fun fetchUserJoinedSimpleGroups(
        @Path("user_id") userId: String
    ): List<GetSimpleGroupJson>

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
    suspend fun fetchGroup(@Path("id") groupId: String): Response<GetGroupJson>

    @GET("groups/{id}/chat")
    suspend fun fetchChatGroup(@Path("id") groupId: String): Response<GetChatGroupJson>

    @GET("groups")
    suspend fun fetchGroups(
        @Query("page") page: Int? = null,
        @Query("user_id") userId: String? = null,
        @Query("area_code") areaCode: Int? = null,
        @Query("area_category") areaCategory: String? = null,
        @Query("group_type") groupType: String? = null,
        @Query("facility_environments[]") facilityEnvironments: List<String> = listOf(),
        @Query("frequency_basis") frequency_bases: String? = null,
        @Query("style") style: String? = null,
        @Query("allow_max_number_group_show") allowMaxNumberGroupShow: Boolean = true,
    ): List<GetGroupJson>

    @GET("groups/locations/count")
    suspend fun fetchGroupCountByArea(
        @Query("user_id") userId: String,
        @Query("allow_max_number_group_show") allowMaxNumberGroupShow: Boolean = false,
    ): List<GetGroupCountByAreaJson>

    @PATCH("groups/{id}/participate")
    suspend fun putUserToGroup(
            @Path("id") groupId: String,
            @Body request: PutUserToGroupJson
    ): Response<MessageResponse>

    @PATCH("groups/{id}/leave")
    suspend fun removeUserFromGroup(
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
            @Query("center_lng") centerLng: Double,
            @Query("radius") radius: String,
    ): Response<List<GetPlaceJson>>

    @GET("maps/place_detail")
    suspend fun getPlaceDetail(
            @Query("place_id") placeId: String,
            @Query("language") language: String,
    ): Response<GetPlaceDetailJson>

    @GET("maps/yolp_text_search")
    suspend fun getYolpTextSearch(
        @Query("query") query: String,
        @Query("center_lat") centerLat: Double,
        @Query("center_lng") centerLng: Double,
        @Query("north_lat") northLat: Double,
        @Query("east_lng") eastLng: Double,
        @Query("south_lat") southLat: Double,
        @Query("west_lng") westLng: Double,
    ): Response<List<GetYolpSimplePlaceJson>>

    @GET("maps/yolp_auto_complete")
    suspend fun getYolpAutoComplete(
        @Query("query") query: String,
        @Query("center_lat") centerLat: Double,
        @Query("center_lng") centerLng: Double,
        @Query("north_lat") northLat: Double,
        @Query("east_lng") eastLng: Double,
        @Query("south_lat") southLat: Double,
        @Query("west_lng") westLng: Double,
    ): Response<List<GetYolpSimplePlaceJson>>

    @GET("maps/yolp_category_search")
    suspend fun getYolpCategorySearch(
        @Query("query") query: String,
        @Query("category") category: String,
        @Query("center_lat") centerLat: Double,
        @Query("center_lng") centerLng: Double,
        @Query("north_lat") northLat: Double,
        @Query("east_lng") eastLng: Double,
        @Query("south_lat") southLat: Double,
        @Query("west_lng") westLng: Double,
    ): Response<List<GetYolpSimplePlaceJson>>

    @GET("maps/yolp_detail_search")
    suspend fun getYolpDetailSearch(
        @Query("place_id") placeId: String,
    ): Response<GetYolpDetailPlaceJson>

    @GET("maps/yolp_reverse_geo_coder")
    suspend fun getYolpReverseGeocode(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
    ): Response<GetYolpReverseGeocodeJson>

    @GET("events") //TODO
    suspend fun getEvents(
            @Query("user_id") userId: String
    ) : List<GetItemEventJson>

    @GET("events/{id}")
    suspend fun getEventDetail(
            @Path("id") eventId: String,
            @Query("user_id") userId: String
    ) : GetEventDetailJson

    @POST("events")
    suspend fun postEvent(
            @Body request: PostEventJson
    ): MessageResponse

    @PATCH("events/{id}/register")
    suspend fun putEvent(
            @Path("id") eventId: String,
            @Body request: PutUserToEventJson
    ): MessageResponse

    @PATCH("events/{id}/unregister")
    suspend fun putEvent(
        @Path("id") eventId: String,
        @Body request: RemoveUserFromEventJson
    ): MessageResponse

    @DELETE("events/{id}")
    suspend fun deleteEvent(
        @Path("id") eventId: String
    ): MessageResponse
}
