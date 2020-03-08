package com.example.privatevanmanagement.Fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privatevanmanagement.Adapters.Adapter_scheduleVan

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.NavDrawer
import com.example.privatevanmanagement.models.Schedule_Student_Model
import java.util.ArrayList

class Schedule_Van : Fragment() {

    private var rootView: View? = null

    private var group_Spinner: Spinner? = null
    private var group_spinner_adapter: ArrayAdapter<String>? = null
    var ArrayList_group: ArrayList<String>? = null

    private var shift_Spinner: Spinner? = null
    private var shift_spinner_adapter: ArrayAdapter<String>? = null
    var ArrayList_shift: ArrayList<String>? = null


    private var schedule_rv: RecyclerView? = null

    private var adapter: Adapter_scheduleVan? = null
    var ArrayList_schedule: ArrayList<Schedule_Student_Model>? = null

    var btn_schedule: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_schedule__van, container, false)
        activity?.setTitle("Schedule Vans")

        init(rootView)

        return rootView
    }

    private fun init(view: View?) {
        schedule_rv = view!!.findViewById(R.id.rv_Schedule) as RecyclerView
        shift_Spinner = view!!.findViewById(R.id.shit_Spinner) as Spinner
        group_Spinner = view!!.findViewById(R.id.group_Spinner) as Spinner
        btn_schedule = view!!.findViewById(R.id.btn_schedule) as Button

        ArrayList_schedule = ArrayList<Schedule_Student_Model>()

        ArrayList_schedule!!.add(Schedule_Student_Model("", "Hamza", "1st Shift"))
        ArrayList_schedule!!.add(Schedule_Student_Model("", "Siddiq", "1st Shift"))
        ArrayList_schedule!!.add(Schedule_Student_Model("", "Raza", "1st Shift"))
        ArrayList_schedule!!.add(Schedule_Student_Model("", "Zohaib", "1st Shift"))
        ArrayList_schedule!!.add(Schedule_Student_Model("", "Sheryar", "1st Shift"))
        ArrayList_schedule!!.add(Schedule_Student_Model("", "Hamza", "1st Shift"))
        ArrayList_schedule!!.add(Schedule_Student_Model("", "Siddiq", "1st Shift"))
        ArrayList_schedule!!.add(Schedule_Student_Model("", "Raza", "1st Shift"))
        ArrayList_schedule!!.add(Schedule_Student_Model("", "Zohaib", "1st Shift"))
        ArrayList_schedule!!.add(Schedule_Student_Model("", "Sheryar", "1st Shift"))


        ArrayList_group = ArrayList<String>()
        ArrayList_group!!.add("Group 1")
        ArrayList_group!!.add("Group 2")
        ArrayList_group!!.add("Group 3")

        group_spinner_adapter = ArrayAdapter<String>(
            this!!.context!!, android.R.layout.simple_spinner_item,
            ArrayList_group!!
        )
        group_spinner_adapter?.setDropDownViewResource(R.layout.spinner_item)
        group_Spinner?.adapter = group_spinner_adapter

        ArrayList_shift = ArrayList<String>()
        ArrayList_shift!!.add("Shift 1")
        ArrayList_shift!!.add("Shift 2")
        ArrayList_shift!!.add("Shift 3")

        shift_spinner_adapter = ArrayAdapter<String>(
            this!!.context!!, android.R.layout.simple_spinner_item,
            ArrayList_shift!!
        )
        shift_spinner_adapter?.setDropDownViewResource(R.layout.spinner_item)
        shift_Spinner?.adapter = shift_spinner_adapter



        schedule_rv?.setLayoutManager(LinearLayoutManager(activity))
        schedule_rv?.setNestedScrollingEnabled(false)
        schedule_rv?.setHasFixedSize(true)
        schedule_rv?.addItemDecoration(
            DividerItemDecoration(
                schedule_rv!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        adapter = Adapter_scheduleVan(ArrayList_schedule, activity)
        schedule_rv?.setAdapter(adapter)



        btn_schedule!!.setOnClickListener(View.OnClickListener {
            var activity: NavDrawer =activity as NavDrawer

            activity.replaceFragment(Allocate_toStudent(),null)
        })


    }

}
