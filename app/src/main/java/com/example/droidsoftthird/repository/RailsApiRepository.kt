package com.example.droidsoftthird.repository

import com.example.droidsoftthird.model.domain_model.*
import com.google.android.gms.maps.model.LatLng

interface RailsApiRepository {

    suspend fun certifyAndRegister(tokenID: String)

    suspend fun createGroup(group: ApiGroup): String?
    suspend fun fetchGroupDetail(groupId: String) : ApiGroupDetail
    suspend fun fetchGroups(page: Int) : List<ApiGroup>
    suspend fun fetchJoinedGroups() : List<ApiGroup>
    suspend fun userJoinGroup(groupId: String): String?

    suspend fun fetchUser(): UserDetail
    suspend fun updateUserDetail(userDetail: UserDetail): String?
    suspend fun createUser(userDetail: UserDetail): String?
    //suspend fun postNewUser(singup: SignUpJson): User?

    suspend fun searchIndividualPlace(query: String, viewPoint: ViewPort): List<Place>
    suspend fun searchByText(query: String, centerPoint: LatLng, type: String, radius: Int): List<Place>
    suspend fun searchByPoi(centerPoint: LatLng, type: String, radius: Int): List<Place>
    suspend fun fetchPlaceDetail(placeId: String): PlaceDetail?

    suspend fun createEvent(event: CreateEvent): String?
    suspend fun fetchEvents(): List<ItemEvent>
}