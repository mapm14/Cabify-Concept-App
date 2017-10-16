package com.manuelperera.cabifychallenge.domain.model

import co.develoop.androidcleanarchitecture.client.transaction.Transaction
import co.develoop.androidcleanarchitecture.client.transaction.TransactionStatus
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
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

    private val travel: Travel = mock()

    @Mock
    lateinit var estimateApiRepository: EstimateApiRepository<Estimate>

    @Mock
    lateinit var estimateCacheRepository: EstimateCacheRepository<Estimate>

    private lateinit var estimateModel: EstimateModel<Estimate>

    private val estimatesData: List<Estimate> = mock()

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