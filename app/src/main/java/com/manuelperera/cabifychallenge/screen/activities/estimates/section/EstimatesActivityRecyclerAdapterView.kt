package com.manuelperera.cabifychallenge.screen.activities.estimates.section

import co.develoop.androidcleanarchitecture.screen.presenter.recyclerview.RecyclerViewAdapterPresenterView
import com.manuelperera.cabifychallenge.domain.objects.Estimate

interface EstimatesActivityRecyclerAdapterView : RecyclerViewAdapterPresenterView {

    fun showInfoDialog(estimate: Estimate)

}