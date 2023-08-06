package com.tsemb.droidsoftthird.model.domain_model

import android.os.Parcelable
import com.tsemb.droidsoftthird.model.infra_model.json.request.PostPlaceJson
import kotlinx.android.parcel.Parcelize
import java.util.UUID

sealed class YolpSinglePlace {
    abstract fun toEditedPlace(memo: String, placeName: String?): EditedPlace

    open val id: String
        get() = when (this) {
            is DetailPlace -> id
            is ReverseGeocode -> UUID.randomUUID().toString()
        }

    abstract val address: String

    open val lat: Double
        get() = when (this) {
            is DetailPlace -> location.lat
            is ReverseGeocode -> lat
        }
    open val lng: Double
        get() = when (this) {
            is DetailPlace -> location.lng
            is ReverseGeocode -> lng
        }

    data class DetailPlace(
        override val id: String,
        override val address: String,
        val name: String,
        val location: Location,
        val yomi: String?,
        val category: String?,
        val tel: String?,
        val url: String?,
    ) : YolpSinglePlace() {
        override fun toEditedPlace(memo: String, placeName: String?): EditedPlace {
            return EditedPlace(
                placeId = id,
                name = name,
                location = location,
                yomi = yomi,
                category = category,
                formattedAddress = address,
                tel = tel,
                url = url,
                memo = memo,
            )
        }

    }

    data class ReverseGeocode (
        override val address: String,
        override val lat: Double,
        override val lng: Double,
    ) : YolpSinglePlace() {
        override fun toEditedPlace(memo: String, placeName: String?): EditedPlace {
            return EditedPlace(
                placeId = id,
                name = placeName ?: "",
                location = Location(lat, lng),
                formattedAddress = address,
                yomi = "",
                category = "",
                tel = "",
                url = "",
                memo = memo,
            )
        }
    }
}

@Parcelize
data class EditedPlace(
    val placeId: String,
    val name: String,
    val location: Location,
    val formattedAddress: String?,
    val yomi: String?,
    val category: String?,
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
