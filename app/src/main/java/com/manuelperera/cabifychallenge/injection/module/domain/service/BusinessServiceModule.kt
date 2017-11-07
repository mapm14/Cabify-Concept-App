package com.manuelperera.cabifychallenge.injection.module.domain.service

import com.manuelperera.cabifychallenge.domain.model.EstimateModel
import com.manuelperera.cabifychallenge.domain.service.EstimateService
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BusinessServiceModule {

    @Provides
    @Singleton
    fun estimateService(estimateModel: EstimateModel): EstimateService =
            EstimateService(estimateModel)

}