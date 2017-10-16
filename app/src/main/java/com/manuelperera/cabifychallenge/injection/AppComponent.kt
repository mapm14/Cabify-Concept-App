package com.manuelperera.cabifychallenge.injection

import android.content.Context
import com.manuelperera.cabifychallenge.injection.module.AppModule
import com.manuelperera.cabifychallenge.injection.module.client.ApiModule
import com.manuelperera.cabifychallenge.injection.module.client.TransactionModule
import com.manuelperera.cabifychallenge.injection.module.domain.model.ModelModule
import com.manuelperera.cabifychallenge.injection.module.domain.repository.ApiRepositoryModule
import com.manuelperera.cabifychallenge.injection.module.domain.repository.CacheRepositoryModule
import com.manuelperera.cabifychallenge.injection.module.domain.service.BusinessServiceModule
import com.manuelperera.cabifychallenge.injection.module.usecase.EstimateUseCaseModule
import com.manuelperera.cabifychallenge.usecase.estimate.GetEstimatesUseCase
import com.manuelperera.cabifychallenge.usecase.estimate.SetTravelUseCase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        ApiModule::class,
        TransactionModule::class,
        BusinessServiceModule::class,
        ModelModule::class,
        ApiRepositoryModule::class,
        CacheRepositoryModule::class,
        EstimateUseCaseModule::class))
interface AppComponent {

    fun provideContext(): Context

    // Use Case

    fun provideSetTravelUseCase(): SetTravelUseCase

    fun provideGetEstimatesUseCase(): GetEstimatesUseCase

}