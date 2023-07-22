package com.example.droidsoftthird.model.domain_model

import android.os.Parcelable
import com.example.droidsoftthird.model.infra_model.json.request.PostPlaceJson
import kotlinx.android.parcel.Parcelize

data class YolpDetailPlace(
        val id: String,
        val name: String,
        val yomi: String,
        val category: String,
        val tel: String?,
        val url: String?,
        val location: Location,
        val formattedAddress: String?,
) {
        fun toEditedPlace(): EditedPlace? {
                return EditedPlace(
                        placeId = id,
                        name = name,
                        yomi = yomi,
                        category = category,
                        tel = tel,
                        url = url,
                        location = location,
                        formattedAddress = formattedAddress,
                        memo = null,
                )

        }
}

@Parcelize
data class EditedPlace(
        val placeId: String,
        val name: String,
        val yomi: String,
        val category: String,
        val location: Location,
        val formattedAddress: String?,
        val tel: String?,
        val url: String?,
        val memo: String?,
): Parcelable {
        fun toJson(): PostPlaceJson? {
                return PostPlaceJson(
                        placeId = placeId,
                        name = name,
                        yomi = yomi,
                        category = category,
                        latitude = location.lat,
                        longitude = location.lng,
                        address = formattedAddress ?: "",
                        tel = tel,
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