package com.example.droidsoftthird.model.domain_model

import android.graphics.Color
import com.google.android.libraries.places.api.model.Place.BusinessStatus
import com.google.android.libraries.places.api.model.PlusCode

data class PlaceDetail(
        val placeId: String,
        val name: String,
        val types: List<String>,
        val location: Location,
        val viewPort: ViewPort,
        val formattedAddress: String?,
        val plusCode: PlusCode,
        val photos: List<LocationPhoto>?,
        val color: String?,
        val url: String?,
        val addressComponents: List<AddressComponent>,
) {
    data class AddressComponent(
            val longName: String,
            val shortName: String,
            val types: List<String>
    )
}

//TODO PlaceとPlaceDetailで重複している部分をPlaceDetailに移行させる。
// Placeは実際には場所情報と名前だけあれば問題ない。