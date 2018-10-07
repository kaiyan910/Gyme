package com.olleh.gyme.gym.dagger

import com.architecture.kotlinmvvm.manager.DialogManager
import com.architecture.kotlinmvvm.manager.MaterialDialogManager
import com.olleh.gyme.gym.ui.GymActivity
import com.olleh.gyme.gym.ui.GymEquipmentAdapter
import com.olleh.gyme.gym.ui.GymInfoAdapter
import dagger.Module
import dagger.Provides

@Module
class GymModule {

    @Provides
    fun provideDialogManager(activity: GymActivity): DialogManager = MaterialDialogManager(activity)

    @Provides
    fun provideGymInfoAdapter(): GymInfoAdapter = GymInfoAdapter()

    @Provides
    fun provideGymEquipmentAdapter(): GymEquipmentAdapter = GymEquipmentAdapter()
}