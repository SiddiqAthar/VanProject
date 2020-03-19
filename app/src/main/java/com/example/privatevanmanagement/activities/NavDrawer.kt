package com.example.privatevanmanagement.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.privatevanmanagement.ChatModule.ShowActivities.Users
import com.example.privatevanmanagement.Fragments.admin.*
import com.example.privatevanmanagement.Fragments.driver.Driver_home
import com.example.privatevanmanagement.Fragments.student.Student_home
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.UserType
import com.example.privatevanmanagement.utils.SendNotification
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class NavDrawer : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            null,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()


        val navigationView = findViewById(R.id.nav_view) as NavigationView
        //check user type to show daa

        navigationView.setNavigationItemSelectedListener(this)

        // dummy login
        if (UserType!!.equals("Driver")) {
            UserType = "driver"
            replaceFragment(Driver_home(), null)
        } else if (UserType!!.equals("Student")) {
            UserType = "student"
            student_detail()
        } else {
            UserType = "Admin"
            replaceFragment(Admin_home(), null)
        }

        val fm = supportFragmentManager

        fm.addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
                moveTaskToBack(true)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
        if (id == R.id.action_chat) {
            startActivity(Intent(applicationContext, Users::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {  // Handle navigation view item clicks here.
        val id = item.itemId


        if (id == R.id.nav_manageFee) {
            val home = Admin_ManageFee()
            replaceFragment(home, null)
        } else if (id == R.id.nav_paySalary) {
            val home = Admin_ManageSalary()
            replaceFragment(home, null)
        } else if (id == R.id.nav_complaintBox) {
            val home = Admin_ViewComplaints()
            replaceFragment(home, null)
        } else if (id == R.id.nav_manageStudents) {
            val home = Admin_ManageStudents()
            replaceFragment(home, null)
        } else if (id == R.id.nav_add_group) {
            showDialogAddGroup()
        } else if (id == R.id.nav_add_shift) {
            showDialogAddShit()
        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
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

                replaceFragment(Student_home(), null)

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

    fun admin_detail() {

        val rootRef = Objects.getFirebaseInstance().reference
        val ordersRef = rootRef.child("AdminDetails").child(Objects.UserID.Globaluser_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Objects.getAdminDetailInstance().admin_id = Objects.UserID.Globaluser_ID
                Objects.getAdminDetailInstance().admin_name =
                    dataSnapshot.child("AdminName").value.toString()
                Objects.getAdminDetailInstance().admin_address =
                    dataSnapshot.child("AdminAddress").value.toString()
                Objects.getAdminDetailInstance().admin_contact =
                    dataSnapshot.child("AdminContact").value.toString()
                Objects.getAdminDetailInstance().admin_cnic =
                    dataSnapshot.child("AdminCnic").value.toString()
//                Objects.getAdminDetailInstance().a =
//                    dataSnapshot.child("DriverEmail").value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)

    }

    fun showDialogAddGroup() {
        val dialogBuilder = AlertDialog.Builder(this!!)
        val inflater = this!!.layoutInflater
        dialogBuilder.setCancelable(false)
        val dialogView = inflater.inflate(R.layout.dialog_add_group, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val et_add_Group = dialogView.findViewById(R.id.et_add_Group) as EditText
        val btn_addGroup = dialogView.findViewById(R.id.btn_addGroup) as Button
        val btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog) as ImageButton



        btn_addGroup.setOnClickListener(View.OnClickListener {
            if (!et_add_Group.text.isNullOrEmpty()) {
                //add data here on firebase and send notification
                val newPost2 =
                    Objects.getFirebaseInstance().reference.child("Groups")
//                newPost2.child("Group_name").setValue(et_add_Group.text.toString())
                newPost2.child(et_add_Group.text.toString()).setValue(et_add_Group.text.toString())
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Group field is empty", Toast.LENGTH_SHORT).show()
            }
        })

        btn_closeDialog.setOnClickListener(View.OnClickListener
        {
            alertDialog.dismiss()
        })

        // alertDialog.show()
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        } else {
            alertDialog.show()
            alertDialog.getWindow()!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }


    fun showDialogAddShit() {
        val dialogBuilder = AlertDialog.Builder(this!!)
        val inflater = this!!.layoutInflater
        dialogBuilder.setCancelable(false)
        val dialogView = inflater.inflate(R.layout.dialog_add_shift, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val et_add_Shift = dialogView.findViewById(R.id.et_add_Shift) as EditText
        val shift_timePicker = dialogView.findViewById(R.id.shift_timePicker) as TimePicker
        val btn_addShift = dialogView.findViewById(R.id.btn_addShift) as Button
        val btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog) as ImageButton
        shift_timePicker.setIs24HourView(false)

        btn_addShift.setOnClickListener(View.OnClickListener {
            var time: String = getTime(shift_timePicker)
            if (!et_add_Shift.text.isNullOrEmpty() && !time.toString().isNullOrEmpty()) {
                //add data here on firebase and send notification
                val newPost2 =
                    Objects.getFirebaseInstance().reference.child("Shifts")
                newPost2.child(et_add_Shift.text.toString())
                    .setValue(time)
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Shift field is empty", Toast.LENGTH_SHORT).show()
            }
        })

        btn_closeDialog.setOnClickListener(View.OnClickListener
        {
            alertDialog.dismiss()
        })

        // alertDialog.show()
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        } else {
            alertDialog.show()
            alertDialog.getWindow()!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }


    fun getTime(picker: TimePicker): String {
        var hour = 0
        var minute = 0;
        var am_pm: String? = null

        if (Build.VERSION.SDK_INT >= 23) {
            hour = picker.getHour();
            minute = picker.getMinute();
        } else {
            hour = picker.getCurrentHour();
            minute = picker.getCurrentMinute();
        }
        if (hour > 12) {
            am_pm = "PM";
            hour = hour - 12;
        } else {
            am_pm = "AM";
        }
        return "" + hour + ":" + minute + " " + am_pm
    }

    fun ChangeManagementFragment(fragment: Fragment, bundle: Bundle?) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mlayout, fragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack("std").commit()
    }

    fun replaceFragment(fragment: Fragment, bundle: Bundle?) {
        var backStateName: String = fragment.javaClass.name

        if (bundle != null)
            fragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        var fragmentPopped: Boolean = fragmentManager.popBackStackImmediate(backStateName, 0)

        if (!fragmentPopped) { //fragment not in back stack, create it.
            var ft = fragmentManager.beginTransaction()
            ft.replace(R.id.mlayout, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}
