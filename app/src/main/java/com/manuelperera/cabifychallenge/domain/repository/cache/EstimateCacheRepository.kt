package com.manuelperera.cabifychallenge.domain.repository.cache

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.client.transaction.TransactionStatus
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import io.reactivex.Completable
import io.reactivex.Observable

open class EstimateCacheRepository {

    private var mEstimatesList: List<Estimate>? = null
    private var mTravel: Travel? = null

    fun getEstimate(travel: Travel): Observable<Transaction<List<Estimate>>> =
            Observable.create { consumer ->
                mEstimatesList?.let { estimatesList ->
                    if (mTravel == travel)
                        consumer.onNext(Transaction(estimatesList, TransactionStatus.SUCCESS))
                }
                consumer.onComplete()
            }


    fun cacheEstimatesList(estimatesList: List<Estimate>, travel: Travel): Completable =
            Completable.create { consumer ->
                mEstimatesList = estimatesList
                mTravel = travel
                consumer.onComplete()
            }

}