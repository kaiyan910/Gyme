package com.architecture.kotlinmvvm.network.response

import com.squareup.moshi.Json

data class GymResponse(
        @Json(name = "District_en") val districtEN: String,
        @Json(name = "District_cn") val districtZH: String,
        @Json(name = "Name_en") val nameEN: String,
        @Json(name = "Name_cn") val nameZH: String,
        @Json(name = "Address_en") val addressEN: String,
        @Json(name = "Address_cn") val addressZH: String,
        @Json(name = "Size_en") val sizeEN: String,
        @Json(name = "Size_cn") val sizeZH: String,
        @Json(name = "Phone") val phone: String,
        @Json(name = "Longitude") val longitude: String,
        @Json(name = "Latitude") val latitude: String
)