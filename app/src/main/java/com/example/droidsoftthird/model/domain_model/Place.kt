package com.example.droidsoftthird.model.domain_model

import com.google.android.libraries.places.api.model.PlusCode

data class Place (
        val placeId: String,
        val name: String,
        val types: List<String>,
        val location: Location,
        val viewPort: ViewPort,
        val formattedAddress: String,
        val plusCode: PlusCode,
        val photos: List<LocationPhoto>?
)

data class LocationPhoto (
        val height: Int,
        val width: Int,
        val photoReference: String,
        val htmlAttributions: List<String>,
)
