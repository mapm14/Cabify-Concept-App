package com.manuelperera.cabifychallenge.domain.repository

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.client.transaction.TransactionStatus
import com.manuelperera.cabifychallenge.client.api_client.EstimateApiClient
import com.manuelperera.cabifychallenge.client.transaction.TransactionRequest
import com.manuelperera.cabifychallenge.client.transaction.TransactionRequestFactory
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
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

    private val travel = mock<Travel>()

    @Mock
    lateinit var transactionRequest: TransactionRequest<Estimate>

    @Mock
    lateinit var transactionRequestFactory: TransactionRequestFactory<Estimate>

    @Mock
    lateinit var estimateApiClient: EstimateApiClient<Estimate>

    private lateinit var estimateApiRepository: EstimateApiRepository<Estimate>

    private lateinit var estimateCacheRepository: EstimateCacheRepository<Estimate>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(transactionRequestFactory.createTransactionRequest()).doReturn(transactionRequest)

        estimateApiRepository = EstimateApiRepository(estimateApiClient, transactionRequestFactory)

        estimateCacheRepository = EstimateCacheRepository()
    }

    @Test
    fun getEstimatesFromApiWithSuccess() {
        val estimatesData = arrayListOf(mock<Estimate>(), mock())

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