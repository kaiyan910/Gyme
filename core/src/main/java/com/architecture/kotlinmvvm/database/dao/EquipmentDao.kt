package com.architecture.kotlinmvvm.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.architecture.kotlinmvvm.database.entity.EquipmentEntity
import io.reactivex.Single

@Dao
interface EquipmentDao {

    @Query("SELECT * FROM equipment")
    fun getAll(): Single<List<EquipmentEntity>>

    @Query("SELECT * FROM equipment WHERE name_en LIKE :keyword OR name_zh LIKE :keyword")
    fun findByKeyword(keyword: String): Single<List<EquipmentEntity>>

    @Query("SELECT * FROM equipment WHERE id IN (:equipmentIds)")
    fun findByIds(equipmentIds: Array<String>): Single<List<EquipmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gymRooms: List<EquipmentEntity>)
}