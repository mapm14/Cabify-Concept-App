package com.manuelperera.cabifychallenge.domain.service

import com.manuelperera.cabifychallenge.domain.model.EstimateModel
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.domain.objects.Travel
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EstimateServiceUnitTest {

    private val travel: Travel = mock()

    @Mock
    lateinit var estimateModel: EstimateModel<Estimate>

    private val estimatesData: List<Estimate> = mock()

    private lateinit var estimateService: EstimateService<Estimate>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        estimateService = EstimateService(estimateModel)
    }

    @Test
    fun getEstimates() {
        estimateService.setTravel(travel).andThen {
            val testObserver = estimateService.getEstimates().test()

            testObserver.assertComplete()
            testObserver.assertValueCount(1)
            testObserver.assertValue { transaction ->
                transaction.data == estimatesData && transaction.isSuccess
            }
        }
    }

}