package com.manuelperera.cabifychallenge.injection.module.domain.repository

import com.manuelperera.cabifychallenge.client.api_client.EstimateApiClient
import com.manuelperera.cabifychallenge.client.transaction.TransactionRequestFactory
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.repository.api.EstimateApiRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiRepositoryModule {

    @Provides
    @Singleton
    fun estimateApiRepository(estimateApiClient: EstimateApiClient,
                              estimateTransactionRequestFactory: TransactionRequestFactory<Estimate>): EstimateApiRepository =
            EstimateApiRepository(estimateApiClient, estimateTransactionRequestFactory)

}