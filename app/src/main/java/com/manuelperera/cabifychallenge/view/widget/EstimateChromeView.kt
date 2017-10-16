package com.manuelperera.cabifychallenge.view.widget

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import com.manuelperera.cabifychallenge.R
import com.manuelperera.cabifychallenge.domain.objects.Estimate
import com.manuelperera.cabifychallenge.extensions.loadUrl
import kotlinx.android.synthetic.main.item_estimate.view.*

class EstimateChromeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : CardView(context, attrs, defStyleAttr) {

    init {
        init()
    }

    private fun init() {
        inflate(context, R.layout.item_estimate, this)
    }

    fun setCategoryChrome(estimate: Estimate) {
        estimateIconImageView.loadUrl(estimate.vehicleType.icons.regular, R.drawable.ic_taxi_purple)

        estimateVehicleShortNameTextView.text = estimate.vehicleType.shortName
        estimateVehicleEtaTextView.text = estimate.vehicleType.eta.formatted
        estimateVehicleFormattedPriceTextView.text = estimate.priceFormatted

        requestLayout()
    }

}