package com.example.privatevanmanagement.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.privatevanmanagement.ChatModule.Model.ChatList
import com.example.privatevanmanagement.Fragments.ChangePassword
import com.example.privatevanmanagement.Fragments.driver.Driver_home
import com.example.privatevanmanagement.Fragments.student.Student_home
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.models.DriverDetail_Model
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*


class UserActivity : BaseActivity() {

    var pd: ProgressDialog? = null
    var AUTOCOMPLETE_REQUEST_CODE: Int = 1;
    lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyBLdi57ZGLkSrGDPD-7on9qCKd64vNlRAk")
        }
        placesClient = Places.createClient(this)


        var fields: List<Place.Field> = Arrays.asList(
            Place.Field.LAT_LNG, Place.Field.NAME,
            Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS
        )
        var intent: Intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("PAK").setTypeFilter(TypeFilter.ESTABLISHMENT).build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
*/



/*

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

// Specify the types of place data to return.
        autocompleteFragment!!
            .setTypeFilter(TypeFilter.CITIES)
            .setCountry("PAK")

        autocompleteFragment!!.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME))
// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("YES", "Place: " + place.getName() + ", " + place.latLng!!.latitude)

            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("NO", "An error occurred: $status")
            }
        })
*/

        supportActionBar!!.setTitle("Van Management")

        pd = ProgressDialog(this)
        pd!!.setMessage("Fetching Data From Server")
        pd!!.setCancelable(false)
        pd!!.show()

        if (Objects.UserType!!.equals("Driver")) {
            Objects.UserType = "driver"
            driver_detail()
            replaceFragmentUserActivity(Driver_home(), null)

        } else if (Objects.UserType!!.equals("Student")) {
            Objects.UserType = "student"
            student_detail()
            replaceFragmentUserActivity(Student_home(), null)

        }

        val fm = supportFragmentManager
        fm.addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
                moveTaskToBack(true)
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                var place: Place = Autocomplete.getPlaceFromIntent(data!!);
                Toast.makeText(this, "Place is " + place.name.toString(), Toast.LENGTH_SHORT).show()
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(this, "Place ERRROR ", Toast.LENGTH_SHORT).show()
                // TODO: Handle the error.
                var status: Status = Autocomplete.getStatusFromIntent(data!!);
//                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    fun student_detail() {

        val rootRef = Objects.getFirebaseInstance().reference
        val ordersRef = rootRef.child("StudentDetails").child(Objects.UserID.Globaluser_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
/*
                var data = dataSnapshot.getValue(StudentDetail_Model::class.java)!!

                Objects.getStudentDetailInstance().student_id = data.student_id
                Objects.getStudentDetailInstance().lat = data.lat
                Objects.getStudentDetailInstance().longi = data.longi
                Objects.getStudentDetailInstance().student_name = data.student_name
                Objects.getStudentDetailInstance().student_email = data.student_email
                Objects.getStudentDetailInstance().student_cnic = data.student_cnic
                Objects.getStudentDetailInstance().student_contact = data.student_contact
                Objects.getStudentDetailInstance().student_address = data.student_address
                Objects.getStudentDetailInstance().fee_status = data.fee_status
                Objects.getStudentDetailInstance().ammount = data.ammount
                Objects.getStudentDetailInstance().status = data.status
                Objects.getStudentDetailInstance().shift_time = data.shift_time
                Objects.getStudentDetailInstance().drop_time = data.drop_time
                Objects.getStudentDetailInstance().allocated_van = data.allocated_van
                Objects.getStudentDetailInstance().driver_id = data.driver_id
                Objects.getStudentDetailInstance().driver_name = data.driver_name
*/

                val listDataRef = dataSnapshot.getValue(StudentDetail_Model::class.java)!!
                Objects.studentDetail_model = listDataRef
                pd?.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                pd?.dismiss()
                Toast.makeText(this@UserActivity, "Error in Fetching data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)

    }

    fun driver_detail() {

        val rootRef = Objects.getFirebaseInstance().reference
        val ordersRef = rootRef.child("DriverDetails").child(Objects.UserID.Globaluser_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listDataRef = dataSnapshot.getValue(DriverDetail_Model::class.java)!!
                Objects.driverDetail_model = listDataRef
                getScheduledList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                pd?.dismiss()
                Toast.makeText(this@UserActivity, "Error in Fetching data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)

    }


    fun getScheduledList() {
        val ref = Objects.getFirebaseInstance().reference.child("scheduled_list")
            .child(Objects.getDriverDetailInstance().driver_id)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Objects.scheduled_list.clear()
                for (postSnapshot in dataSnapshot.children) {
                    var model: ChatList = ChatList(postSnapshot.value.toString(), postSnapshot.key)
                    Objects.scheduled_list.add(model)
                }
                pd?.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                pd?.dismiss()
                Toast.makeText(this@UserActivity, "Error in Fetching data", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_settings -> {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                FirebaseAuth.getInstance().signOut();
                finishAffinity()
            }
            R.id.action_change_pswd -> {
                replaceFragmentUserActivity(ChangePassword(), null)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
