package com.architecture.kotlinmvvm.dagger

import android.arch.lifecycle.ViewModel
import javax.inject.Inject
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.NonNull
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
@AppScope
@JvmSuppressWildcards
class InjectionViewModelFactory @Inject constructor(
        private val creators: Map<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    @NonNull
    override fun <T : ViewModel> create(@NonNull modelClass: Class<T>): T {

        val creator = creators[modelClass]
                ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
                ?: throw IllegalArgumentException("unknown model class $modelClass")

        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}