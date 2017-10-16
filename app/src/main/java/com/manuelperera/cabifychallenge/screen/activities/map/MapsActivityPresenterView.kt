package com.manuelperera.cabifychallenge.screen.activities.map

import android.support.design.widget.FloatingActionButton
import android.widget.EditText
import android.widget.ImageView
import co.develoop.androidcleanarchitecture.screen.presenter.PresenterView
import co.develoop.androidcleanarchitecture.screen.presenter.actions.PresenterClicker
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable

interface MapsActivityPresenterView : PresenterView {

    fun getMapSearchPointAClearButton(): PresenterClicker<ImageView>

    fun getMapSearchPointBClearButton(): PresenterClicker<ImageView>

    fun getMapCalculateEstimatesButton(): PresenterClicker<FloatingActionButton>

    fun getMapSearchPointAEditText(): EditText

    fun getMapSearchPointBEditText(): EditText

    fun clearMapSearchEditText(editText: EditText)

    fun addMarkerOnMap(latLng: LatLng, id: String, title: String, moveCamera: Boolean)

    fun setStreetAddressName(strAddress: String, id: String): Completable

    fun removeMarker(id: String)

    fun showErrorToast(message: String)

    fun routeToEstimates()

}