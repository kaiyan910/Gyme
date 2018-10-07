package com.architecture.kotlinmvvm.network

import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.model.Gym
import io.reactivex.Flowable

interface RemoteRepository {

    fun getGymRooms(): Flowable<List<Gym>>
    fun getEquipments(): Flowable<List<Equipment>>
}