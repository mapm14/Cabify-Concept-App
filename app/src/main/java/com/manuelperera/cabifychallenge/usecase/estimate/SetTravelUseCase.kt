package com.manuelperera.cabifychallenge.usecase.estimate

import co.develoop.androidcleanarchitecture.usecase.UseCaseParams
import co.develoop.androidcleanarchitecture.usecase.UseCaseWithParams
import com.manuelperera.cabifychallenge.domain.service.EstimateService
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class SetTravelUseCase(private val estimateService: EstimateService<Estimate>) : UseCaseWithParams<Completable, SetTravelUseCase.Params> {

    override fun bind(params: Params): Completable =
            estimateService.setTravel(params.travel).subscribeOn(Schedulers.io())

    class Params(val travel: Travel) : UseCaseParams()

}