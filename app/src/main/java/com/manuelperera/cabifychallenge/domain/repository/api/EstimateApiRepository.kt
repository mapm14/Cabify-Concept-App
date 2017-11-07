package com.manuelperera.cabifychallenge.domain.repository.api

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import com.manuelperera.cabifychallenge.client.api_client.EstimateApiClient
import com.manuelperera.cabifychallenge.client.transaction.TransactionRequestFactory
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import io.reactivex.Observable

open class EstimateApiRepository(private val estimateApiClient: EstimateApiClient,
                                 private val estimateTransactionRequestFactory: TransactionRequestFactory<Estimate>) {

    open fun getEstimates(travel: Travel): Observable<Transaction<List<Estimate>>> =
            estimateTransactionRequestFactory.createTransactionRequest().modifyObservableList(estimateApiClient.getEstimates(travel))

}