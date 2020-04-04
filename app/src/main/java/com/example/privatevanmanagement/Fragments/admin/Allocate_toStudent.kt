package com.example.privatevanmanagement.Fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.AdminNav_Activity
import com.example.privatevanmanagement.adapters.Spinner_FreeDriver_Adapter
import com.example.privatevanmanagement.adapters.Spinner_Van_Adapter
import com.example.privatevanmanagement.models.DriverDetail_Model
import com.example.privatevanmanagement.models.VanDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.sortedList
import com.example.privatevanmanagement.utils.SendNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class Allocate_toStudent : Fragment() {

    private var rootView: View? = null
    var mainActivity: AdminNav_Activity? = null
    private var vanNumber_Spinner: Spinner? = null
    private var driverName_Spinner: Spinner? = null
    private lateinit var vanNumber_adapter: Adapter
    private lateinit var driverName_adapter: Adapter
    var btn_Allocate_toStudent: Button? = null

    var userRef2: DatabaseReference? = null
    //selected Van and Driver
    var selected_van_id: String? = null
    var selected_van_registeration: String? = null
    var selected_driver_id: String? = null
    var selected_driver_name: String? = null
    var dummyArrayList: ArrayList<String>? = null

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

        dummyArrayList = ArrayList()

        init(rootView)

        return rootView
    }


    private fun init(rootView: View?) {
        mainActivity = activity as AdminNav_Activity

        vanNumber_Spinner = rootView!!.findViewById(R.id.vanNumber_Spinner) as Spinner
        driverName_Spinner = rootView!!.findViewById(R.id.driverName_Spinner) as Spinner
        btn_Allocate_toStudent = rootView!!.findViewById(R.id.btn_Allocate_toStudent) as Button

        vanNumber_adapter = Spinner_Van_Adapter(
            this!!.context!!, R.id.spinner_item_tv, R.id.spinner_item_tv,
            Objects.freevanList as ArrayList<VanDetail_Model>
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

            if (!selected_van_id.isNullOrEmpty() || !selected_driver_id.isNullOrEmpty()) {

                updateVaninfo()
                updateStudentinfo()
                updateDriverinfo()
                updateScheduledList()

                sendMessageToStudent("Dear Student your driver is " + selected_driver_name + " with Van " + selected_van_registeration)
                sendMessageToDriver("Dear you have allocated van "+selected_van_registeration+".")

            }

        })

    }


    private fun updateScheduledList() {
        userRef2 = Objects.getFirebaseInstance().getReference().child("scheduled_list")
            .child(selected_driver_id.toString())
        for (i in 0 until sortedList.size) {
            userRef2!!.child(sortedList.get(i).student_id).setValue(sortedList.get(i).student_name)
        }
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
            val ref = Objects.getFirebaseInstance().reference.child("StudentDetails")
                .child(sortedList.get(i).student_id)

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
        val ref = Objects.getFirebaseInstance().reference.child("DriverDetails")
            .child(selected_driver_id.toString())
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

    private fun sendMessageToStudent(toString: String) {
        var userList = ArrayList<String>()
        val myRef = Objects.getFirebaseInstance().getReference("Token")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in 0 until sortedList.size) {
                    for (postSnapshot in snapshot.children) {
                        if(sortedList.get(i).student_id.equals(postSnapshot.key))
                        userList.add(postSnapshot.value.toString())
                    }
                }
                msgsendToStudent(userList, toString)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })
    }


    private fun sendMessageToDriver(toString: String) {
        val myRef =
            Objects.getFirebaseInstance().getReference("Token").child(selected_driver_id.toString())
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userList = ArrayList<String>()
                userList.add(snapshot.key.toString())
                msgsendToDriver(userList, toString)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })
    }

    fun msgsendToStudent(to: ArrayList<String>, body: String) {

/*        for (i in 0 until sortedList.size) {
            if (to.get(i).equals(sortedList.get(i).student_id)) {
                dummyArrayList!!.add(sortedList.get(i).student_id)
            }
        }*/
        var sendNoteaa = SendNotification()
        sendNoteaa.execute(to, body);
    }

    fun msgsendToDriver(to: ArrayList<String>, body: String) {

        var sendNoteaa = SendNotification()
        sendNoteaa.execute(to, body);
    }

}
