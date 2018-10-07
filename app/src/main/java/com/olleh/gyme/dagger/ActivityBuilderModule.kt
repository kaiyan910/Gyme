package com.olleh.gyme.dagger

import com.olleh.gyme.equipment.dagger.EquipmentSearchResultModule
import com.olleh.gyme.equipment.ui.EquipmentSearchResultActivity
import com.olleh.gyme.gym.dagger.GymModule
import com.olleh.gyme.gym.ui.GymActivity
import com.olleh.gyme.main.dagger.MainFragmentBuilderModule
import com.olleh.gyme.main.dagger.MainModule
import com.olleh.gyme.main.ui.MainActivity
import com.olleh.gyme.splashing.dagger.SplashingModule
import com.olleh.gyme.splashing.ui.SplashingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [
        SplashingModule::class
    ])
    internal abstract fun bindSplashingActivity(): SplashingActivity

    @ContributesAndroidInjector(modules = [
        MainModule::class,
        MainFragmentBuilderModule::class
    ])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [
        EquipmentSearchResultModule::class
    ])
    internal abstract fun bindEquipmentSearchResultActivity(): EquipmentSearchResultActivity

    @ContributesAndroidInjector(modules = [
        GymModule::class
    ])
    internal abstract fun bindGymActivity(): GymActivity
}

