package com.architecture.kotlinmvvm.network

import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.model.Gym
import io.reactivex.Flowable

class RetrofitRemoteRepository(
        private val api: API
) : RemoteRepository {

    override fun getGymRooms(): Flowable<List<Gym>> = api.getGymRoom()
            .flatMapIterable { list -> list }
            .map { res -> Gym.from(res) }
            .toList()
            .toFlowable()

    override fun getEquipments(): Flowable<List<Equipment>> = api.getEquipment()
            .flatMapIterable { list -> list }
            .map { res -> Equipment.from(res) }
            .toList()
            .toFlowable()
}