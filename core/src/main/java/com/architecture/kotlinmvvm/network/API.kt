package com.architecture.kotlinmvvm.network

import com.architecture.kotlinmvvm.network.response.EquipmentResponse
import com.architecture.kotlinmvvm.network.response.GymResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface API {

    @GET("/datagovhk/facility/facility-fitrm.json")
    fun getGymRoom(): Observable<List<GymResponse>>
    @GET("/datagovhk/facility/facility-fiteqmt.json")
    fun getEquipment(): Observable<List<EquipmentResponse>>
}