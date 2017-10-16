package com.manuelperera.cabifychallenge.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.manuelperera.cabifychallenge.R
import com.squareup.picasso.Picasso

fun ViewGroup.inflate(layoutResourceId: Int): View = LayoutInflater.from(context).inflate(layoutResourceId, this, false)

fun convertDpToPixel(dp: Float): Float = dp * (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun convertPixelsToDp(px: Float): Float = px / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun Activity.transitionUpAnimation() {
    overridePendingTransition(R.anim.slide_up_info, R.anim.no_change)
}

fun Activity.transitionDownAnimation() {
    overridePendingTransition(R.anim.no_change, R.anim.slide_down_info)
}

fun ImageView.loadUrl(url: String?, placeholder: Int = R.mipmap.ic_launcher) {
    if (url != null)
        Picasso.with(context)
                .load(url)
                .placeholder(placeholder)
                .into(this)
}