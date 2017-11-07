package com.manuelperera.cabifychallenge.injection.module.domain.model

import com.manuelperera.cabifychallenge.domain.model.EstimateModel
import com.manuelperera.cabifychallenge.domain.repository.api.EstimateApiRepository
import com.manuelperera.cabifychallenge.domain.repository.cache.EstimateCacheRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModelModule {

    @Provides
    @Singleton
    fun estimateModel(estimateApiRepository: EstimateApiRepository,
                      estimateCacheRepository: EstimateCacheRepository): EstimateModel =
            EstimateModel(estimateApiRepository, estimateCacheRepository)

}