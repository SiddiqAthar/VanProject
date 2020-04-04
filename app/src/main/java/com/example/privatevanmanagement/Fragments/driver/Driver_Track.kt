package com.example.privatevanmanagement.Fragments.driver

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.retrofit.RetrofitClient
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.PermissionUtils
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.LocationCallback
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_allocate_to_student.*
import okhttp3.ResponseBody
import retrofit2.Response


class Driver_Track : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {
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
    var getDired: Button? = null
    var originLat: String? = null
    var desLat: String? = null
    var loading: ProgressDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_student_track_vans, container, false)
        initViews(rootView);
        return rootView;
    }

    private fun initViews(rootView: View) {
        loading = ProgressDialog(context)
        loading?.setMessage("Please wait")
        loading?.setTitle("Loading")
        loading?.setCancelable(false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        databaseReference = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        isOnline = FirebaseDatabase.getInstance().reference.child(".info/connected")
        currentUserRef = FirebaseDatabase.getInstance().getReference(Objects.driverLatLongTable)
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
        getDired = rootView.findViewById(R.id.getStudent) as Button
        getDired?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {


                var geoUser =
                    GeoFire(FirebaseDatabase.getInstance().getReference(Objects.studentLatLongTable))
                geoUser.getLocation("GIDBebOLzwU0RawUKVqHG7lfKQX2", object : LocationCallback {
                    override fun onLocationResult(key: String?, location: GeoLocation?) {

                        if (location != null) {
                            val userData =
                                FirebaseDatabase.getInstance().getReference("StudentDetails")
                                    .child("GIDBebOLzwU0RawUKVqHG7lfKQX2")
                            userData.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val userModel =
                                        dataSnapshot.getValue(StudentDetail_Model::class.java)

                                    val userMarker = googleMap?.addMarker(
                                        MarkerOptions().title(userModel!!.student_name)
                                            .position(LatLng(location.latitude, location.longitude))
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_CYAN
                                                )
                                            )
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


//                var geoUser =
//                    GeoFire(FirebaseDatabase.getInstance().getReference(Objects.studentLatLongTable))
//                geoUser.getLocation(
//                    "GIDBebOLzwU0RawUKVqHG7lfKQX2",
//                    object : LocationCallback {
//                        override fun onLocationResult(key: String?, location: GeoLocation?) {
//                            Log.i(
//                                "url",
//                                getDirectionsUrl(
//                                    LatLng(
//                                        Objects.driverLastLocation.latitude,
//                                        Objects.driverLastLocation.longitude
//                                    ), LatLng(location!!.latitude, location.longitude)
//                                )
//                            )


//                            if (location != null) {
//                                var userLat =
//                                    location!!.latitude.toString() + "," + location.longitude.toString()

//                                loading?.show()
//                                RetrofitClient.getInstance().getDirection(
//                                    originLat,
//                                    userLat,
//                                    context!!.resources.getString(R.string.google_maps_key),
//                                    object : RetrofitClient.CallbackGenric<Any> {
//                                        override fun onResult(z: Boolean, response: Response<*>?) {
//                                            loading?.dismiss()
//                                            if (z) {
//                                                if (response?.body() is ResponseBody) {
//                                                    val stringResponse =
//                                                        (response.body() as ResponseBody)?.string()
////                                            val stringResponse = response.body().string()
//                                                    Log.i("res", stringResponse)
//
//                                                }
//                                            }
//                                        }
//
//                                        override fun onError(error: String?) {
//                                            loading?.dismiss()
//                                        }
//                                    })
//                            }
//
//
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError?) {
////                        userLat = ""
//
//                        }
//                    })


////                desLat = UserLatLong()
//                if (desLat != null) {
//
//                }


            }
        })

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
            Objects.driverLastLocation =
                LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            if (Objects.driverLastLocation != null) {
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker?.remove()
                }
                originLat =
                    Objects.driverLastLocation.latitude.toString() + "," + Objects.driverLastLocation.longitude.toString()
                mCurrLocationMarker?.remove()
                mCurrLocationMarker = googleMap?.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            Objects.driverLastLocation.latitude,
                            Objects.driverLastLocation.longitude
                        )
                    ).title("Your Are here")
                )
                googleMap!!.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            Objects.driverLastLocation.latitude,
                            Objects.driverLastLocation.longitude
                        ), 15.0f
                    )
                )

                geoFire.setLocation(FirebaseAuth.getInstance().currentUser?.uid, GeoLocation(
                    Objects.driverLastLocation.latitude
                    , Objects.driverLastLocation.longitude
                ), object : GeoFire.CompletionListener {
                    override fun onComplete(key: String?, error: DatabaseError?) {

                    }
                })


            }
        }
    }

    private fun UserLatLong(): String? {
        var userLat: String? = null
        var geoUser =
            GeoFire(FirebaseDatabase.getInstance().getReference(Objects.studentLatLongTable))
        geoUser.getLocation("GIDBebOLzwU0RawUKVqHG7lfKQX2", object : LocationCallback {
            override fun onLocationResult(key: String?, location: GeoLocation?) {
                userLat = location!!.latitude.toString() + "," + location.longitude.toString()

            }

            override fun onCancelled(databaseError: DatabaseError?) {
                userLat = ""

            }
        })
//        geoFire.getLocation(
//            FirebaseDatabase.getInstance().getReference(Objects.studentLatLongTable).child(
//                "GIDBebOLzwU0RawUKVqHG7lfKQX2"
//            ).toString(), object : LocationCallback {
//                override fun onLocationResult(key: String?, location: GeoLocation?) {
//                    userLat = location!!.latitude.toString() + "," + location.longitude.toString()
//                }
//
//                override fun onCancelled(databaseError: DatabaseError?) {
//                    userLat = ""
//                }
//            }
//        )
        return userLat

    }


    private fun startLocationUpdates() {
        if (PermissionUtils.hasLocationPermissionGranted(context)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
        } else {
            PermissionUtils.requestLocationPermissions(activity, 101)
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

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(context!!).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
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
        Objects.driverLastLocation = location
        displayLocation()


    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null)
            mGoogleApiClient?.connect()
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        val sensor = "sensor=false";
        val mode = "mode=driving";

        // Building the parameters to the web service
        val parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        val output = "json";

        // Building the url to the web service
        val url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }


}
