package com.manuelperera.cabifychallenge.client.api_client

import com.manuelperera.cabifychallenge.client.retrofit.RetrofitEstimateApiClient
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import io.reactivex.Observable

class EstimateApiClientImpl(private val retrofitEstimateApiClient: RetrofitEstimateApiClient) : EstimateApiClient {

    override fun getEstimates(travel: Travel): Observable<List<Estimate>> =
            retrofitEstimateApiClient.getEstimates(travel)

}