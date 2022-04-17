package com.xardev.tangoshop.di.modules

import com.xardev.tangoshop.domain.schedulers.DefaultSchedulers
import com.xardev.tangoshop.domain.schedulers.SchedulersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SchedulersModule {

    @Provides
    fun getSchedulersProvider(): SchedulersProvider {
        return DefaultSchedulers
    }

}