package com.manuelperera.cabifychallenge.client.api_client

import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import io.reactivex.Observable

interface EstimateApiClient {

    fun getEstimates(travel: Travel): Observable<List<Estimate>>

}