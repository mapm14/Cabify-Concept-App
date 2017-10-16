package com.manuelperera.cabifychallenge.injection.module.usecase

import com.manuelperera.cabifychallenge.domain.service.EstimateService
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.usecase.estimate.GetEstimatesUseCase
import com.manuelperera.cabifychallenge.usecase.estimate.SetTravelUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class EstimateUseCaseModule {

    @Provides
    @Singleton
    fun setTravelUseCase(estimateService: EstimateService<Estimate>): SetTravelUseCase =
            SetTravelUseCase(estimateService)

    @Provides
    @Singleton
    fun getEstimatesUseCase(estimateService: EstimateService<Estimate>): GetEstimatesUseCase =
            GetEstimatesUseCase(estimateService)

}