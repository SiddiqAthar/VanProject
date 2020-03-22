package com.example.privatevanmanagement.Fragments.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.UserActivity
import com.example.privatevanmanagement.adapters.Adapter_comingTrip
import com.example.privatevanmanagement.models.ManageFee_Model
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.driver_modelList
import com.example.privatevanmanagement.utils.Objects.student_modelList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class Driver_Coming_Trip : Fragment() {

    var rootView: View? = null
    private var rv_tripStudents: RecyclerView? = null
    private var tv_tripDetail: TextView? = null
    private var btn_start_ride: Button? = null
    private var adapter_comingTrip: Adapter_comingTrip? = null
    var ArrayList_Model: ArrayList<ManageFee_Model>? = null
    var mainActivity: UserActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_driver__coming__trip, container, false)
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        mainActivity = activity as UserActivity
        rv_tripStudents = rootView?.findViewById(R.id.rv_tripStudents)
        tv_tripDetail = rootView?.findViewById(R.id.tv_tripDetail)
        btn_start_ride = rootView?.findViewById(R.id.btn_start_ride)
        if (!driver_modelList.isEmpty()) {
            tv_tripDetail!!.setText(
                "You have ride with following Student. Your Van number is " + driver_modelList.get(
                    1
                ).van_number
            )
/*
         //"Set here model according to trip and add data in list, Idr just for UI yahi model pass kar dia ")
*/
            getAllocatedStudentList()

            rv_tripStudents?.setLayoutManager(LinearLayoutManager(activity))
            rv_tripStudents?.setNestedScrollingEnabled(false)
            rv_tripStudents?.setHasFixedSize(true)
            rv_tripStudents?.addItemDecoration(
                DividerItemDecoration(
                    rv_tripStudents!!.getContext(),
                    DividerItemDecoration.VERTICAL
                )
            )


            btn_start_ride!!.setOnClickListener(View.OnClickListener {
                mainActivity!!.replaceFragmentUserActivity(Driver_Track(), null)
            })

        }

    }

    fun getAllocatedStudentList() {
        val myRef = Objects.getFirebaseInstance().getReference()
        val query = myRef.child("StudentDetails").orderByChild("driver_id")
            .equalTo(Objects.UserID.Globaluser_ID)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                student_modelList.clear()
                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(StudentDetail_Model::class.java)!!
                    student_modelList.add(listDataRef)

                }
                adapter_comingTrip = Adapter_comingTrip(student_modelList as ArrayList<StudentDetail_Model>?, activity)
                adapter_comingTrip!!.notifyDataSetChanged()
                rv_tripStudents?.setAdapter(adapter_comingTrip)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })

    }
}
