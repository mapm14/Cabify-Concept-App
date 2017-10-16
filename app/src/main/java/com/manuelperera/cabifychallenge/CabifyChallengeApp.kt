package com.manuelperera.cabifychallenge

import android.app.Application
import android.support.v4.content.ContextCompat
import com.manuelperera.cabifychallenge.injection.AppComponent
import com.manuelperera.cabifychallenge.injection.DaggerAppComponent
import com.manuelperera.cabifychallenge.injection.module.AppModule
import com.manuelperera.cabifychallenge.view.Fonts
import es.dmoral.toasty.Toasty

class CabifyChallengeApp : Application() {

    companion object {
        lateinit var mDaggerAppComponent: AppComponent
        fun daggerAppComponent() = mDaggerAppComponent
    }

    override fun onCreate() {
        super.onCreate()
        mDaggerAppComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        Toasty.Config.getInstance()
                .setErrorColor(ContextCompat.getColor(this, R.color.colorPrimaryLight))
                .setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .tintIcon(true)
                .setToastTypeface(Fonts.values()[0].getTypeface(this)!!)
                .apply()
//                .setInfoColor(@ColorInt int infoColor) // optional
//                .setSuccessColor(@ColorInt int successColor) // optional
//                .setWarningColor(@ColorInt int warningColor) // optional
    }
}