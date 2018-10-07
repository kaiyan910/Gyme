package com.architecture.kotlinmvvm.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.architecture.kotlinmvvm.model.Gym

@Entity(tableName = "gym")
data class GymEntity(

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,
        @ColumnInfo(name = "name_zh")
        val nameZH: String,
        @ColumnInfo(name = "name_en")
        val nameEN: String,
        @ColumnInfo(name = "district_zh")
        val districtZH: String,
        @ColumnInfo(name = "district_eh")
        val districtEN: String,
        @ColumnInfo(name = "area")
        val area: String,
        @ColumnInfo(name = "address_zh")
        val addressZH: String,
        @ColumnInfo(name = "address_en")
        val addressEN: String,
        @ColumnInfo(name = "size_zh")
        val sizeZH: String,
        @ColumnInfo(name = "size_en")
        val sizeEN: String,
        @ColumnInfo(name = "phone")
        val phone: String,
        @ColumnInfo(name = "latitude")
        val latitude: Double,
        @ColumnInfo(name = "longitude")
        val longitude: Double
) {

    companion object {

        private val districts: Map<String, String> = mapOf(

                "中西區" to "HK",
                "灣仔區" to "HK",
                "東區" to "HK",
                "南區" to "HK",

                "九龍城區" to "KLN",
                "黃大仙區" to "KLN",
                "觀塘區" to "KLN",
                "油尖旺區" to "KLN",
                "深水埗區" to "KLN",

                "荃灣區" to "NT",
                "葵青區" to "NT",
                "西貢區" to "NT",
                "沙田區" to "NT",
                "大埔區" to "NT",
                "北區" to "NT",
                "屯門區" to "NT",
                "元朗區" to "NT",
                "離島區" to "NT"
        )

        fun from(gymRoom: Gym): GymEntity {

            return GymEntity(
                    gymRoom.name["en"]!!
                            .replace(Regex("[^A-Za-z0-9]"), "")
                            .toUpperCase(),
                    gymRoom.name["zh"]!!,
                    gymRoom.name["en"]!!,
                    gymRoom.district["zh"]!!,
                    gymRoom.district["en"]!!,
                    districts[gymRoom.district["zh"]!!]!!,
                    gymRoom.address["zh"]!!,
                    gymRoom.address["en"]!!,
                    gymRoom.size["zh"]!!,
                    gymRoom.size["en"]!!,
                    gymRoom.phone,
                    gymRoom.latitude,
                    gymRoom.longitude
            )
        }
    }
}
