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
import com.example.privatevanmanagement.adapters.Adapter_manageGroups
import com.example.privatevanmanagement.utils.Objects.group_list
import java.util.*


public class Admin_ManageGroup : Fragment() {

    var rootView: View? = null
    private var rv_manageGroups: RecyclerView? = null
    private var adapter_manageGroups: Adapter_manageGroups? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_admin__manage_group, container, false)
        activity?.setTitle("Manage Groups")
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_manageGroups = rootView?.findViewById(R.id.rv_manageGroups)


        rv_manageGroups?.setLayoutManager(LinearLayoutManager(activity))
        rv_manageGroups?.setNestedScrollingEnabled(false)
        rv_manageGroups?.setHasFixedSize(true)
        rv_manageGroups?.addItemDecoration(
            DividerItemDecoration(
                rv_manageGroups!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        if (!group_list.isNullOrEmpty()) // agr list empty nai hai to jae
            {
                adapter_manageGroups = Adapter_manageGroups(group_list as ArrayList<String>?, activity)
                adapter_manageGroups!!.notifyDataSetChanged()
                rv_manageGroups?.setAdapter(adapter_manageGroups)
            }
    }

}
