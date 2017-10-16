package com.manuelperera.cabifychallenge.client.retrofit

import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitEstimateApiClient {

    @POST("/api/v2/estimate")
    fun getEstimates(@Body travel: Travel): Observable<List<Estimate>>

}