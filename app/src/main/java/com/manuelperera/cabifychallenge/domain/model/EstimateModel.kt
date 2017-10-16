package com.manuelperera.cabifychallenge.domain.model

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.client.transaction.TransactionStatus
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import com.manuelperera.cabifychallenge.domain.repository.api.EstimateApiRepository
import com.manuelperera.cabifychallenge.domain.repository.cache.EstimateCacheRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class EstimateModel<E : Estimate>(private val estimateApiRepository: EstimateApiRepository<E>,
                                  private val estimateCacheRepository: EstimateCacheRepository<E>) {

    fun getEstimates(travel: Travel): Observable<Transaction<List<E>>> =
            estimateCacheRepository.getEstimate(travel)
                    .concatWith(estimateApiRepository.getEstimates(travel).doOnNext { transaction ->
                        if (transaction.isSuccess)
                            estimateCacheRepository.cacheEstimatesList(transaction.data, travel).subscribeOn(Schedulers.io()).subscribe()
                    })
                    .first(Transaction(TransactionStatus.EXCEPTION)).toObservable()

}