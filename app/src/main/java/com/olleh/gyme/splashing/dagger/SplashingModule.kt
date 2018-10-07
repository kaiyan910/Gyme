package com.olleh.gyme.splashing.dagger

import com.architecture.kotlinmvvm.manager.DialogManager
import com.architecture.kotlinmvvm.manager.MaterialDialogManager
import com.olleh.gyme.main.ui.MainActivity
import com.olleh.gyme.splashing.ui.SplashingActivity
import dagger.Module
import dagger.Provides

@Module
class SplashingModule {

    @Provides
    fun provideDialogManager(activity: SplashingActivity): DialogManager = MaterialDialogManager(activity)
}