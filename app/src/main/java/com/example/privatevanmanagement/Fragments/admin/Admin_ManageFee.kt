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
import com.example.privatevanmanagement.adapters.Adapter_manageFeeextends
import com.example.privatevanmanagement.adapters.Adapter_manageStudent
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.student_modelList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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
        activity?.setTitle("Manage Fee")

        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_manageFee = rootView?.findViewById(R.id.rv_manageFee)

        if (student_modelList.isNullOrEmpty()) // agr list empty hai to jae
        {
            val adminHome = Admin_home()
            adminHome.getStudentList()
            adapter_manageFee = Adapter_manageFeeextends(student_modelList, activity)
            adapter_manageFee!!.notifyDataSetChanged()
            rv_manageFee?.setAdapter(adapter_manageFee)

        } else {
            adapter_manageFee = Adapter_manageFeeextends(student_modelList, activity)
            adapter_manageFee!!.notifyDataSetChanged()
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
}
