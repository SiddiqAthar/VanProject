package com.example.privatevanmanagement.Fragments.student

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.models.DriverDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.PermissionUtils
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.LocationCallback
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class Student_TrackVans : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    lateinit var rootView: View
    var googleMap: GoogleMap? = null
    internal lateinit var mLocationRequest: LocationRequest
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal var mCurrLocationMarker: Marker? = null
    var mapFragment: SupportMapFragment? = null
    lateinit var geoFire: GeoFire
    var mAuth: FirebaseAuth? = null
    var firebaseUsers: FirebaseUser? = null
    var databaseReference: DatabaseReference? = null
    var isOnline: DatabaseReference? = null
    var currentUserRef: DatabaseReference? = null
    var getDriverBtn: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_student_track_vans, container, false)

        activity?.setTitle("Track Your Van")
        initViews(rootView);
        return rootView;
    }

    private fun initViews(rootView: View?) {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        databaseReference = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        isOnline = FirebaseDatabase.getInstance().reference.child(".info/connected")
        currentUserRef = FirebaseDatabase.getInstance().getReference(Objects.studentLatLongTable)
        isOnline!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //We will remove value from Service Providers LatLng when Service Provider Disconnected
                currentUserRef!!.onDisconnect().removeValue()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        geoFire = GeoFire(currentUserRef)
        setUpLocation()
        getDriverBtn = rootView?.findViewById(R.id.getStudent) as Button
        getDriverBtn?.setText("Get Driver")
        getDriverBtn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val driverLocation =
                    GeoFire(FirebaseDatabase.getInstance().getReference(Objects.driverLatLongTable))
                driverLocation.getLocation("VNrkHgtg95WeDWq3sQ3VXLB7gkk1",
                    object : LocationCallback {
                        override fun onLocationResult(key: String?, location: GeoLocation?) {
                            if (location != null) {

                                FirebaseDatabase.getInstance().getReference("DriverDetails")
                                    .child("VNrkHgtg95WeDWq3sQ3VXLB7gkk1")
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(p0: DataSnapshot) {
                                            val driverInfo =
                                                p0.getValue(DriverDetail_Model::class.java)
                                            googleMap?.addMarker(
                                                MarkerOptions().icon(
                                                    BitmapDescriptorFactory
                                                        .defaultMarker(
                                                            BitmapDescriptorFactory.HUE_CYAN
                                                        )
                                                ).position(
                                                    LatLng(
                                                        location.latitude,
                                                        location.longitude
                                                    )
                                                )
                                                    .title(driverInfo!!.driver_name)
                                            )


                                        }

                                        override fun onCancelled(p0: DatabaseError) {
                                        }
                                    })

                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError?) {

                        }
                    })
            }
        })
    }

    private fun setUpLocation() {
        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            buildGoogleApiClient()
            createLocationRequest()
        } else {
            PermissionUtils.requestLocationPermissions(activity, 101)
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(5000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setSmallestDisplacement(10.0f)


    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(context!!).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
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
        displayLocation()

    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient?.connect()

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(location: Location?) {
        Objects.studentLastLocation = location
        displayLocation()

    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null)
            mGoogleApiClient?.connect()
    }

    private fun displayLocation() {
        if (!PermissionUtils.hasLocationPermissionGranted(context)) {
            return
        }
        if (mGoogleApiClient!!.isConnected) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
            Objects.studentLastLocation =
                LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            if (Objects.studentLastLocation != null) {
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker?.remove()
                }
                mCurrLocationMarker?.remove()
                mCurrLocationMarker = googleMap?.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            Objects.studentLastLocation.latitude,
                            Objects.studentLastLocation.longitude
                        )
                    ).title("Your Are here")
                )
                googleMap!!.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            Objects.studentLastLocation.latitude,
                            Objects.studentLastLocation.longitude
                        ), 15.0f
                    )
                )

                geoFire.setLocation(FirebaseAuth.getInstance().currentUser?.uid, GeoLocation(
                    Objects.studentLastLocation.latitude
                    , Objects.studentLastLocation.longitude
                ), object : GeoFire.CompletionListener {
                    override fun onComplete(key: String?, error: DatabaseError?) {

                    }
                })


            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpLocation()
                displayLocation()
            }
        }
    }

}
