package com.manuelperera.cabifychallenge.domain.service

import com.manuelperera.cabifychallenge.domain.model.EstimateModel
import com.manuelperera.cabifychallenge.domain.objects.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EstimateServiceUnitTest {

    private val travel: Travel = Travel(listOf(Stop(listOf(0.0, 0.0), "Carrefour La Latina", "C. Calatrava", "18", "Madrid", "Spain", "", Contact("Manuel", "+34", "000555111")),
            Stop(listOf(1.0, 2.0), "Carrefour La Latina", "C. Calatrava", "18", "Madrid", "Spain", "", Contact("Manuel", "+34", "000555111"))), "")

    @Mock
    private
    lateinit var estimateModel: EstimateModel

    private val estimatesData: List<Estimate> = listOf(Estimate(VehicleType("1", "Premium Lux", "Premium", "Luxury car", Icons(""), "icon", Eta(0, 10, true, "")), 100, "100,00 €", "EUR", "€"),
            Estimate(VehicleType("2", "Premium Lux", "Premium", "Luxury car", Icons(""), "icon", Eta(0, 10, true, "")), 100, "100,00 €", "EUR", "€"))

    private lateinit var estimateService: EstimateService

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