package com.architecture.kotlinmvvm.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.architecture.kotlinmvvm.database.entity.EquipmentDetailsEntity
import com.architecture.kotlinmvvm.database.entity.GymEntity
import com.architecture.kotlinmvvm.database.entity.GymEquipmentEntity
import io.reactivex.Single

@Dao
interface GymEquipmentDao {

    @Query("SELECT * FROM gym INNER JOIN gym_equipment ON gym.id = gym_equipment.gym_id WHERE gym_equipment.equipment_id IN (:equipmentIds) GROUP BY gym_equipment.gym_id HAVING COUNT(DISTINCT gym_equipment.equipment_id) = :count")
    fun findGymMatchAllEquipments(equipmentIds: Array<String>, count: Int): Single<List<GymEntity>>

    @Query("SELECT id FROM gym INNER JOIN gym_equipment ON gym.id = gym_equipment.gym_id WHERE gym_equipment.equipment_id IN (:equipmentIds)")
    fun findGymMatchPartialEquipments(equipmentIds: Array<String>): Single<List<String>>

    @Query("SELECT equipment.id, equipment.name_en, equipment.name_zh, gym_equipment.number_of_seat, gym_equipment.room_number, gym_equipment.shared FROM equipment INNER JOIN gym_equipment ON equipment.id = gym_equipment.equipment_id WHERE gym_equipment.gym_id = :gymId")
    fun findEquipmentByGymId(gymId: String): Single<List<EquipmentDetailsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gymEquipmentList: List<GymEquipmentEntity>)
}