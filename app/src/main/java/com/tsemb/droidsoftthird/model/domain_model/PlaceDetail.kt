package com.tsemb.droidsoftthird.model.domain_model


data class AddressComponent(
        val longName: String,
        val shortName: String,
        val types: List<String>
)


//TODO PlaceとPlaceDetailで重複している部分をPlaceDetailに移行させる。
// Placeは実際には場所情報と名前だけあれば問題ない。