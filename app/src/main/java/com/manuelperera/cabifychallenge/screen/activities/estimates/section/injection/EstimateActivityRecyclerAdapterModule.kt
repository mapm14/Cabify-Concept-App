package com.manuelperera.cabifychallenge.screen.activities.estimates.section.injection

import com.manuelperera.cabifychallenge.screen.activities.estimates.section.EstimatesActivityRecyclerAdapterPresenter
import com.manuelperera.cabifychallenge.usecase.estimate.GetEstimatesUseCase
import dagger.Module
import dagger.Provides

@Module
class EstimateActivityRecyclerAdapterModule {

    @Provides
    @EstimateActivityRecyclerAdapterScope
    fun estimateActivityRecyclerPresenter(getEstimatesUseCase: GetEstimatesUseCase): EstimatesActivityRecyclerAdapterPresenter =
            EstimatesActivityRecyclerAdapterPresenter(getEstimatesUseCase)

}