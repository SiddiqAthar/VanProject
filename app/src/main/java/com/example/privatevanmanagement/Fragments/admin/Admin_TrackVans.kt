package com.example.privatevanmanagement.Fragments.admin

import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.trackDriverbyVan
import com.example.privatevanmanagement.utils.PermissionUtils
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.LocationCallback
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class Admin_TrackVans : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    val mainHandler = Handler(Looper.getMainLooper())

    var mMapView: MapView? = null
    lateinit var rootView: View
    var googleMap: GoogleMap? = null
    internal lateinit var mLocationRequest: LocationRequest
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal var mCurrLocationMarker: Marker? = null
    internal var mUserLocationMarker: Marker? = null

    var mapFragment: SupportMapFragment? = null
    lateinit var geoFire: GeoFire
    var mAuth: FirebaseAuth? = null
    var firebaseUsers: FirebaseUser? = null
    var databaseReference: DatabaseReference? = null
    var isOnline: DatabaseReference? = null
    var currentUserRef: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_admin__track_vans, container, false)
        activity?.setTitle("Track Vans")
        initViews(rootView);

        return rootView;
    }

    private fun initViews(rootView: View?) {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        databaseReference = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
//        isOnline = FirebaseDatabase.getInstance().reference.child(".info/connected")
        currentUserRef = FirebaseDatabase.getInstance().getReference(Objects.driverLatLongTable)
//        isOnline!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                //We will remove value from Service Providers LatLng when Service Provider Disconnected
//                currentUserRef!!.onDisconnect().removeValue()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        })
        geoFire = GeoFire(currentUserRef)
        if (trackDriverbyVan.size > 0) {
            mainHandler.postDelayed(object : Runnable {
                override fun run() {
                    getDirection()
                    mainHandler.postDelayed(this, 2000)
                }
            }, 4000)
        }
    }


    private fun getDirection() {
        val driverLocation =
            GeoFire(FirebaseDatabase.getInstance().getReference(Objects.driverLatLongTable))
        driverLocation.getLocation(
            trackDriverbyVan.get(0).driver_id,
            object : LocationCallback {
                override fun onLocationResult(key: String?, location: GeoLocation?) {
                    if (location != null) {
                        if (mUserLocationMarker != null) {
                            mUserLocationMarker?.remove()
                        }

                        mUserLocationMarker = googleMap?.addMarker(
                            MarkerOptions().icon(
                                BitmapDescriptorFactory
                                    .fromResource(R.drawable.busicon)
                            ).position(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                )
                            )
                                .title(trackDriverbyVan.get(0).driver_name)
                        )
                    }
                    /*else {
                   geoFire.setLocation(trackDriverbyVan.get(0).driver_id, GeoLocation(
                       trackDriverbyVan.get(0).driver_lat.toDouble()
                       , trackDriverbyVan.get(0).driver_longi.toDouble()
                   ), object : GeoFire.CompletionListener {
                       override fun onComplete(
                           key: String?,
                           error: DatabaseError?
                       ) {
                           if (mUserLocationMarker != null) {
                               mUserLocationMarker?.remove()
                           }
                           mUserLocationMarker = googleMap?.addMarker(
                               MarkerOptions().title(trackDriverbyVan.get(0).driver_name)
                                   .position(
                                       LatLng(
                                           Objects.driverDetail_model.driver_lat.toDouble()
                                           ,
                                           Objects.driverDetail_model.driver_longi.toDouble()
                                       )
                                   )
                                   .icon(
                                       BitmapDescriptorFactory
                                           .fromResource(R.drawable.pin)
                                   )
                           )
                       }
                   })

                   }*/
                }

                override fun onCancelled(databaseError: DatabaseError?) {

                }
            })

    }


    override fun onMapReady(map: GoogleMap?) {
        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            this.googleMap = map
            googleMap?.isMyLocationEnabled = true
            googleMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
            googleMap?.setTrafficEnabled(false)
            googleMap?.setIndoorEnabled(false)
            googleMap?.setBuildingsEnabled(false)
            googleMap?.getUiSettings()?.setZoomControlsEnabled(true)


//            displayLocation()

        }
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient?.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(p0: Location?) {
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null)
            mGoogleApiClient?.connect()
    }


    private fun setUpLocation() {
        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            buildGoogleApiClient()
            createLocationRequest()
        } else {
            PermissionUtils.requestLocationPermissions(activity, 101)
        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient =
            GoogleApiClient.Builder(context!!).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(5000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setSmallestDisplacement(10.0f)

    }

}
