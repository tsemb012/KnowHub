package com.example.droidsoftthird.model.domain_model

import android.os.Parcelable
import com.example.droidsoftthird.model.infra_model.json.request.PostPlaceJson
import kotlinx.android.parcel.Parcelize



data class AddressComponent(
        val longName: String,
        val shortName: String,
        val types: List<String>
)


//TODO PlaceとPlaceDetailで重複している部分をPlaceDetailに移行させる。
// Placeは実際には場所情報と名前だけあれば問題ない。