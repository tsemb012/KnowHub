package com.example.droidsoftthird

import com.example.droidsoftthird.model.domain_model.Place
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject

class MapUseCase  @Inject constructor(private val repository: BaseRepositoryImpl) {
    suspend fun searchPlaces(query: String, viewPort: ViewPort): List<Place> =
        repository.searchPlaces(query, viewPort)
    //suspend fun fetchPOIs(type: String): List<Marker> = repository.fetchPOIs
    //suspend fun fetchCurrentLocation():  TODO 現在地を取得するようにする。
    //suspend fun fetchPlaceDetail: TODO 場所の詳細を取得する　→　PlaceDetailのデータクラスを取得するようにする。
}