package com.architecture.kotlinmvvm.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

data class EquipmentDetailsEntity(

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,
        @ColumnInfo(name = "name_en")
        val nameEN: String,
        @ColumnInfo(name = "name_zh")
        val nameZH: String,
        @ColumnInfo(name = "number_of_seat")
        val numberOfSeat: Int,
        @ColumnInfo(name = "shared")
        val shared: Boolean,
        @ColumnInfo(name = "room_number")
        val roomNumber: Int
)