package com.example.privatevanmanagement.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.privatevanmanagement.Fragments.AddDriver
import com.example.privatevanmanagement.Fragments.AddStudent
import com.example.privatevanmanagement.Fragments.AdminAddVan
import com.example.privatevanmanagement.Fragments.HomeFragment
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.UserType
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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
            toolbar,
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
                    if (menuItem.itemId == R.id.nav_addStudent) {
                        menuItem.isVisible = false
                    }
                } else if (UserType.equals("Driver")) {
                    if (menuItem.itemId == R.id.nav_addDrivers) {
                        menuItem.isVisible = false
                    }
                }
            }
        }
        navigationView.setNavigationItemSelectedListener(this)
        ChangeManagementFragment(HomeFragment())
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
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {  // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_home) {
            val home = HomeFragment()
            ChangeFragments(home)
        } else if (id == R.id.nav_addStudent) {
            val home = AddStudent()
            ChangeFragments(home)
        } else if (id == R.id.nav_addDrivers) {
            val home = AddDriver()
            ChangeFragments(home)
        }else if (id == R.id.nav_addVan) {
            val home = AdminAddVan()
            ChangeFragments(home)
        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun ChangeFragments(newFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.mlayout, newFragment).commit()
    }

    fun ChangeManagementFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mlayout, fragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack("std").commit()
    }


}
