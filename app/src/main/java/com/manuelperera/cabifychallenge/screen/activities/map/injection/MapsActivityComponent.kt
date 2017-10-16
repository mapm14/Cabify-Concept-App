package com.manuelperera.cabifychallenge.screen.activities.map.injection

import com.manuelperera.cabifychallenge.injection.AppComponent
import com.manuelperera.cabifychallenge.screen.activities.map.MapsActivity
import dagger.Component

@MapsActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(MapsActivityModule::class))
interface MapsActivityComponent {

    fun inject(mapsActivity: MapsActivity)

}