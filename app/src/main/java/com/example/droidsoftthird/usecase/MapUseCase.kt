package com.example.droidsoftthird.usecase

import com.example.droidsoftthird.model.domain_model.Category
import com.example.droidsoftthird.model.domain_model.Place
import com.example.droidsoftthird.model.domain_model.PlaceDetail
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.example.droidsoftthird.model.domain_model.YolpSimplePlace
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class MapUseCase  @Inject constructor(private val repository: BaseRepositoryImpl) {

    suspend fun searchIndividualPlace(query: String, viewPort: ViewPort): List<Place> =
        repository.searchIndividualPlace(query, viewPort) //特定の場所を検索する。

    suspend fun searchByText(query: String, viewPort: ViewPort, centerPoint: LatLng,): List<YolpSimplePlace> =
        repository.yolpTextSearch(query, viewPort, centerPoint)

    suspend fun searchByPoi(viewPort: ViewPort, centerPoint: LatLng, category: Category): List<YolpSimplePlace> =
        repository.yolpCategorySearch(viewPort, centerPoint, category) //POIで検索する。

    suspend fun autoComplete(query: String, viewPort: ViewPort, centerPoint: LatLng): List<YolpSimplePlace> =
        repository.yolpAutoComplete(query, viewPort, centerPoint)

    suspend fun fetchPlaceDetail(placeId: String): PlaceDetail? =
        repository.fetchPlaceDetail(placeId)

    /*suspend fun fetchPlaceDetail(placeId: String): YolpDetailPlace? =
        repository.yolpDetailSearch(placeId) //場所の詳細を取得する。*/

    //suspend fun fetchCurrentLocation():  TODO 現在地を取得するようにする。
}