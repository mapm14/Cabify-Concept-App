package com.manuelperera.cabifychallenge.screen.activities.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import co.develoop.androidcleanarchitecture.screen.presenter.actions.PresenterClicker
import co.develoop.reactivepermission.rx2.ReactivePermission
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.manuelperera.cabifychallenge.CabifyChallengeApp
import com.manuelperera.cabifychallenge.R
import com.manuelperera.cabifychallenge.extensions.hideKeyboard
import com.manuelperera.cabifychallenge.extensions.routeToEstimatesActivity
import com.manuelperera.cabifychallenge.screen.activities.map.injection.DaggerMapsActivityComponent
import es.dmoral.toasty.Toasty
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject

class MapsActivity : FragmentActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, MapsActivityPresenterView {

    @Inject
    lateinit var mapsPresenter: MapsActivityPresenter

    companion object {
        val POINT_A = "POINT_A"
        val POINT_B = "POINT_B"
    }

    private val MAP_ACTIVITY_TAG = "MAP_ACTIVITY"

    private var mMap: GoogleMap? = null
    private val hashMapMarkers = HashMap<String, Marker>()

    private lateinit var googleApiClient: GoogleApiClient

    init {
        DaggerMapsActivityComponent.builder().appComponent(CabifyChallengeApp.daggerAppComponent()).build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mapsPresenter.init(this)

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        MapsInitializer.initialize(application)
        mapView.getMapAsync(this)

        initGoogleApiClient()
        configMapSearchEditText()
    }

