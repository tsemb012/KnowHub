package com.example.droidsoftthird.model.domain_model

import android.os.Parcelable
import com.google.android.libraries.places.api.model.PlusCode
import kotlinx.android.parcel.Parcelize

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
       fun toEditedPlaceDetail() = EditedPlaceDetail(
               placeId = placeId,
               name = name,
               type =  types.first(),
               formattedAddress = formattedAddress,
               url = url,
               memo = null,
       )
}

@Parcelize
data class EditedPlaceDetail(
        val placeId: String,
        val name: String,
        val type: String,
        val formattedAddress: String?,
        val url: String?,
        val memo: String?,
): Parcelable

data class AddressComponent(
        val longName: String,
        val shortName: String,
        val types: List<String>
)


//TODO PlaceとPlaceDetailで重複している部分をPlaceDetailに移行させる。
// Placeは実際には場所情報と名前だけあれば問題ない。