package com.architecture.kotlinmvvm.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index

@Entity(
        tableName = "gym_equipment",
        indices = [
            Index("gym_id"),
            Index("equipment_id")
        ],
        primaryKeys = ["gym_id", "equipment_id"],
        foreignKeys = [
            ForeignKey(
                    entity = GymEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["gym_id"]
            ),
            ForeignKey(
                    entity = EquipmentEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["equipment_id"]
            )
        ]
)
class GymEquipmentEntity(

        @ColumnInfo(name = "gym_id")
        val gymId: String,
        @ColumnInfo(name = "equipment_id")
        val equipmentId: String,
        @ColumnInfo(name = "number_of_seat")
        val numberOfSeat: Int,
        @ColumnInfo(name = "shared")
        val shared: Boolean,
        @ColumnInfo(name = "room_number")
        val roomNumber: Int
)