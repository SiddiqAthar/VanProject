package com.example.privatevanmanagement.Fragments.admin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.NavDrawer
import com.example.privatevanmanagement.adapters.Adapter_manageStudent
import com.example.privatevanmanagement.adapters.Spinner_Adapter
import com.example.privatevanmanagement.adapters.Spinner_FreeDriver_Adapter
import com.example.privatevanmanagement.adapters.Spinner_Van_Adapter
import com.example.privatevanmanagement.models.DriverDetail_Model
import com.example.privatevanmanagement.models.Shift_Model
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.models.VanDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.sortedList
import com.example.privatevanmanagement.utils.Objects.vanList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.HashMap

class Allocate_toStudent : Fragment() {

    private var rootView: View? = null
    var mainActivity: NavDrawer? = null
    private var vanNumber_Spinner: Spinner? = null
    private var driverName_Spinner: Spinner? = null
    private lateinit var vanNumber_adapter: Adapter
    private lateinit var driverName_adapter: Adapter
    var btn_Allocate_toStudent: Button? = null

    //selected Van and Driver
    var selected_van_id: String? = null
    var selected_van_registeration: String? = null
    var selected_driver_id: String? = null
    var selected_driver_name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_allocate_to_student, container, false)
        activity?.setTitle("Allocate to Student")


        init(rootView)

        return rootView
    }


    private fun init(rootView: View?) {
        mainActivity = activity as NavDrawer

        vanNumber_Spinner = rootView!!.findViewById(R.id.vanNumber_Spinner) as Spinner
        driverName_Spinner = rootView!!.findViewById(R.id.driverName_Spinner) as Spinner
        btn_Allocate_toStudent = rootView!!.findViewById(R.id.btn_Allocate_toStudent) as Button

        vanNumber_adapter = Spinner_Van_Adapter(
            this!!.context!!, R.id.spinner_item_tv, R.id.spinner_item_tv,
            Objects.vanList as ArrayList<VanDetail_Model>
        )
        vanNumber_Spinner?.adapter = vanNumber_adapter as Spinner_Van_Adapter


        driverName_adapter = Spinner_FreeDriver_Adapter(
            this!!.context!!, R.id.spinner_item_tv, R.id.spinner_item_tv,
            Objects.freeDriverList as ArrayList<DriverDetail_Model>
        )
        driverName_Spinner?.adapter = driverName_adapter as Spinner_FreeDriver_Adapter


        vanNumber_Spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println(" Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selected_van_id = (parent?.getItemAtPosition(position) as VanDetail_Model).vanId
                selected_van_registeration =
                    (parent?.getItemAtPosition(position) as VanDetail_Model).vanRegisteration
            }
        }
        driverName_Spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println(" Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selected_driver_id =
                    (parent?.getItemAtPosition(position) as DriverDetail_Model).driver_id
                selected_driver_name =
                    (parent?.getItemAtPosition(position) as DriverDetail_Model).driver_name
            }
        }



        btn_Allocate_toStudent!!.setOnClickListener(View.OnClickListener {

            updateVaninfo()
            updateStudentinfo()
            updateDriverinfo()

        })

    }

    private fun updateVaninfo() {
        val ref = Objects.getFirebaseInstance().reference.child("AddVan")
            .child(selected_van_id.toString())
        val updates = HashMap<String, Any>()

        updates["assign_Status"] = "Yes"
        updates["assign_DriverId"] = selected_driver_id.toString()
        updates["assign_DriverName"] = selected_driver_name.toString()

        if (updates.size > 0) {
            ref.updateChildren(updates)
        }
    }

    private fun updateStudentinfo() {
        for (i in 0 until sortedList.size) {
            val ref = Objects.getFirebaseInstance().reference.child("StudentDetails").child(
                sortedList.get(i).student_id)

            val updates = HashMap<String, Any>()

            updates["allocated_van"] = selected_van_id.toString()
            updates["driver_id"] = selected_driver_id.toString()
            updates["driver_name"] = selected_driver_name.toString()

            if (updates.size > 0) {
                ref.updateChildren(updates)
            }

        }
     }

    private fun updateDriverinfo() {
        val ref = Objects.getFirebaseInstance().reference.child("DriverDetails").child(selected_driver_id.toString())
        val updates = HashMap<String, Any>()

        updates["assigned_status"] = "Yes"
        updates["driver_van_id"] = selected_van_id.toString()
        updates["van_number"] = selected_van_registeration.toString()

        if (updates.size > 0) {
            ref.updateChildren(updates)
        }

        Toast.makeText(activity, "Allocate Van Successfully", Toast.LENGTH_SHORT).show();
        mainActivity!!.replaceFragment(Admin_home(), null)
    }



}
