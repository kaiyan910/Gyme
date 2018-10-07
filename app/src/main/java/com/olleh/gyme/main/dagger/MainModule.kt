package com.olleh.gyme.main.dagger

import com.architecture.kotlinmvvm.manager.DialogManager
import com.architecture.kotlinmvvm.manager.MaterialDialogManager
import com.olleh.gyme.main.ui.MainActivity
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    fun provideDialogManager(activity: MainActivity): DialogManager = MaterialDialogManager(activity)
}