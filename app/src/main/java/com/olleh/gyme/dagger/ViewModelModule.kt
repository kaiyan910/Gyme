package com.olleh.gyme.dagger

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.architecture.kotlinmvvm.dagger.InjectionViewModelFactory
import com.architecture.kotlinmvvm.dagger.ViewModelKey
import com.olleh.gyme.equipment.viewModel.EquipmentSearchResultViewModel
import com.olleh.gyme.equipment.viewModel.EquipmentListViewModel
import com.olleh.gyme.equipment.viewModel.EquipmentViewModel
import com.olleh.gyme.gym.viewModel.GymViewModel
import com.olleh.gyme.main.viewModel.MainViewModel
import com.olleh.gyme.map.viewModel.MapViewModel
import com.olleh.gyme.settings.viewModel.SettingsViewModel
import com.olleh.gyme.splashing.viewModel.SplashingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(SplashingViewModel::class)
    abstract fun bindSplashingViewModel(viewModel: SplashingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(viewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EquipmentListViewModel::class)
    abstract fun bindEquipmentListViewModel(viewModel: EquipmentListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EquipmentSearchResultViewModel::class)
    abstract fun bindEquipmentSearchResultViewModel(viewModel: EquipmentSearchResultViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EquipmentViewModel::class)
    abstract fun bindEquipmentViewModel(viewModel: EquipmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GymViewModel::class)
    abstract fun bindGymViewModel(viewModel: GymViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: InjectionViewModelFactory): ViewModelProvider.Factory
}
