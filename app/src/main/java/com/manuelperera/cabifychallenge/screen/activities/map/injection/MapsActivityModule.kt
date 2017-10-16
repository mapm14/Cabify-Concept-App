package com.manuelperera.cabifychallenge.screen.activities.map.injection

import com.manuelperera.cabifychallenge.screen.activities.map.MapsActivityPresenter
import com.manuelperera.cabifychallenge.usecase.estimate.SetTravelUseCase
import dagger.Module
import dagger.Provides

@Module
class MapsActivityModule {

    @Provides
    @MapsActivityScope
    fun mapsActivityPresenter(setTravelUseCase: SetTravelUseCase): MapsActivityPresenter =
            MapsActivityPresenter(setTravelUseCase)

}