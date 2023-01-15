package com.example.droidsoftthird.model.domain_model

data class Place (
        val placeId: String,
        val name: String,
        val type: String,
        val location: Location,
        val viewPort: ViewPort,
        val formattedAddress: String,
        //val photos: List<Photo>
)
