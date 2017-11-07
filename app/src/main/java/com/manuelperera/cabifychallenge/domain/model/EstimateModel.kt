package com.manuelperera.cabifychallenge.domain.model

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.client.transaction.TransactionStatus
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import com.manuelperera.cabifychallenge.domain.repository.api.EstimateApiRepository
import com.manuelperera.cabifychallenge.domain.repository.cache.EstimateCacheRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

open class EstimateModel(private val estimateApiRepository: EstimateApiRepository,
                         private val estimateCacheRepository: EstimateCacheRepository) {

    open fun getEstimates(travel: Travel): Observable<Transaction<List<Estimate>>> =
            estimateCacheRepository.getEstimate(travel)
                    .concatWith(estimateApiRepository.getEstimates(travel).doOnNext { transaction ->
                        if (transaction.isSuccess)
                            estimateCacheRepository.cacheEstimatesList(transaction.data, travel).subscribeOn(Schedulers.io()).subscribe()
                    })
                    .first(Transaction(TransactionStatus.EXCEPTION)).toObservable()

}