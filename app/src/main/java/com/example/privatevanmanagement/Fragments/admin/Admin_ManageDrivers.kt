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
import com.example.privatevanmanagement.adapters.Adapter_manageDriver
import com.example.privatevanmanagement.utils.Objects.driver_modelList


public class Admin_ManageDrivers : Fragment() {

    var rootView: View? = null
    private var rv_manageDriver: RecyclerView? = null
    private var adapter_manageDriver: Adapter_manageDriver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_admin__manage_driver, container, false)
        activity?.setTitle("Manage Drivers")
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_manageDriver = rootView?.findViewById(R.id.rv_manageDriver)


        rv_manageDriver?.setLayoutManager(LinearLayoutManager(activity))
        rv_manageDriver?.setNestedScrollingEnabled(false)
        rv_manageDriver?.setHasFixedSize(true)
        rv_manageDriver?.addItemDecoration(
            DividerItemDecoration(
                rv_manageDriver!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        if (driver_modelList.isNullOrEmpty()) // agr list empty hai to jae
            {
                val adminHome = Admin_home()
                adminHome.getDriverList()
                adapter_manageDriver = Adapter_manageDriver(driver_modelList, activity)
                adapter_manageDriver!!.notifyDataSetChanged()
                rv_manageDriver?.setAdapter(adapter_manageDriver)
            }
            else {
            adapter_manageDriver = Adapter_manageDriver(driver_modelList, activity)
            rv_manageDriver?.setAdapter(adapter_manageDriver)
        }
    }

}
