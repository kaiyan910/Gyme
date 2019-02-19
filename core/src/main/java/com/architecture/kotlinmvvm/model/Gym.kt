package com.architecture.kotlinmvvm.model

import com.architecture.kotlinmvvm.database.entity.GymEntity
import com.architecture.kotlinmvvm.network.response.GymResponse
import timber.log.Timber

data class Gym(

        val id: String,
        val district: Map<String, String>,
        val name: Map<String, String>,
        val address: Map<String, String>,
        val size: Map<String, String>,
        val phone: String,
        val latitude: Double,
        val longitude: Double
) {

    fun district(): String =  district["zh"] ?: "-"

    fun size(): String = size["zh"] ?: "-"

    fun name(): String = name["zh"] ?: "-"

    fun address(): String = address["zh"] ?: "-"

    companion object {

        fun from(db: GymEntity): Gym {

            val distinctMap = mutableMapOf<String, String>()
            val nameMap = mutableMapOf<String, String>()
            val addressMap = mutableMapOf<String, String>()
            val sizeMap = mutableMapOf<String, String>()

            distinctMap["en"] = db.districtEN
            distinctMap["zh"] = db.districtZH

            nameMap["en"] = db.nameEN
            nameMap["zh"] = db.nameZH

            addressMap["en"] = db.addressEN
            addressMap["zh"] = db.addressZH

            sizeMap["en"] = db.sizeEN
            sizeMap["zh"] = db.sizeZH

            return Gym(db.id, distinctMap, nameMap, addressMap, sizeMap, db.phone, db.latitude, db.longitude)
        }

        fun from(json: GymResponse): Gym {

            val distinctMap = mutableMapOf<String, String>()
            val nameMap = mutableMapOf<String, String>()
            val addressMap = mutableMapOf<String, String>()
            val sizeMap = mutableMapOf<String, String>()

            distinctMap["en"] = json.districtEN
            distinctMap["zh"] = json.districtZH

            nameMap["en"] = json.nameEN
            nameMap["zh"] = json.nameZH

            addressMap["en"] = json.addressEN
            addressMap["zh"] = json.addressZH

            sizeMap["en"] = json.sizeEN
            sizeMap["zh"] = json.sizeZH

            val id = json.nameEN
                    .replace(Regex("[^A-Za-z0-9]"), "")
                    .toUpperCase()

            val latitude = json.latitude.split("-")
            val longitude = json.longitude.split("-")

            val lat = convert(latitude[0].toInt(), latitude[1].toInt(), latitude[2].toInt())
            val lng = convert(longitude[0].toInt(), longitude[1].toInt(), longitude[2].toInt())

            return Gym(id, distinctMap, nameMap, addressMap, sizeMap, json.phone, lat, lng)
        }

        private fun convert(degree: Int, min: Int, sec: Int): Double = degree + (min / 60.0) + (sec / 3600.0)

        private fun convert(degree: Double): String {

            val abs = Math.abs(degree)
            val degrees = Math.floor(degree)
            val minutesNotTruncated = (abs - degrees) * 60
            val minutes = Math.floor(minutesNotTruncated)
            val seconds = Math.floor((minutesNotTruncated - minutes) * 60)

            return "$degrees-$minutes-$seconds"
        }
    }
}