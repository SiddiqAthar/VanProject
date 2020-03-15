package com.example.privatevanmanagement.Fragments.admin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.models.ManageFee_Model
import java.util.ArrayList
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.privatevanmanagement.Adapters.Adapter_manageFeeextends
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import android.text.method.TextKeyListener.clear
import com.example.privatevanmanagement.models.Schedule_Student_Model
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.student_modelList
import com.google.firebase.database.ValueEventListener


public class Admin_ManageFee : Fragment() {

    var rootView: View? = null

    private var rv_manageFee: RecyclerView? = null
    private var adapter_manageFee: Adapter_manageFeeextends? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_admin__manage_fee, container, false)
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_manageFee = rootView?.findViewById(R.id.rv_manageFee)

        if (student_modelList.isNullOrEmpty()) // agr list empty hai to jae
            getStudentList()
        else
        {
            adapter_manageFee = Adapter_manageFeeextends(student_modelList, activity)
            rv_manageFee?.setAdapter(adapter_manageFee)
        }

        rv_manageFee?.setLayoutManager(LinearLayoutManager(activity))
        rv_manageFee?.setNestedScrollingEnabled(false)
        rv_manageFee?.setHasFixedSize(true)
        rv_manageFee?.addItemDecoration(
            DividerItemDecoration(
                rv_manageFee!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    fun getStudentList() {
        val myRef = Objects.getFirebaseInstance().getReference("StudentDetails")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                student_modelList.clear()

                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(StudentDetail_Model::class.java)!!
                    student_modelList.add(listDataRef)

                    adapter_manageFee = Adapter_manageFeeextends(student_modelList, activity)
                    rv_manageFee?.setAdapter(adapter_manageFee)

                    // here you can access to name property like university.name

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })
    }
}
