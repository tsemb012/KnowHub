package com.example.droidsoftthird.usecase

import com.example.droidsoftthird.model.domain_model.Category
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.example.droidsoftthird.model.domain_model.YolpDetailPlace
import com.example.droidsoftthird.model.domain_model.YolpReverseGeocode
import com.example.droidsoftthird.model.domain_model.YolpSimplePlace
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class MapUseCase  @Inject constructor(private val repository: BaseRepositoryImpl) {


    suspend fun searchByText(query: String, viewPort: ViewPort, centerPoint: LatLng,): List<YolpSimplePlace> =
        repository.yolpTextSearch(query, viewPort, centerPoint)

    suspend fun searchByCategory(viewPort: ViewPort, centerPoint: LatLng, category: Category): List<YolpSimplePlace> =
        repository.yolpCategorySearch(viewPort, centerPoint, category)

    suspend fun autoComplete(query: String, viewPort: ViewPort, centerPoint: LatLng): List<YolpSimplePlace> =
        repository.yolpAutoComplete(query, viewPort, centerPoint)

    suspend fun fetchPlaceDetail(placeId: String): YolpDetailPlace? =
        repository.yolpDetailSearch(placeId)
    suspend fun reverseGeocode(latLng: LatLng): YolpReverseGeocode? =
        repository.yolpReverseGeocode(latLng.latitude, latLng.longitude)

    //suspend fun fetchCurrentLocation():  TODO 現在地を取得するようにする。
}