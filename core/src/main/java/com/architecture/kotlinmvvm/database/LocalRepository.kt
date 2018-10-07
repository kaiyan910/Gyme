package com.architecture.kotlinmvvm.database

import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.model.Gym
import io.reactivex.Flowable

interface LocalRepository {

    fun init(gymRoom: List<Gym>, equipment: List<Equipment>): Flowable<Boolean>

    fun getEquipmentsByKeyword(keyword: String): Flowable<List<Equipment>>

    fun getEquipmentsByIds(ids: List<String>): Flowable<List<Equipment>>

    fun getEquipmentsById(id: String): Flowable<List<Equipment>>

    fun getGymByMatchingAllEquipments(requiredEquipments: List<String>): Flowable<List<Gym>>

    fun getGymByMatchingPartialEquipments(requiredEquipments: List<String>): Flowable<List<String>>

    fun getGym(): Flowable<List<Gym>>

    fun getGymById(id: String): Flowable<Gym>

    fun getGymByIds(ids: Array<String>): Flowable<List<Gym>>

    fun getGymByArea(area: String): Flowable<List<Gym>>

    fun getEquipments(): Flowable<List<Equipment>>

    fun clear(): Flowable<Boolean>
}