package com.olleh.gyme.equipment.dagger

import com.olleh.gyme.equipment.ui.EquipmentListAdapter
import dagger.Module
import dagger.Provides

@Module
class EquipmentListModule {

    @Provides
    fun provideFacilitiesListAdapter() = EquipmentListAdapter()
}