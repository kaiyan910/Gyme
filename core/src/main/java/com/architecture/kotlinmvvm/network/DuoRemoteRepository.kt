package com.architecture.kotlinmvvm.network

import com.architecture.kotlinmvvm.database.LocalRepository
import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.model.Gym
import io.reactivex.Flowable

class DuoRemoteRepository(
        private val remoteRepository: RemoteRepository,
        private val localRepository: LocalRepository
) : RemoteRepository {

    override fun getGymRooms(): Flowable<List<Gym>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEquipments(): Flowable<List<Equipment>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}