package com.manuelperera.cabifychallenge.extensions

import android.app.Activity
import android.content.Intent
import com.manuelperera.cabifychallenge.screen.activities.estimates.EstimatesActivity

fun Activity.routeToEstimatesActivity() {
    startActivity(Intent(this, EstimatesActivity::class.java))
    transitionUpAnimation()
}