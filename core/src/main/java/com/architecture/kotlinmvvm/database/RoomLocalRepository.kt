package com.architecture.kotlinmvvm.database

import com.architecture.kotlinmvvm.database.entity.EquipmentDetailsEntity
import com.architecture.kotlinmvvm.database.entity.EquipmentEntity
import com.architecture.kotlinmvvm.database.entity.GymEntity
import com.architecture.kotlinmvvm.database.entity.GymEquipmentEntity
import com.architecture.kotlinmvvm.model.Equipment
import com.architecture.kotlinmvvm.model.Gym
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import timber.log.Timber

class RoomLocalRepository(private val database: AppDatabase) : LocalRepository {

    override fun clear(): Flowable<Boolean> {

        return Flowable.fromCallable {

            database.clearAllTables()
            true
        }
    }

    override fun getEquipmentsById(id: String): Flowable<List<Equipment>> {

        return database.gymEquipmentDao()
                .findEquipmentByGymId(id)
                .toFlowable()
                .compose(transformEquipmentDetails())
    }

    override fun getGymById(id: String): Flowable<Gym> {

        return database.gymDao()
                .findById(id)
                .toFlowable()
                .map { Gym.from(it) }
    }

    override fun getGymByIds(ids: Array<String>): Flowable<List<Gym>> {

        return database.gymDao()
                .findByGymRoomIds(ids)
                .toFlowable()
                .compose(transformGym())
    }

    override fun getEquipmentsByIds(ids: List<String>): Flowable<List<Equipment>> {

        return database.equipmentDao()
                .findByIds(ids.toTypedArray())
                .toFlowable()
                .compose(transformEquipment())
    }

    override fun getGymByArea(area: String): Flowable<List<Gym>> {

        return database.gymDao()
                .findByArea(area)
                .toFlowable()
                .compose(transformGym())

    }

    override fun getGym(): Flowable<List<Gym>> {

        return database.gymDao()
                .getAll()
                .toFlowable()
                .compose(transformGym())
    }

    override fun init(gymRoom: List<Gym>, equipment: List<Equipment>): Flowable<Boolean> {

        // 1. insert gym
        return Flowable.fromIterable(gymRoom)
                .map { GymEntity.from(it) }
                .toList()
                .toFlowable()
                .flatMap {

                    Timber.d("<DEBUG> insert gym=${it.size}")
                    database.gymDao().insertAll(it)
                    Flowable.just(true)
                }

                // 2. distinct equipment then insert equipment
                .flatMap { _ ->

                    Flowable.fromIterable(equipment)
                            .distinct { it.id }
                            .map { EquipmentEntity(it.id, it.nameEN, it.nameZH) }
                            .toList()
                            .toFlowable()
                            .flatMap {

                                Timber.d("<DEBUG> insert equipment=${it.size}")
                                database.equipmentDao().insertAll(it)
                                Flowable.just(true)
                            }
                }

                // 3. insert relation between gym and equipment
                .flatMap { _ ->

                    Flowable.fromIterable(equipment)
                            .map {

                                GymEquipmentEntity(
                                        it.gymId,
                                        it.id,
                                        it.numberOfSeat,
                                        it.shared,
                                        it.roomNumber
                                )
                            }
                            .toList()
                            .toFlowable()
                            .flatMap {

                                Timber.d("<DEBUG> insert gym-equipment=${it.size}")
                                database.gymEquipmentDao().insertAll(it)
                                Flowable.just(true)
                            }
                }
    }


    override fun getGymByMatchingAllEquipments(requiredEquipments: List<String>): Flowable<List<Gym>> {

        return database.gymEquipmentDao()
                .findGymMatchAllEquipments(requiredEquipments.toTypedArray(), requiredEquipments.size)
                .toFlowable()
                .compose(transformGym())
    }

    override fun getGymByMatchingPartialEquipments(requiredEquipments: List<String>): Flowable<List<String>> {

        return database.gymEquipmentDao()
                .findGymMatchPartialEquipments(requiredEquipments.toTypedArray())
                .toFlowable()
    }

    override fun getEquipments(): Flowable<List<Equipment>> {

        return database.equipmentDao()
                .getAll()
                .toFlowable()
                .compose(transformEquipment())
    }

    override fun getEquipmentsByKeyword(keyword: String): Flowable<List<Equipment>> {

        return database.equipmentDao()
                .findByKeyword(keyword)
                .toFlowable()
                .compose(transformEquipment())
    }

    private fun transformEquipmentDetails(): FlowableTransformer<List<EquipmentDetailsEntity>, List<Equipment>> {

        return FlowableTransformer { upstream ->

            upstream
                    .flatMapIterable { list -> list }
                    .map { Equipment.from(it) }
                    .toList()
                    .toFlowable()
        }
    }

    private fun transformEquipment(): FlowableTransformer<List<EquipmentEntity>, List<Equipment>> {

        return FlowableTransformer { upstream ->

            upstream
                    .flatMapIterable { list -> list }
                    .map { Equipment.from(it) }
                    .toList()
                    .toFlowable()
        }
    }

    private fun transformGym(): FlowableTransformer<List<GymEntity>, List<Gym>> {

        return FlowableTransformer { upstream ->

            upstream
                    .flatMapIterable { list -> list }
                    .map { Gym.from(it) }
                    .toList()
                    .toFlowable()
        }
    }
}