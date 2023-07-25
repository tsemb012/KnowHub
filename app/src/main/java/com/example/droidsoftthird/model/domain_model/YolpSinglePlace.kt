package com.example.droidsoftthird.model.domain_model

import android.os.Parcelable
import com.example.droidsoftthird.model.infra_model.json.request.PostPlaceJson
import kotlinx.android.parcel.Parcelize
import java.util.UUID

sealed class YolpSinglePlace {
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
        val name: String,
        val yomi: String?,
        val category: String?,
        val tel: String?,
        val url: String?,
        val location: Location,
        override val address: String,
    ) : YolpSinglePlace() {
        fun toEditedPlace(): EditedPlace? {
            return EditedPlace(
                placeId = id,
                name = name,
                yomi = yomi,
                category = category,
                tel = tel,
                url = url,
                location = location,
                formattedAddress = address,
                memo = null,
            )
        }
    }

    data class ReverseGeocode (
        override val address: String,
        override val lat: Double,
        override val lng: Double,
    ) : YolpSinglePlace()
}

@Parcelize
data class EditedPlace(
    val placeId: String,
    val name: String,
    val yomi: String?,
    val category: String?,
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
