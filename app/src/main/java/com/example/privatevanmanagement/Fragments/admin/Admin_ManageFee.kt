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




public class Admin_ManageFee : Fragment() {

    var rootView: View? = null

    private var rv_manageFee: RecyclerView? = null
    private var adapter_manageFee: Adapter_manageFeeextends? = null
    var ArrayList_Model: ArrayList<ManageFee_Model>? = null

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

        ArrayList_Model=ArrayList<ManageFee_Model>()
        //add dummy data
        ArrayList_Model!!.add(ManageFee_Model("", "Siddiq", "2000", "Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Uzair", "2000", "Un-Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Usman", "2000", "Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Zohaib", "2000", "Un-Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Umer", "2000", "Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Ali", "2000", "Un-Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Ahtisham", "2000", "Un-Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Athar Iqbal", "2000", "Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Nabeeel Shoukat", "2000", "Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Abu Bakar", "2000", "Un-Paid"))
        ArrayList_Model!!.add(ManageFee_Model("", "Zaheer", "2000", "Un-Paid"))


        rv_manageFee?.setLayoutManager(LinearLayoutManager(activity))
        rv_manageFee?.setNestedScrollingEnabled(false)
        rv_manageFee?.setHasFixedSize(true)
        rv_manageFee?.addItemDecoration(
            DividerItemDecoration(
                rv_manageFee!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        adapter_manageFee = Adapter_manageFeeextends(ArrayList_Model, activity)
        rv_manageFee?.setAdapter(adapter_manageFee)

    }
}
