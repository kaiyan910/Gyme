package com.architecture.kotlinmvvm.manager

import com.architecture.kotlinmvvm.dagger.AppModule
import com.architecture.kotlinmvvm.database.LocalRepository
import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.model.Gym
import com.architecture.kotlinmvvm.network.RemoteRepository
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Named

class GovDataManager @Inject constructor(
        private val localRepository: LocalRepository,
        @Named(AppModule.REMOTE_REPOSITORY) private val remoteRepository: RemoteRepository
) : DataManager {

    override fun clean(): Flowable<Boolean> {

        return localRepository.clear()
    }

    override fun download(): Flowable<Boolean> {

        return Flowable
                .zip(
                        remoteRepository.getGymRooms(),
                        remoteRepository.getEquipments(),
                        BiFunction { gymRooms: List<Gym>, equipments: List<Equipment> ->

                            DataWrapper(gymRooms, equipments)
                        }
                )
                .flatMap {
                    localRepository.init(it.gymRooms, it.equipments)
                }
    }

    data class DataWrapper(val gymRooms: List<Gym>, val equipments: List<Equipment>)
}