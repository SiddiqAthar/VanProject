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
import androidx.fragment.app.FragmentManager
import com.example.privatevanmanagement.Fragments.ChangePassword
import com.example.privatevanmanagement.Fragments.admin.*
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class AdminNav_Activity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{


    lateinit var mDrawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        val navigationView = findViewById(R.id.nav_view) as NavigationView
        //check user type to show daa

        navigationView.setNavigationItemSelectedListener(this)

         replaceFragment(Admin_home(), null)


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
         val id = item.itemId
        if (id == R.id.action_settings) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            FirebaseAuth.getInstance().signOut();
             finishAffinity()
        }
        if (id == R.id.action_change_pswd) {
            replaceFragment(ChangePassword(),null)
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
        } else if (id == R.id.nav_manageDrivers) {
            val home = Admin_ManageDrivers()
            replaceFragment(home, null)
        }else if (id == R.id.nav_manageVans) {
            val home = Admin_ManageVans()
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

}
