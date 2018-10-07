package com.architecture.kotlinmvvm.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.architecture.kotlinmvvm.database.dao.EquipmentDao
import com.architecture.kotlinmvvm.database.dao.GymDao
import com.architecture.kotlinmvvm.database.dao.GymEquipmentDao
import com.architecture.kotlinmvvm.database.entity.EquipmentEntity
import com.architecture.kotlinmvvm.database.entity.GymEntity
import com.architecture.kotlinmvvm.database.entity.GymEquipmentEntity

@Database(
        entities = [
            GymEntity::class,
            EquipmentEntity::class,
            GymEquipmentEntity::class
        ],
        exportSchema = false,
        version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gymDao(): GymDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun gymEquipmentDao(): GymEquipmentDao
}