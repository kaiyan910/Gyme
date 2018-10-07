package com.olleh.gyme.equipment.dagger

import com.architecture.kotlinmvvm.manager.DialogManager
import com.architecture.kotlinmvvm.manager.MaterialDialogManager
import com.olleh.gyme.equipment.ui.EquipmentSearchResultActivity
import com.olleh.gyme.equipment.ui.EquipmentSearchResultGymListAdapter
import dagger.Module
import dagger.Provides

@Module
class EquipmentSearchResultModule {

    @Provides
    fun provideDialogManager(activity: EquipmentSearchResultActivity): DialogManager = MaterialDialogManager(activity)

    @Provides
    fun provideEquipmentSearchResultGymListAdapter() = EquipmentSearchResultGymListAdapter()
}