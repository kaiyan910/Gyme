package com.olleh.gyme.main.dagger

import com.olleh.gyme.equipment.dagger.EquipmentListModule
import com.olleh.gyme.equipment.ui.EquipmentListFragment
import com.olleh.gyme.map.ui.MapFragment
import com.olleh.gyme.settings.ui.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun bindMapFragment(): MapFragment

    @ContributesAndroidInjector(modules = [
        EquipmentListModule::class
    ])
    internal abstract fun bindEquipmentListFragment(): EquipmentListFragment

    @ContributesAndroidInjector
    internal abstract fun bindSettingsFragment(): SettingsFragment
}