package com.example.droidsoftthird.usecase

import com.example.droidsoftthird.model.domain_model.Place
import com.example.droidsoftthird.model.domain_model.PlaceDetail
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class MapUseCase  @Inject constructor(private val repository: BaseRepositoryImpl) {

    suspend fun searchIndividualPlace(query: String, viewPort: ViewPort): List<Place> =
        repository.searchIndividualPlace(query, viewPort) //特定の場所を検索する。

    suspend fun searchByText(query: String, centerPoint: LatLng, type: String, radius: Int): List<Place> =
        repository.searchByText(query, centerPoint, type, radius) //周辺の場所を検索する。

    suspend fun searchByPoi(centerPoint: LatLng, type: String, radius: Int): List<Place> =
        repository.searchByPoi(centerPoint, type, radius) //POIで検索する。

    suspend fun fetchPlaceDetail(placeId: String): PlaceDetail? =
        repository.fetchPlaceDetail(placeId)

    //suspend fun fetchCurrentLocation():  TODO 現在地を取得するようにする。
}