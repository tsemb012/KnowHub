package com.example.droidsoftthird.model.domain_model

import android.os.Parcelable
import com.example.droidsoftthird.model.infra_model.json.request.EditedPlaceJson
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
       fun toEditedPlace() = EditedPlace(
               placeId = placeId,
               name = name,
               placeType =  types.first(),
               location = location,
               formattedAddress = formattedAddress,
               plusCode = plusCode,
               url = url,
               memo = null,
       )
}

@Parcelize
data class EditedPlace(
        val placeId: String,
        val name: String,
        val placeType: String,
        val location: Location,
        val formattedAddress: String?,
        val plusCode: PlusCode,
        val url: String?,
        val memo: String?,
): Parcelable {
        fun toJson(): EditedPlaceJson {
                return EditedPlaceJson(
                        placeId = placeId,
                        name = name,
                        placeType = placeType,
                        latitude = location.lat,
                        longitude = location.lng,
                        address = formattedAddress ?: "",
                        globalCode = plusCode.globalCode,
                        compoundCode = plusCode.compoundCode,
                        url = url,
                        memo = memo,
                )
        }
}

data class AddressComponent(
        val longName: String,
        val shortName: String,
        val types: List<String>
)


//TODO PlaceとPlaceDetailで重複している部分をPlaceDetailに移行させる。
// Placeは実際には場所情報と名前だけあれば問題ない。