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
import com.example.privatevanmanagement.adapters.Adapter_manageSalaryextends
import com.example.privatevanmanagement.models.DriverDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.driver_modelList
import java.util.*


public class Admin_ManageSalary : Fragment() {
    var rootView: View? = null


    private var rv_manageSalary: RecyclerView? = null
    private var adapter_manageSalary: Adapter_manageSalaryextends? = null
    var ArrayList_Model: ArrayList<DriverDetail_Model>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_admin__manage_salary, container, false)
        activity?.setTitle("Pay Salary")

        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_manageSalary = rootView?.findViewById(R.id.rv_manageSalary)

        rv_manageSalary?.setLayoutManager(LinearLayoutManager(activity))
        rv_manageSalary?.setNestedScrollingEnabled(false)
        rv_manageSalary?.setHasFixedSize(true)
        rv_manageSalary?.addItemDecoration(
            DividerItemDecoration(
                rv_manageSalary!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        if (Objects.student_modelList.isNullOrEmpty()) // agr list empty hai to jae
        {
            val adminHome = Admin_home()
            adminHome.getStudentList()
            adapter_manageSalary = Adapter_manageSalaryextends(
                driver_modelList as ArrayList<DriverDetail_Model>?,
                activity
            )
            adapter_manageSalary!!.notifyDataSetChanged()
            rv_manageSalary?.setAdapter(adapter_manageSalary)

        } else {
            adapter_manageSalary = Adapter_manageSalaryextends(
                driver_modelList as ArrayList<DriverDetail_Model>?,
                activity
            )
            adapter_manageSalary!!.notifyDataSetChanged()
            rv_manageSalary?.setAdapter(adapter_manageSalary)
        }
    }

}
