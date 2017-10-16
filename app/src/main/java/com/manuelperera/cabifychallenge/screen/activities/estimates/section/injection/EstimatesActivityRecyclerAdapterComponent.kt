package com.manuelperera.cabifychallenge.screen.activities.estimates.section.injection

import com.manuelperera.cabifychallenge.injection.AppComponent
import com.manuelperera.cabifychallenge.screen.activities.estimates.EstimatesActivity
import com.manuelperera.cabifychallenge.screen.activities.estimates.section.EstimatesActivityRecyclerAdapter
import com.manuelperera.cabifychallenge.screen.activities.map.MapsActivity
import com.manuelperera.cabifychallenge.screen.activities.map.injection.MapsActivityModule
import com.manuelperera.cabifychallenge.screen.activities.map.injection.MapsActivityScope
import dagger.Component

@EstimateActivityRecyclerAdapterScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EstimateActivityRecyclerAdapterModule::class))
interface EstimatesActivityRecyclerAdapterComponent {

    fun inject(estimatesActivityRecyclerAdapter: EstimatesActivityRecyclerAdapter)

}