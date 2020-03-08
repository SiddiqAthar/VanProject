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
import com.example.privatevanmanagement.Adapters.Adapter_manageFeeextends
import com.example.privatevanmanagement.Adapters.Adapter_scheduleVan

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.models.ManageFee_Model
import java.util.ArrayList
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.privatevanmanagement.Adapters.Adapter_manageStudent


public class Admin_ManageStudents : Fragment() {

    var rootView: View? = null
    private var rv_manageStudent: RecyclerView? = null
    private var adapter_manageStudent: Adapter_manageStudent? = null
    var ArrayList_Model: ArrayList<ManageFee_Model>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_admin__manage_student, container, false)
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_manageStudent = rootView?.findViewById(R.id.rv_manageStudent)

        ArrayList_Model = ArrayList<ManageFee_Model>()
        //add dummy data
        ArrayList_Model!!.add(ManageFee_Model("", "Siddiq", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Uzair", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Usman", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Zohaib", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Umer", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Ali", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Ahtisham", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Athar Iqbal", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Nabeeel Shoukat", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Abu Bakar", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("", "Zaheer", "", ""))


        rv_manageStudent?.setLayoutManager(LinearLayoutManager(activity))
        rv_manageStudent?.setNestedScrollingEnabled(false)
        rv_manageStudent?.setHasFixedSize(true)
        rv_manageStudent?.addItemDecoration(
            DividerItemDecoration(
                rv_manageStudent!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        adapter_manageStudent = Adapter_manageStudent(ArrayList_Model, activity)
        rv_manageStudent?.setAdapter(adapter_manageStudent)

    }
}
