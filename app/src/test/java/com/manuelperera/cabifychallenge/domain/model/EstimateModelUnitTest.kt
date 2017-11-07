package com.manuelperera.cabifychallenge.domain.model

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.client.transaction.TransactionStatus
import com.manuelperera.cabifychallenge.domain.objects.*
import com.manuelperera.cabifychallenge.domain.repository.api.EstimateApiRepository
import com.manuelperera.cabifychallenge.domain.repository.cache.EstimateCacheRepository
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EstimateModelUnitTest {

    private val travel: Travel = Travel(listOf(Stop(listOf(0.0, 0.0), "Carrefour La Latina", "C. Calatrava", "18", "Madrid", "Spain", "", Contact("Manuel", "+34", "000555111")),
            Stop(listOf(1.0, 2.0), "Carrefour La Latina", "C. Calatrava", "18", "Madrid", "Spain", "", Contact("Manuel", "+34", "000555111"))), "")

    @Mock private
    lateinit var estimateApiRepository: EstimateApiRepository

    @Mock private
    lateinit var estimateCacheRepository: EstimateCacheRepository

    private lateinit var estimateModel: EstimateModel

    private val estimatesData: List<Estimate> = listOf(Estimate(VehicleType("1", "Premium Lux", "Premium", "Luxury car", Icons(""), "icon", Eta(0, 10, true, "")), 100, "100,00 €", "EUR", "€"),
            Estimate(VehicleType("2", "Premium Lux", "Premium", "Luxury car", Icons(""), "icon", Eta(0, 10, true, "")), 100, "100,00 €", "EUR", "€"))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        estimateModel = EstimateModel(estimateApiRepository, estimateCacheRepository)
    }

    @Test
    fun getEstimates() {
        whenever(estimateApiRepository.getEstimates(travel)).doReturn(Observable.create { observer ->
            observer.onNext(Transaction(estimatesData, TransactionStatus.SUCCESS))
            observer.onComplete()
        })

        val testObserver = estimateModel.getEstimates(travel).test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue { transaction ->
            transaction.data == estimatesData && transaction.isSuccess
        }
    }
}