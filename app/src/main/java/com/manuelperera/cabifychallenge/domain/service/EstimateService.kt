package com.manuelperera.cabifychallenge.domain.service

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import com.manuelperera.cabifychallenge.domain.model.EstimateModel
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import io.reactivex.Completable
import io.reactivex.Observable

open class EstimateService(private val estimateModel: EstimateModel) {

    lateinit var mTravel: Travel

    open fun setTravel(travel: Travel): Completable =
            Completable.create {
                mTravel = travel
                it.onComplete()
            }

    open fun getEstimates(): Observable<Transaction<List<Estimate>>> =
            estimateModel.getEstimates(mTravel)

}