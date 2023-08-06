package com.tsemb.droidsoftthird.model.infra_model.json.request

import com.tsemb.droidsoftthird.model.domain_model.Area
import com.tsemb.droidsoftthird.model.domain_model.City
import com.tsemb.droidsoftthird.model.domain_model.Prefecture
import com.squareup.moshi.Json

/***
 * 基本方針として、モデルの１つは１つのテーブルに対応するようにする。
 * 理由：ネストされたモデルをストロングパラメーターで扱うのが難しいため
 * 例：　prefectureJson →　prefectureTable, cityJson →　cityTable*/

class PostUserDetailJson(
    @Json(name = "user_id")
    val userId: String,
    @Json(name = "user_name")
    val userName: String? = null,
    @Json(name = "user_image")
    val userImage: String,
    val comment:String,
    val gender: String,
    val birthday: String,
    @Json(name = "prefecture_code")
    val prefectureCode: Int,
    @Json(name = "city_code")
    val cityCode: Int,
)

class AreaJson(
    @Json(name = "prefecture")
    val prefecture: PrefectureJson?,
    @Json(name = "city")
        val city: CityJson? = null
) {
        fun toEntity() = Area(
                prefecture = prefecture?.toEntity(),
                city = city?.toEntity()
        )

}

class PrefectureJson(
        @Json(name = "prefecture_code")
        val prefectureCode: Int,
        val name: String,
        val spell: String,
        @Json(name = "capital_name")
        val capitalName: String,
        @Json(name = "capital_spell")
        val capitalSpell: String,
        @Json(name = "capital_latitude")
        val capitalLatitude: Double,
        @Json(name = "capital_longitude")
        val capitalLongitude: Double,
) {
        fun toEntity(): Prefecture =
                Prefecture(
                        prefectureCode = prefectureCode,
                        name = name,
                        spell = spell,
                        capital = Prefecture.Capital(
                                name = capitalName,
                                spell = capitalSpell,
                                latitude = capitalLatitude,
                                longitude = capitalLongitude
                        )
                )
}

class CityJson(
        @Json(name = "city_code")
        val cityCode: Int,
        val name: String,
        val spell: String,
        val latitude: Double,
        val longitude: Double,
) {
        fun toEntity(): City =
                City(
                        cityCode = cityCode,
                        name = name,
                        spell = spell,
                        latitude = latitude,
                        longitude = longitude
                )
}
