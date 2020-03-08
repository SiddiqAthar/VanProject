package com.example.privatevanmanagement.Fragments.student

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects.location
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*


class Student_TrackVans : Fragment(), OnMapReadyCallback {

  var locationManager: LocationManager?=null
  var locationListener: LocationListener?=null
    lateinit var rootView: View
    var mMapView: MapView? = null
    lateinit var googleMap : GoogleMap
    internal lateinit var mLastLocation: Location
    internal lateinit var mLocationResult: LocationRequest
    internal lateinit var mLocationCallback: LocationCallback
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    internal var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_student_track_vans, container, false)

        activity?.setTitle("Track Your Van")


        mMapView = rootView.findViewById(R.id.mapView) as MapView
        mMapView!!.onCreate(savedInstanceState)

        mMapView!!.onResume() // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        mMapView!!.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            // For showing a move to my location button
            googleMap.setMyLocationEnabled(true)

//            // For dropping a marker at a point on the Map
//            val sydney = LatLng(-34.0, 151.0)
//            mMap.addMarker(MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"))
//
//            // For zooming automatically to the location of the marker
//            val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        })


        return rootView;
    }




    fun updateMap(location: Location) {
        val userlocation = LatLng(location.latitude, location.longitude)
//        position_lat = location.latitude.toString()
//        position_lang = location.longitude.toString()
         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 15f))

        val latLng = LatLng(location.latitude, location.longitude)

        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = googleMap!!.addMarker(markerOptions)

    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this!!.context!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) === PackageManager.PERMISSION_GRANTED
                ) {


                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0f,
                        locationListener
                    )
                    val lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    updateMap(lastLocation!!)
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
       this.googleMap = googleMap


        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                 updateMap(location)

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            }

            override fun onProviderEnabled(provider: String) {

            }

            override fun onProviderDisabled(provider: String) {

            }
        }
        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(
                    this!!.context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this!!.context!!, Manifest.permission.ACCESS_COARSE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                 return
            }
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        } else {
            if (ContextCompat.checkSelfPermission(
                    this!!.context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    (this!!.context as Activity?)!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    100,
                    10f,
                    locationListener
                )
                val lastLocation =
                    locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (lastLocation != null) {
                     updateMap(lastLocation!!)
                }
                if (lastLocation == null) {

                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        100,
                        10f,
                        locationListener
                    )
                    val lastLocation2 =
                        locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (lastLocation2 != null) {
                         updateMap(lastLocation2!!)
                    }
                }
            }
        }

     }



}
