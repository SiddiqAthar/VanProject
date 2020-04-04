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
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.student_modelList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


public class Admin_ManageStudents : Fragment() {

    var rootView: View? = null
    private var rv_manageStudent: RecyclerView? = null
    private var adapter_manageStudent: Adapter_manageStudent? = null
/*
    var ArrayList_Model: ArrayList<ManageFee_Model>? = null
*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_admin__manage_student, container, false)
        activity?.setTitle("Manage Students")
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        rv_manageStudent = rootView?.findViewById(R.id.rv_manageStudent)


        rv_manageStudent?.setLayoutManager(LinearLayoutManager(activity))
        rv_manageStudent?.setNestedScrollingEnabled(false)
        rv_manageStudent?.setHasFixedSize(true)
        rv_manageStudent?.addItemDecoration(
            DividerItemDecoration(
                rv_manageStudent!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        if (student_modelList.isNullOrEmpty()) // agr list empty hai to jae
            {
                val adminHome = Admin_home()
                adminHome.getStudentList()
                adapter_manageStudent = Adapter_manageStudent(student_modelList, activity)
                adapter_manageStudent!!.notifyDataSetChanged()
                rv_manageStudent?.setAdapter(adapter_manageStudent)
            }
            else {
            adapter_manageStudent = Adapter_manageStudent(student_modelList, activity)
            rv_manageStudent?.setAdapter(adapter_manageStudent)
        }
    }

}