    private fun initGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
        locationRequest.interval = 10000
        locationRequest.smallestDisplacement = 100f
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            mMap = googleMap
            mMap?.setOnMapLongClickListener(this)
            mMap?.setOnMarkerClickListener(this)
            mMap?.setOnInfoWindowClickListener(this)
            mMap?.uiSettings?.isZoomControlsEnabled = false
            mMap?.uiSettings?.isMyLocationButtonEnabled = true
            mMap?.uiSettings?.isMapToolbarEnabled = true
            mMap?.uiSettings?.isIndoorLevelPickerEnabled = false
            mMap?.uiSettings?.isCompassEnabled = true
            centerMapOnSpain()
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        val permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED && permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            setMyLocationEnabled()
        } else {
            mMap?.isMyLocationEnabled = true
            moveCameraToUserLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ReactivePermission.Builder(this)
                    .addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .addPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe { permissionResults ->
                        if (permissionResults.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && permissionResults.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            mMap?.isMyLocationEnabled = true
                            moveCameraToUserLocation()
                            if (!googleApiClient.isConnected)
                                googleApiClient.connect()
                        }
                    }
        } else {
            mMap?.isMyLocationEnabled = true
            moveCameraToUserLocation()
        }
    }

    private fun moveCameraToUserLocation() {
        val userLocation = getLastKnownUserLocation()
        if (userLocation != null) {
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userLocation.latitude, userLocation.longitude), 17f))
        } else
            centerMapOnSpain()
    }

    private fun centerMapOnSpain() {
        val madridLatLng = LatLng(40.4379332, -3.7495761)
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(madridLatLng, 5.2f))
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownUserLocation(): Location? {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let {
            if (marker.isInfoWindowShown)
                marker.hideInfoWindow()
            else
                marker.showInfoWindow()
            return true
        }
        return false
    }

    override fun onInfoWindowClick(marker: Marker?) {
        marker?.let {
            val dialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
            dialog.setTitle(getString(R.string.remove_stop))
            dialog.setMessage(getString(R.string.question_remove_stop))
            dialog.setPositiveButton(android.R.string.yes, { _, _ ->
                mapsPresenter.removeMarker(marker, hashMapMarkers)
            })
            dialog.setNegativeButton(android.R.string.no, { _, _ -> })
            dialog.show()
        }
    }

    override fun onMapLongClick(latLng: LatLng?) {
        latLng?.let {
            if (hashMapMarkers[POINT_A] == null)
                mapsPresenter.createStopFromLatLng(latLng, POINT_A)
            else if (hashMapMarkers[POINT_B] == null)
                mapsPresenter.createStopFromLatLng(latLng, POINT_B)
        }
    }

    override fun addMarkerOnMap(latLng: LatLng, id: String, title: String, moveCamera: Boolean) {
        mMap?.let {
            val marker = mMap!!.addMarker(MarkerOptions().position(latLng).title(title))
            hashMapMarkers.put(id, marker)
            if (moveCamera)
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    override fun setStreetAddressName(strAddress: String, id: String): Completable =
            Completable.create {
                when (id) {
                    POINT_A -> {
                        mapSearchPointAEditText.setText(strAddress)
                        mapSearchPointAEditText.setSelection(mapSearchPointAEditText.text.length)
                        it.onComplete()
                    }
                    POINT_B -> {
                        mapSearchPointBEditText.setText(strAddress)
                        mapSearchPointBEditText.setSelection(mapSearchPointBEditText.text.length)
                        it.onComplete()
                    }
                }
            }

    override fun getMapSearchPointAClearButton(): PresenterClicker<ImageView> = PresenterClicker(mapSearchPointAClearImageView)

    override fun getMapSearchPointBClearButton(): PresenterClicker<ImageView> = PresenterClicker(mapSearchPointBClearImageView)

    override fun getMapCalculateEstimatesButton(): PresenterClicker<FloatingActionButton> = PresenterClicker(mapCalculateEstimatesButton)

    override fun getMapSearchPointAEditText(): EditText = mapSearchPointAEditText

    override fun getMapSearchPointBEditText(): EditText = mapSearchPointBEditText

    override fun clearMapSearchEditText(editText: EditText) {
        editText.setText("")
    }

    override fun removeMarker(id: String) {
        hashMapMarkers[id]?.remove()
        hashMapMarkers.remove(id)
        when (id) {
            POINT_A -> clearMapSearchEditText(mapSearchPointAEditText)
            POINT_B -> clearMapSearchEditText(mapSearchPointBEditText)
        }
    }

    private fun configMapSearchEditText() {
        mapSearchPointAEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                if (charSequence.isNotEmpty() && mapSearchPointAClearImageView.visibility == View.GONE)
                    mapSearchPointAClearImageView.visibility = View.VISIBLE
                else if (charSequence.isEmpty() && mapSearchPointAClearImageView.visibility == View.VISIBLE)
                    mapSearchPointAClearImageView.visibility = View.GONE

                if (hashMapMarkers[POINT_A] != null) {
                    hashMapMarkers[POINT_A]?.remove()
                    hashMapMarkers.remove(POINT_A)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        mapSearchPointAEditText.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mapsPresenter.createStopFromAddress(textView.text.toString(), POINT_A)
                hideKeyboard()
                return@OnEditorActionListener true
            }
            false
        })

        mapSearchPointBEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                if (charSequence.isNotEmpty() && mapSearchPointBClearImageView.visibility == View.GONE)
                    mapSearchPointBClearImageView.visibility = View.VISIBLE
                else if (charSequence.isEmpty() && mapSearchPointBClearImageView.visibility == View.VISIBLE)
                    mapSearchPointBClearImageView.visibility = View.GONE

                if (hashMapMarkers[POINT_B] != null) {
                    hashMapMarkers[POINT_B]?.remove()
                    hashMapMarkers.remove(POINT_B)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        mapSearchPointBEditText.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mapsPresenter.createStopFromAddress(textView.text.toString(), POINT_B)
                hideKeyboard()
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onConnected(bundle: Bundle?) {
        Log.i(MAP_ACTIVITY_TAG, "OnConnected")
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i(MAP_ACTIVITY_TAG, "Connection suspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i(MAP_ACTIVITY_TAG, "Connection failed")
    }

    override fun onResume() {
        super.onResume()
        if (mapView != null)
            mapView.onResume()

        googleApiClient.connect()
    }

    override fun onPause() {
        super.onPause()
        if (mapView != null)
            mapView.onPause()

        googleApiClient.disconnect()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (mapView != null)
            mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapView != null)
            mapView.onDestroy()
        mapsPresenter.clear()
    }

    override fun showErrorToast(message: String) {
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show()
    }

    override fun routeToEstimates() = routeToEstimatesActivity()

}
