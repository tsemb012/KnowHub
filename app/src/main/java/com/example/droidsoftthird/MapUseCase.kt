package com.example.droidsoftthird

import com.example.droidsoftthird.model.domain_model.Place
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class MapUseCase  @Inject constructor(private val repository: BaseRepositoryImpl) {
    suspend fun searchIndividualPlace(query: String, viewPort: ViewPort): List<Place> =
        repository.searchIndividualPlace(query, viewPort) //特定の場所を検索する。
    suspend fun searchByText(query: String, centerPoint: LatLng): List<Place> =
        repository.searchByText(query, centerPoint) //周辺の場所を検索する。
    //suspend fun fetchPOIs(type: String): List<Marker> = repository.fetchPOIs
    //suspend fun fetchCurrentLocation():  TODO 現在地を取得するようにする。
    //suspend fun fetchPlaceDetail: TODO 場所の詳細を取得する　→　PlaceDetailのデータクラスを取得するようにする。
}