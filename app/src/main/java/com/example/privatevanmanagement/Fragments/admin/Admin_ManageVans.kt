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
import com.example.privatevanmanagement.adapters.Adapter_manageStudent
import com.example.privatevanmanagement.adapters.Adapter_manageVans
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.models.VanDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.student_modelList
import com.example.privatevanmanagement.utils.Objects.vanList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


public class Admin_ManageVans : Fragment() {

    var rootView: View? = null
    private var rv_manageVans: RecyclerView? = null
    private var adapter_manageVans: Adapter_manageVans? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_admin__manage_vans, container, false)
        activity?.setTitle("Manage Vans")
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_manageVans = rootView?.findViewById(R.id.rv_manageVans)


        rv_manageVans?.setLayoutManager(LinearLayoutManager(activity))
        rv_manageVans?.setNestedScrollingEnabled(false)
        rv_manageVans?.setHasFixedSize(true)
        rv_manageVans?.addItemDecoration(
            DividerItemDecoration(
                rv_manageVans!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        if (vanList.isNullOrEmpty()) // agr list empty hai to jae
            {
                val adminHome = Admin_home()
                adminHome.getVanData()
                adapter_manageVans = Adapter_manageVans(vanList as ArrayList<VanDetail_Model>?, activity)
                adapter_manageVans!!.notifyDataSetChanged()
                rv_manageVans?.setAdapter(adapter_manageVans)
            }
            else {
            adapter_manageVans = Adapter_manageVans(vanList as ArrayList<VanDetail_Model>?, activity)
            adapter_manageVans!!.notifyDataSetChanged()
             rv_manageVans?.setAdapter(adapter_manageVans)
        }
    }

}
