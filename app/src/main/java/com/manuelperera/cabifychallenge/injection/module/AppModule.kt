package com.manuelperera.cabifychallenge.injection.module

import android.content.Context
import com.manuelperera.cabifychallenge.CabifyChallengeApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: CabifyChallengeApp) {

    @Provides
    @Singleton
    fun context(): Context = app.applicationContext

}