package com.example.privatevanmanagement.Fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
 import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.adapters.Adapter_ViewComplaints
import com.example.privatevanmanagement.adapters.Adapter_manageFeeextends
import com.example.privatevanmanagement.models.Complaint_Model
import com.example.privatevanmanagement.models.Shift_Model
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.student_modelList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


public class Admin_ViewComplaints : Fragment() {

    var rootView: View? = null
    private var rv_ViewComplaints: RecyclerView? = null
    private var adapter_ViewComplaints: Adapter_ViewComplaints? = null
    var complaint_list: ArrayList<Complaint_Model> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_admin_viewcomplaints, container, false)
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_ViewComplaints = rootView?.findViewById(R.id.rv_ViewComplaints)

        if (complaint_list.isNullOrEmpty()) // agr list empty hai to jae
            getComplaintList()
        else
        {
            adapter_ViewComplaints = Adapter_ViewComplaints(complaint_list, activity)
            rv_ViewComplaints?.setAdapter(adapter_ViewComplaints)
        }

        rv_ViewComplaints?.setLayoutManager(LinearLayoutManager(activity))
        rv_ViewComplaints?.setNestedScrollingEnabled(false)
        rv_ViewComplaints?.setHasFixedSize(true)
        rv_ViewComplaints?.addItemDecoration(
            DividerItemDecoration(
                rv_ViewComplaints!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    fun getComplaintList() {
        val myRef = Objects.getFirebaseInstance().getReference("Complaints")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                complaint_list.clear()

                for (postSnapshot in snapshot.children) {
                    for (snap2 in postSnapshot.children) {
                        val model = Complaint_Model(snap2.key, snap2.value!!.toString())
                        complaint_list.add(model)
                    }
                    // here you can access to name property like university.name
                }
                adapter_ViewComplaints = Adapter_ViewComplaints(complaint_list, activity)
                rv_ViewComplaints?.setAdapter(adapter_ViewComplaints)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })
    }
}
