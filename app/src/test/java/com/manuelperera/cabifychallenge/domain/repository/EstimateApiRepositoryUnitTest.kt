package com.manuelperera.cabifychallenge.domain.repository

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.client.transaction.TransactionStatus
import com.manuelperera.cabifychallenge.client.api_client.EstimateApiClient
import com.manuelperera.cabifychallenge.client.transaction.TransactionRequest
import com.manuelperera.cabifychallenge.client.transaction.TransactionRequestFactory
import com.manuelperera.cabifychallenge.domain.objects.*
import com.manuelperera.cabifychallenge.domain.repository.api.EstimateApiRepository
import com.manuelperera.cabifychallenge.domain.repository.cache.EstimateCacheRepository
import com.nhaarman.mockito_kotlin.any
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
class EstimateApiRepositoryUnitTest {

    private val travel: Travel = Travel(listOf(Stop(listOf(0.0, 0.0), "Carrefour La Latina", "C. Calatrava", "18", "Madrid", "Spain", "", Contact("Manuel", "+34", "000555111")),
            Stop(listOf(1.0, 2.0), "Carrefour La Latina", "C. Calatrava", "18", "Madrid", "Spain", "", Contact("Manuel", "+34", "000555111"))), "")

    @Mock private
    lateinit var transactionRequest: TransactionRequest<Estimate>

    @Mock private
    lateinit var transactionRequestFactory: TransactionRequestFactory<Estimate>

    @Mock private
    lateinit var estimateApiClient: EstimateApiClient

    private lateinit var estimateApiRepository: EstimateApiRepository

    private lateinit var estimateCacheRepository: EstimateCacheRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(transactionRequestFactory.createTransactionRequest()).doReturn(transactionRequest)

        estimateApiRepository = EstimateApiRepository(estimateApiClient, transactionRequestFactory)

        estimateCacheRepository = EstimateCacheRepository()
    }

    @Test
    fun getEstimatesFromApiWithSuccess() {
        val estimatesData: List<Estimate> = listOf(Estimate(VehicleType("1", "Premium Lux", "Premium", "Luxury car", Icons(""), "icon", Eta(0, 10, true, "")), 100, "100,00 €", "EUR", "€"),
                Estimate(VehicleType("2", "Premium Lux", "Premium", "Luxury car", Icons(""), "icon", Eta(0, 10, true, "")), 100, "100,00 €", "EUR", "€"))

        whenever(estimateApiClient.getEstimates(travel)).doReturn(Observable.create { observer ->
            observer.onNext(estimatesData)
            observer.onComplete()
        })

        whenever(transactionRequest.modifyObservableList(any())).doReturn(Observable.create { observer ->
            observer.onNext(Transaction(estimatesData, TransactionStatus.SUCCESS))
            observer.onComplete()
        })

        val testObserver = estimateApiRepository.getEstimates(travel).test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue { transaction ->
            transaction.data == estimatesData && transaction.isSuccess
        }
    }

    @Test
    fun gesEstimatesFromApiWithError() {
        whenever(estimateApiClient.getEstimates(travel)).doReturn(Observable.create { observer ->
            observer.onComplete()
        })

        whenever(transactionRequest.modifyObservableList(any())).doReturn(Observable.create { observer ->
            observer.onNext(Transaction(TransactionStatus.ERROR))
            observer.onComplete()
        })

        val testObserver = estimateApiRepository.getEstimates(travel).test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue { transaction ->
            transaction.data == null && !transaction.isSuccess && transaction.status == TransactionStatus.ERROR
        }
    }

    @Test
    fun getEstimatesFromApiWithTimeout() {
        whenever(estimateApiClient.getEstimates(travel)).doReturn(Observable.create { observer ->
            observer.onComplete()
        })

        whenever(transactionRequest.modifyObservableList(any())).doReturn(Observable.create { observer ->
            observer.onNext(Transaction(TransactionStatus.TIMEOUT))
            observer.onComplete()
        })

        val testObserver = estimateApiRepository.getEstimates(travel).test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue { transaction ->
            transaction.data == null && !transaction.isSuccess && transaction.status == TransactionStatus.TIMEOUT
        }
    }
}