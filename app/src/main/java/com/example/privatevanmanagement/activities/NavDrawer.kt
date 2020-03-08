package com.example.privatevanmanagement.activities

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.privatevanmanagement.ChatModule.ShowActivities.Users
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.UserType
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.example.privatevanmanagement.Fragments.admin.Admin_ManageFee
import com.example.privatevanmanagement.Fragments.admin.Admin_ManageSalary
import com.example.privatevanmanagement.Fragments.admin.Admin_ManageStudents
import com.example.privatevanmanagement.Fragments.admin.Admin_home
import com.example.privatevanmanagement.Fragments.driver.Driver_home
import com.example.privatevanmanagement.Fragments.student.Student_home


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


        val menu = navigationView.menu
        for (menuItemIndex in 0 until menu.size()) {
            val menu = navigationView.menu
            for (menuItemIndex in 0 until menu.size()) {
                val menuItem = menu.getItem(menuItemIndex)
                if (UserType.equals("Student")) {
//                    if (menuItem.itemId == R.id.nav_addStudent) {
//                        //fill model of signed in UserType
//                        student_detail()
//                        menuItem.isVisible = false
//                    }
                } else if (UserType.equals("Driver")) {
//                    if (menuItem.itemId == R.id.nav_addDrivers) {
//                        //fill model of signed in UserType
//                        driver_detail()
//                        menuItem.isVisible = false
//                    }
                } else {
                    admin_detail()
                }
            }
        }
        navigationView.setNavigationItemSelectedListener(this)

        // dummy login
        if (UserType!!.equals("driver")) {
            UserType = "driver"
            replaceFragment(Driver_home(), null)
        } else if (UserType!!.equals("student")) {
            UserType = "student"
            replaceFragment(Student_home(), null)
        } else {
            UserType = "admin"
            replaceFragment(Admin_home(), null)
        }

        val fm = supportFragmentManager

        fm.addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0)
            {
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
            showDialogMakeComplaint()
        } else if (id == R.id.nav_manageStudents) {
            val home = Admin_ManageStudents()
            replaceFragment(home, null)
        }

//        if (id == R.id.nav_home) {
//            val home = Admin_home()
//            ChangeFragments(home)
//        } else if (id == R.id.nav_addStudent) {
//            val home = AddStudent()
//            ChangeFragments(home)
//        } else if (id == R.id.nav_addDrivers) {
//            val home = AddDriver()
//            ChangeFragments(home)
//        } else if (id == R.id.nav_addVan) {
//            val home = AdminAddVan()
//            ChangeFragments(home)
//        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun ChangeFragments(newFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.mlayout, newFragment).commit()
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


    fun student_detail() {

        val rootRef = Objects.getFirebaseInstance().reference
        val ordersRef = rootRef.child("StudentDetails").child(Objects.UserID.Globaluser_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Objects.getStudentDetailInstance().studend_id = Objects.UserID.Globaluser_ID
                Objects.getStudentDetailInstance().studend_name =
                    dataSnapshot.child("StudentName").value.toString()
                Objects.getStudentDetailInstance().studend_address =
                    dataSnapshot.child("StudentAddress").value.toString()
                Objects.getStudentDetailInstance().studend_contact =
                    dataSnapshot.child("StudentContact").value.toString()
                Objects.getStudentDetailInstance().studend_cnic =
                    dataSnapshot.child("StudentCnic").value.toString()
                Objects.getStudentDetailInstance().studend_email =
                    dataSnapshot.child("StudentEmail").value.toString()
                Objects.getStudentDetailInstance().studend_van_id =
                    dataSnapshot.child("Van_ID").value.toString()
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


    fun showDialogMakeComplaint() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this!!.layoutInflater
        dialogBuilder.setCancelable(false)
        val dialogView = inflater.inflate(R.layout.dialog_register_complaint, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val btn_sendNotficaion = dialogView.findViewById(R.id.btn_submitComplaint) as Button
        val btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog) as ImageButton

        btn_sendNotficaion.setOnClickListener(View.OnClickListener { })

        btn_closeDialog.setOnClickListener(
            View.OnClickListener
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
