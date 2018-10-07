package com.architecture.kotlinmvvm.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.architecture.kotlinmvvm.database.entity.GymEntity
import io.reactivex.Single

@Dao
interface GymDao {

    @Query("SELECT * FROM gym WHERE id = :id")
    fun findById(id: String): Single<GymEntity>

    @Query("SELECT * FROM gym")
    fun getAll(): Single<List<GymEntity>>

    @Query("SELECT * FROM gym WHERE area = :area")
    fun findByArea(area: String): Single<List<GymEntity>>

    @Query("SELECT * FROm gym WHERE id IN (:gymRoomIds)")
    fun findByGymRoomIds(gymRoomIds: Array<String>): Single<List<GymEntity>>

    @Query("DELETE FROM gym")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gymRooms: List<GymEntity>)
}