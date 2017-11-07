package com.manuelperera.cabifychallenge.extensions

import android.app.Activity
import android.content.Intent
import com.manuelperera.cabifychallenge.R
import com.manuelperera.cabifychallenge.screen.activities.estimates.EstimatesActivity

fun Activity.transitionUpAnimation() {
    overridePendingTransition(R.anim.slide_up_info, R.anim.no_change)
}

fun Activity.transitionDownAnimation() {
    overridePendingTransition(R.anim.no_change, R.anim.slide_down_info)
}

fun Activity.routeToEstimatesActivity() {
    startActivity(Intent(this, EstimatesActivity::class.java))
}