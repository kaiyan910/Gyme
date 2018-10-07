package com.architecture.kotlinmvvm.network.response

import com.squareup.moshi.Json

data class EquipmentResponse(
        @Json(name = "Name_en") val nameEN: String,
        @Json(name = "Name_cn") val nameZH: String,
        @Json(name = "Equipment_en") val equipmentEN: String,
        @Json(name = "Equipment_cn") val equipmentZH: String,
        @Json(name = "No_of_set") val numberOfSeat: Int,
        @Json(name = "Shared_with_persons_with_disabilities") val shared: String,
        @Json(name = "Room_No") val roomNumber: Int
)