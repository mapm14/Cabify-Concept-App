package com.manuelperera.cabifychallenge.injection.module.domain.repository

import com.manuelperera.cabifychallenge.domain.repository.cache.EstimateCacheRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheRepositoryModule {

    @Provides
    @Singleton
    fun estimateCacheRepository(): EstimateCacheRepository =
            EstimateCacheRepository()

}