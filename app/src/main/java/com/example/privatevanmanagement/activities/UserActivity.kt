package com.example.privatevanmanagement.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import java.io.File


class UserActivity : BaseActivity() {

    var pd: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




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
                clearApplicationData()
                FirebaseAuth.getInstance().signOut();
                FirebaseInstanceId.getInstance().deleteInstanceId()
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

    fun clearApplicationData() {
        val cache: File = cacheDir
        val appDir = File(cache.getParent())
        if (appDir.exists()) {
            val children: Array<String> = appDir.list()
            for (s in children) {
                if (s != "lib") {
                    deleteDir(File(appDir, s))
                }
            }
        }
    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

}
