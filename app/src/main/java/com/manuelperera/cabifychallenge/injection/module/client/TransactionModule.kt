package com.manuelperera.cabifychallenge.injection.module.client

import com.manuelperera.cabifychallenge.client.transaction.TransactionRequestFactory
import com.manuelperera.cabifychallenge.client.transaction.TransactionRequestFactoryImpl
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TransactionModule {

    @Provides
    @Singleton
    fun estimateTransactionRequestFactory(): TransactionRequestFactory<Estimate> =
            TransactionRequestFactoryImpl()

}