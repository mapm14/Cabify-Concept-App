package com.manuelperera.cabifychallenge.usecase.estimate

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.usecase.UseCase
import com.manuelperera.cabifychallenge.domain.service.EstimateService
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetEstimatesUseCase(private val estimateService: EstimateService<Estimate>) : UseCase<Observable<Transaction<List<Estimate>>>> {

    override fun bind(): Observable<Transaction<List<Estimate>>> =
            estimateService.getEstimates().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}