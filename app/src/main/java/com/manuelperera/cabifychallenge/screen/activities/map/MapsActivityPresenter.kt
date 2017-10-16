package com.manuelperera.cabifychallenge.screen.activities.map

import android.location.Address
import android.location.Geocoder
import android.util.Log
import co.develoop.androidcleanarchitecture.screen.presenter.Presenter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.manuelperera.cabifychallenge.CabifyChallengeApp
import com.manuelperera.cabifychallenge.R
import com.manuelperera.cabifychallenge.domain.objects.Contact
import com.manuelperera.cabifychallenge.domain.objects.Stop
import com.manuelperera.cabifychallenge.domain.objects.Travel
import com.manuelperera.cabifychallenge.usecase.estimate.SetTravelUseCase
import java.util.*

class MapsActivityPresenter(private val setTravelUseCase: SetTravelUseCase) : Presenter<MapsActivityPresenterView>() {

    private val MAP_ACTIVITY_PRESENTER_TAG = "MAP_ACTIVITY_PRESENTER"
    private val context = CabifyChallengeApp.mDaggerAppComponent.provideContext()

    private var stopA: Stop? = null
    private var stopB: Stop? = null

    override fun init() {
        bindMapSearchPointAClickButton()
        bindMapSearchPointBClickButton()
        bindMapCalculateEstimatesButton()
    }

    private fun setTravelAngGetEstimates() {
        if (getStops() != null)
            addSubscription(setTravelUseCase.bind(SetTravelUseCase.Params(Travel(getStops()!!, null))).doOnComplete {
                view.routeToEstimates()
            }.subscribe())
        else
            view.showErrorToast(context.getString(R.string.select_two_stops))
    }

    private fun getStops(): List<Stop>? {
        return if (stopA != null && stopB != null)
            listOf(stopA!!, stopB!!)
        else null
    }

    fun createStopFromLatLng(latLng: LatLng, id: String) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>

        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses.isEmpty()) {
            view.showErrorToast(context.getString(R.string.no_street_address_result))
            return
        } else
            createStopAndAddMarker(addresses, latLng, id, false)
    }

    fun createStopFromAddress(strAddress: String, id: String) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>

        addresses = geocoder.getFromLocationName(strAddress, 1)
        if (addresses.isEmpty()) {
            view.showErrorToast(context.getString(R.string.no_street_address_result))
            return
        } else
            createStopAndAddMarker(addresses, LatLng(addresses[0].latitude, addresses[0].longitude), id)
    }

    private fun createStopAndAddMarker(addresses: List<Address>, latLng: LatLng, id: String, moveCamera: Boolean = true) {
        val address = addresses[0].getAddressLine(0)
        val city = addresses[0].locality
        val country = addresses[0].countryName
        val knownName = addresses[0].featureName
        val thoroughfare = addresses[0].thoroughfare
        //        val state = addresses[0].adminArea
        //        val postalCode = addresses[0].postalCode

        try {
            when (id) {
                MapsActivity.POINT_A -> {
                    stopA = Stop(listOf(latLng.latitude, latLng.longitude), thoroughfare, address, knownName, city, country, "instructions", Contact("Manuel", "+34", "000111"))
                    view.addMarkerOnMap(latLng, MapsActivity.POINT_A, context.getString(R.string.start_point), moveCamera)
                    view.setStreetAddressName(address, MapsActivity.POINT_A)
                }
                MapsActivity.POINT_B -> {
                    stopB = Stop(listOf(latLng.latitude, latLng.longitude), thoroughfare, address, knownName, city, country, "instructions", Contact("Nelly", "+58", "111999"))
                    view.addMarkerOnMap(latLng, MapsActivity.POINT_B, context.getString(R.string.end_point), moveCamera)
                    view.setStreetAddressName(address, MapsActivity.POINT_B)
                }
            }
        } catch (e: Exception) {
            Log.i(MAP_ACTIVITY_PRESENTER_TAG, e.localizedMessage)
            view.showErrorToast(context.getString(R.string.no_street_address_result))
            return
        }
    }

    fun removeMarker(marker: Marker, hashMap: HashMap<String, Marker>) {
        var id = ""
        hashMap.entries
                .filter { it.value == marker }
                .forEach { id = it.key }

        when (id) {
            MapsActivity.POINT_A -> {
                view.removeMarker(MapsActivity.POINT_A)
                stopA = null
            }
            MapsActivity.POINT_B -> {
                view.removeMarker(MapsActivity.POINT_B)
                stopB = null
            }
        }
    }

    private fun bindMapSearchPointAClickButton() {
        addSubscription(view.getMapSearchPointAClearButton().bind().subscribe {
            view.clearMapSearchEditText(view.getMapSearchPointAEditText())
            view.removeMarker(MapsActivity.POINT_A)
            stopA = null
        })
    }

    private fun bindMapSearchPointBClickButton() {
        addSubscription(view.getMapSearchPointBClearButton().bind().subscribe {
            view.clearMapSearchEditText(view.getMapSearchPointBEditText())
            view.removeMarker(MapsActivity.POINT_B)
            stopB = null
        })
    }

    private fun bindMapCalculateEstimatesButton() {
        addSubscription(view.getMapCalculateEstimatesButton().bind().subscribe {
            setTravelAngGetEstimates()
        })
    }

}