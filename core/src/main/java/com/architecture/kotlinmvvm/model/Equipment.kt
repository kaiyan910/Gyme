package com.architecture.kotlinmvvm.model

import com.architecture.kotlinmvvm.database.entity.EquipmentDetailsEntity
import com.architecture.kotlinmvvm.database.entity.EquipmentEntity
import com.architecture.kotlinmvvm.network.response.EquipmentResponse

class Equipment(
        val id: String,
        val gymId: String,
        val nameZH: String,
        val nameEN: String,
        val numberOfSeat: Int,
        val shared: Boolean,
        val roomNumber: Int
) {

    companion object {

        fun from(db: EquipmentDetailsEntity): Equipment {

            return Equipment(

                    db.id,
                    "",
                    db.nameZH,
                    db.nameEN,
                    db.numberOfSeat,
                    db.shared,
                    db.roomNumber
            )
        }

        fun from(db: EquipmentEntity): Equipment {

            return Equipment(
                    db.id,
                    "",
                    db.nameZH,
                    db.nameEN,
                    0,
                    false,
                    0
            )
        }

        fun from(res: EquipmentResponse): Equipment {

            return Equipment(
                    res.equipmentEN
                            .replace(Regex("[^A-Za-z0-9]"), "")
                            .toUpperCase(),
                    res.nameEN
                            .replace(Regex("[^A-Za-z0-9]"), "")
                            .toUpperCase(),
                    res.equipmentZH,
                    res.equipmentEN,
                    res.numberOfSeat,
                    res.shared == "Y",
                    res.roomNumber
            )
        }
    }
}