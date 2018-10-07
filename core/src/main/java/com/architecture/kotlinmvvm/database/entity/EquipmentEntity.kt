package com.architecture.kotlinmvvm.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "equipment")
data class EquipmentEntity(

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,
        @ColumnInfo(name = "name_en")
        val nameEN: String,
        @ColumnInfo(name = "name_zh")
        val nameZH: String
)