package com.example.privatevanmanagement.Fragments.driver

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.directions.route.*
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.UserActivity
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.sortedList
import com.example.privatevanmanagement.utils.PermissionUtils
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.LocationCallback
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class Driver_Track : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, RoutingListener {

    var pd: ProgressDialog? = null

    lateinit var pick_to_studentID: String
    lateinit var pick_to_studentName: String
    lateinit var flag_picORdrop: String
    //    val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var mainHandler: Handler

    lateinit var polylines: ArrayList<Polyline>


    var userMarker: Marker? = null

    var pick_to_lat by Delegates.notNull<Double>()
    var pick_to_long by Delegates.notNull<Double>()
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
    var getDired: Button? = null
    var arrived: Button? = null
    var originLat: String? = null
    var desLat: String? = null
    var loading: ProgressDialog? = null
    lateinit var polyline: Polyline
    var mRunnable: Runnable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainHandler = Handler()
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
        arrived = rootView.findViewById(R.id.arrived) as Button
        polylines = ArrayList<Polyline>()


        if (Objects.student_modelList.size > 0) {

            pick_to_studentID = Objects.student_modelList.get(0).student_id
            pick_to_studentName = Objects.student_modelList.get(0).student_name
            flag_picORdrop = Objects.student_modelList.get(0).arrival
            pick_to_lat = (Objects.student_modelList.get(0).lat).toDouble()
            pick_to_long = (Objects.student_modelList.get(0).longi).toDouble()
            arrived!!.setText("Arrived on " + Objects.student_modelList.get(0).student_name)
                .toString()


            mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(this)
            databaseReference = FirebaseDatabase.getInstance().reference
            mAuth = FirebaseAuth.getInstance()
            isOnline = FirebaseDatabase.getInstance().reference.child(".info/connected")
            currentUserRef = FirebaseDatabase.getInstance().getReference(Objects.driverLatLongTable)
            isOnline!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //We will remove value from Service Providers LatLng when Service Provider Disconnected
//                currentUserRef!!.onDisconnect().removeValue()
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            geoFire = GeoFire(currentUserRef)
            setUpLocation()

            arrived?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    if (!arrived!!.text.equals("End Ride")) {

                        val ref =
                            Objects.getFirebaseInstance().reference.child("StudentDetails")
                                .child(pick_to_studentID).child("arrival")
                        ref.setValue("picked")
                        Objects.student_modelList.removeAt(0) // got to next index number

                        if (Objects.student_modelList.size > 0) {


                            pick_to_studentID = Objects.student_modelList.get(0).student_id
                            pick_to_studentName = Objects.student_modelList.get(0).student_name
                            flag_picORdrop = Objects.student_modelList.get(0).arrival
                            pick_to_lat = Objects.student_modelList.get(0).lat.toDouble()
                            pick_to_long = Objects.student_modelList.get(0).longi.toDouble()
                            arrived!!.setText("Arrived on " + Objects.student_modelList.get(0).student_name)

                        } else if (Objects.student_modelList.size == 0) {
                            arrived!!.setText("End Ride")
                            pick_to_lat = 0.0
                            pick_to_lat = 0.0
                            googleMap!!.clear()
                        }
                    } else { // for end ride button to reset all data
                        pd = ProgressDialog(context)
                        pd!!.setMessage("Please Wait")
                        pd!!.setCancelable(false)
                        pd!!.show()
                        reset()
                    }


                }
            })


