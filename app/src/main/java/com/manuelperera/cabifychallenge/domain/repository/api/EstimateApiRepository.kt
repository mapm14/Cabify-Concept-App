package com.manuelperera.cabifychallenge.domain.repository.api

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import com.manuelperera.cabifychallenge.client.api_client.EstimateApiClient
import com.manuelperera.cabifychallenge.client.transaction.TransactionRequestFactory
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import io.reactivex.Observable

class EstimateApiRepository<E : Estimate>(private val estimateApiClient: EstimateApiClient<E>,
                                          private val estimateTransactionRequestFactory: TransactionRequestFactory<E>) {

    fun getEstimates(travel: Travel): Observable<Transaction<List<E>>> =
            estimateTransactionRequestFactory.createTransactionRequest().modifyObservableList(estimateApiClient.getEstimates(travel))

}