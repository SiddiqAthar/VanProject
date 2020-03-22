package com.example.privatevanmanagement.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.privatevanmanagement.Fragments.driver.Driver_home
import com.example.privatevanmanagement.Fragments.student.Student_home
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class UserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setTitle("Van Management")

        if (Objects.UserType!!.equals("Driver")) {
            Objects.UserType = "driver"
            driver_detail()
            replaceFragmentUserActivity(Driver_home(), null)

        } else if (Objects.UserType!!.equals("Student")) {
            Objects.UserType = "student"
            student_detail()
            replaceFragmentUserActivity(Student_home(), null)

        }
    }


    fun student_detail() {

        val rootRef = Objects.getFirebaseInstance().reference
        val ordersRef = rootRef.child("StudentDetails").child(Objects.UserID.Globaluser_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
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

/*
                Objects.getStudentDetailInstance().studend_name =
                    dataSnapshot.child("StudentName").value.toString()
 */
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)

    }

    fun driver_detail() {

        val rootRef = Objects.getFirebaseInstance().reference
        val ordersRef = rootRef.child("DriverDetails").child(Objects.UserID.Globaluser_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Objects.getDriverDetailInstance().driver_id = Objects.UserID.Globaluser_ID
                Objects.getDriverDetailInstance().driver_name =
                    dataSnapshot.child("DriverName").value.toString()
                Objects.getDriverDetailInstance().driver_address =
                    dataSnapshot.child("DriverAddress").value.toString()
                Objects.getDriverDetailInstance().driver_contact =
                    dataSnapshot.child("DriverContact").value.toString()
                Objects.getDriverDetailInstance().driver_cnic =
                    dataSnapshot.child("DriverCnic").value.toString()
                Objects.getDriverDetailInstance().driver_email =
                    dataSnapshot.child("DriverEmail").value.toString()
                Objects.getDriverDetailInstance().driver_van_id =
                    dataSnapshot.child("Van_ID").value.toString()


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_settings -> Toast.makeText(this, "Clicked Menu 1", Toast.LENGTH_SHORT).show()
             else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