/*
            mRunnable = Runnable {
                mainHandler.postDelayed(mRunnable, 5000)
                getDirection()
            }
*/

            mainHandler.postDelayed(object : Runnable {
                override fun run() {
                    if (Objects.student_modelList.size == 0)
                        mainHandler.removeCallbacks(this)
                    else {
                        getDirection()
                        mainHandler.postDelayed(this, 4000)
                    }
                }
            }, 8000)

            /* getDired = rootView.findViewById(R.id.getStudent) as Button
             getDired?.setOnClickListener(object : View.OnClickListener {
                 override fun onClick(v: View?) {
th

                     var geoUser =
                         GeoFire(FirebaseDatabase.getInstance().getReference(Objects.studentLatLongTable))
 //                geoUser.getLocation("GIDBebOLzwU0RawUKVqHG7lfKQX2", object : LocationCallback {
                     geoUser.getLocation(pick_to_studentID, object : LocationCallback {
                         override fun onLocationResult(key: String?, location: GeoLocation?) {

                             if (location != null) {
 *//*
                        val userData =
                            FirebaseDatabase.getInstance().getReference("StudentDetails")
                                .child(pick_to_studentID)
                        userData.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val userModel = dataSnapshot.getValue(StudentDetail_Model::class.java)
*//*
*//*
                                if (userMarker != null) {
                                    userMarker!!.remove()
                                    userMarker == null
                                }
                                if (userMarker == null) {
*//*
                                googleMap!!.clear()
                                val userMarker2 = googleMap?.addMarker(
                                    MarkerOptions().title(pick_to_studentName)
                                        .position(LatLng(location.latitude, location.longitude))
                                        .icon(
                                            BitmapDescriptorFactory
                                                .fromResource(R.drawable.pin)
                                        )
                                )
//                                 }
                            }

*//*                            override fun onCancelled(p0: DatabaseError) {
                            }
                        })

                    } *//*
                            else {
                                geoFire.setLocation(pick_to_studentID, GeoLocation(
                                    pick_to_lat
                                    , pick_to_long
                                ), object : GeoFire.CompletionListener {
                                    override fun onComplete(
                                        key: String?,
                                        error: DatabaseError?
                                    ) {
                                        googleMap!!.clear()
                                        val userMarker2 = googleMap?.addMarker(
                                            MarkerOptions().title(pick_to_studentName)
                                                .position(LatLng(pick_to_lat, pick_to_long))
                                                .icon(
                                                    BitmapDescriptorFactory
                                                        .fromResource(R.drawable.pin)
                                                )
                                        )
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
            })*/

        } else {
            arrived!!.visibility = View.GONE
        }
    }

    fun reset() {
/*
        reset data here according to Pick or Dropped
*/

        //reset van data
        val ref0 = Objects.getFirebaseInstance().reference.child("AddVan")
            .child(Objects.getDriverDetailInstance().driver_van_id)
        val updates0 = HashMap<String, Any>()

        updates0["assign_DriverId"] = ""
        updates0["assign_DriverName"] = ""
        updates0["assign_Status"] = ""
        ref0.updateChildren(updates0)


        //reset driver data
        val ref2 = Objects.getFirebaseInstance().reference.child("DriverDetails")
            .child(Objects.UserID.Globaluser_ID)
        val updates2 = HashMap<String, Any>()
        updates2["assigned_status"] = ""
        updates2["van_number"] = ""
        updates2["driver_van_id"] = ""
        ref2.updateChildren(updates2)

        val ref3 = Objects.getFirebaseInstance().reference.child("scheduled_list")
            .child(Objects.UserID.Globaluser_ID)
        ref3.removeValue()

        // refresh list of all student one more time and then reset data
        refreshlist()
    }

    fun refreshlist() {
        val myRef = Objects.getFirebaseInstance().getReference()
        val query = myRef.child("StudentDetails").orderByChild("driver_id")
            .equalTo(Objects.UserID.Globaluser_ID)

        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Objects.sortedList.clear()
                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(StudentDetail_Model::class.java)!!
                    // to reset all data we use another dummy list name as sorted
                    if (!postSnapshot.child("arrival").value!!.equals(""))// agr arrival status sirf empty na ho tooooo
                    {
                        sortedList.add(listDataRef)
                    }

                }
                resetStudent()
            }

            override fun onCancelled(p0: DatabaseError) {
                pd!!.dismiss()
            }

        })
    }

    private fun resetStudent() {
        // reset all student data who have been allocated this van
        for (i in 0 until sortedList.size) {
            val ref = Objects.getFirebaseInstance().reference.child("StudentDetails")
                .child(sortedList.get(i).student_id)
            if (sortedList.get(i).arrival.equals("picked", ignoreCase = true)) {
                val updates = HashMap<String, Any>()
                updates["arrival"] = "dropped"
                updates["allocated_van"] = ""
                updates["driver_id"] = ""
                updates["driver_name"] = ""
                ref.updateChildren(updates)
            } else if (sortedList.get(i).arrival.equals("dropped", ignoreCase = true)) {
                val updates = HashMap<String, Any>()
                updates["arrival"] = ""
                updates["allocated_van"] = ""
                updates["driver_id"] = ""
                updates["driver_name"] = ""
                ref.updateChildren(updates)
            }
        }
        googleMap!!.clear()
        Objects.driverDetail_model.assigned_status = ""
        Objects.getDriverDetailInstance().van_number = ""
        Objects.getDriverDetailInstance().driver_van_id = ""
        pd!!.dismiss()
        var mainActivity: UserActivity = activity as UserActivity
        mainActivity!!.replaceFragmentUserActivity(Driver_home(), null)

    }


    private fun getDirection() {
        var geoUser =
            GeoFire(FirebaseDatabase.getInstance().getReference(Objects.studentLatLongTable))
//                geoUser.getLocation("GIDBebOLzwU0RawUKVqHG7lfKQX2", object : LocationCallback {
        geoUser.getLocation(pick_to_studentID, object : LocationCallback {
            override fun onLocationResult(key: String?, location: GeoLocation?) {

                if (location != null) {
                    if (mUserLocationMarker != null) {
                        mUserLocationMarker?.remove()
                    }
                    // googleMap!!.clear()
                    mUserLocationMarker = googleMap?.addMarker(
                        MarkerOptions().title(pick_to_studentName)
                            .position(LatLng(location.latitude, location.longitude))
                            .icon(
                                BitmapDescriptorFactory
                                    .fromResource(R.drawable.pin)
                            )
                    )
                    getRouteToMarker(LatLng(location.latitude, location.longitude))

                    /*  FetchURL(requireActivity()).execute(
                      getDirectionsUrl(
                          LatLng(
                              Objects.driverLastLocation.latitude,
                              Objects.driverLastLocation.longitude
                          ),
                          LatLng(location.latitude, location.longitude)
                      ), "driving"
                  )*/

//                                 }
                }

/*                            override fun onCancelled(p0: DatabaseError) {
                            }
                        })

                    } */
                else {
                    geoFire.setLocation(pick_to_studentID, GeoLocation(
                        pick_to_lat
                        , pick_to_long
                    ), object : GeoFire.CompletionListener {
                        override fun onComplete(
                            key: String?,
                            error: DatabaseError?
                        ) {
                            if (mUserLocationMarker != null) {
                                mUserLocationMarker?.remove()
                            }
                            //                            googleMap!!.clear()
                            mUserLocationMarker = googleMap?.addMarker(
                                MarkerOptions().title(pick_to_studentName)
                                    .position(LatLng(pick_to_lat, pick_to_long))
                                    .icon(
                                        BitmapDescriptorFactory
                                            .fromResource(R.drawable.pin)
                                    )
                            )
                            if (pick_to_lat != 0.0 && pick_to_long != 0.0)
                                getRouteToMarker(LatLng(pick_to_lat, pick_to_long))
                            /* FetchURL(requireActivity()).execute(
                             getDirectionsUrl(
                                 LatLng(
                                     Objects.driverLastLocation.latitude,
                                     Objects.driverLastLocation.longitude
                                 ),
                                 LatLng(pick_to_lat, pick_to_long)
                             ), "driving"
                         )*/
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
            googleMap?.setIndoorEnabled(true)
            googleMap?.setBuildingsEnabled(true)
            googleMap?.getUiSettings()?.setZoomControlsEnabled(false)

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


    override fun onRoutingCancelled() {
    }

    override fun onRoutingStart() {
    }

    override fun onRoutingFailure(e: RouteException?) {
        if (e != null) {
            Toast.makeText(activity, "Error: " + e.message.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onRoutingSuccess(route: ArrayList<Route>?, i: Int) {

        if (polylines.size > 0) {
            for (poly in polylines) {
                poly.remove();
            }
        }
        polylines = ArrayList<Polyline>();

        for (i in 0 until route!!.size) {
            var polyOptions: PolylineOptions = PolylineOptions();
            polyOptions.color(Color.DKGRAY);
            polyOptions.width(15 + i * 30.toFloat())
            polyOptions.addAll(route!!.get(i).getPoints());
            var polyline: Polyline = googleMap!!.addPolyline(polyOptions);
            (polylines as ArrayList<Polyline>).add(polyline);

/*
            Toast.makeText(
                activity,
                "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(
                    i
                ).getDurationValue(),
                Toast.LENGTH_SHORT
            ).show();
*/
        }
    }


    private fun getRouteToMarker(pickuplatLng: LatLng) {
        if (activity != null) {
            var routing: Routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .key(getString(R.string.google_maps_key))
                .waypoints(
                    LatLng(
                        Objects.driverLastLocation.latitude,
                        Objects.driverLastLocation.longitude
                    ), pickuplatLng
                )
                .build();
            routing.execute();
        }
    }


}
