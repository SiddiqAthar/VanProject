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
import com.example.privatevanmanagement.Adapters.Adapter_manageSalaryextends
import com.example.privatevanmanagement.models.ManageSalary_Model


public class Admin_ManageSalary : Fragment() {
    var rootView: View? = null


    private var rv_manageSalary: RecyclerView? = null
    private var adapter_manageSalary: Adapter_manageSalaryextends? = null
    var ArrayList_Model: ArrayList<ManageSalary_Model>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_admin__manage_salary, container, false)
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_manageSalary = rootView?.findViewById(R.id.rv_manageSalary)

        ArrayList_Model=ArrayList<ManageSalary_Model>()
        //add dummy data
        ArrayList_Model!!.add(ManageSalary_Model("", "Siddiq", "Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Uzair",  "Un-Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Usman",  "Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Zohaib",  "Un-Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Umer",  "Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Ali",  "Un-Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Ahtisham",  "Un-Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Athar Iqbal",  "Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Nabeeel Shoukat",  "Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Abu Bakar",  "Un-Paid"))
        ArrayList_Model!!.add(ManageSalary_Model("", "Zaheer",  "Un-Paid"))


        rv_manageSalary?.setLayoutManager(LinearLayoutManager(activity))
        rv_manageSalary?.setNestedScrollingEnabled(false)
        rv_manageSalary?.setHasFixedSize(true)
        rv_manageSalary?.addItemDecoration(
            DividerItemDecoration(
                rv_manageSalary!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        adapter_manageSalary = Adapter_manageSalaryextends(ArrayList_Model, activity)
        rv_manageSalary?.setAdapter(adapter_manageSalary)

    }
}
