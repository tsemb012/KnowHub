package com.tsemb.droidsoftthird.repository

import androidx.paging.PagingData
import com.google.android.gms.maps.model.LatLng
import com.tsemb.droidsoftthird.model.domain_model.ApiGroup
import com.tsemb.droidsoftthird.model.domain_model.Category
import com.tsemb.droidsoftthird.model.domain_model.ChatGroup
import com.tsemb.droidsoftthird.model.domain_model.CreateEvent
import com.tsemb.droidsoftthird.model.domain_model.EditedGroup
import com.tsemb.droidsoftthird.model.domain_model.EventDetail
import com.tsemb.droidsoftthird.model.domain_model.EventItem
import com.tsemb.droidsoftthird.model.domain_model.GroupCountByArea
import com.tsemb.droidsoftthird.model.domain_model.SimpleGroup
import com.tsemb.droidsoftthird.model.domain_model.UserDetail
import com.tsemb.droidsoftthird.model.domain_model.ViewPort
import com.tsemb.droidsoftthird.model.domain_model.YolpSimplePlace
import com.tsemb.droidsoftthird.model.domain_model.YolpSinglePlace
import kotlinx.coroutines.flow.Flow

interface RailsApiRepository {

    suspend fun certifyAndRegister(tokenID: String)

    suspend fun createGroup(group: EditedGroup): String?
    suspend fun fetchGroupDetail(groupId: String) : ApiGroup
    suspend fun fetchGroups(groupFilterCondition: ApiGroup.FilterCondition) : Flow<PagingData<ApiGroup>>
    suspend fun fetchJoinedGroups() : List<ApiGroup>
    suspend fun userJoinGroup(groupId: String): String?
    suspend fun userLeaveGroup(groupId: String): String?
    suspend fun fetchGroupCountByArea(): List<GroupCountByArea>
    suspend fun fetchUser(): UserDetail
    suspend fun updateUserDetail(userDetail: UserDetail): String?
    suspend fun createUser(userDetail: UserDetail): String?
    //suspend fun postNewUser(singup: SignUpJson): User?
    suspend fun createEvent(event: CreateEvent): String?
    suspend fun fetchEvents(): List<EventItem>
    suspend fun fetchEventDetail(eventId: String): EventDetail
    suspend fun registerEvent(eventId: String): String?
    suspend fun unregisterEvent(eventId: String): String?
    suspend fun deleteEvent(eventId: String): String?
    suspend fun fetchUserJoinedGroupIds(): List<String>

    suspend fun fetchUserJoinedSimpleGroups(): List<SimpleGroup>
    suspend fun fetchChatGroup(groupId: String): ChatGroup

    suspend fun yolpTextSearch(query: String, viewPort: ViewPort, centerPoint: LatLng, ): List<YolpSimplePlace>
    suspend fun yolpAutoComplete(query: String, viewPort: ViewPort, centerPoint: LatLng): List<YolpSimplePlace>
    suspend fun yolpCategorySearch(viewPort: ViewPort, centerPoint: LatLng, category: Category): List<YolpSimplePlace>
    suspend fun yolpDetailSearch(placeId: String): YolpSinglePlace.DetailPlace?
    suspend fun yolpReverseGeocode(lat: Double, lon: Double): YolpSinglePlace.ReverseGeocode?
    suspend fun deleteUser(): String?
    suspend fun checkUserRegistered(): Boolean?
}