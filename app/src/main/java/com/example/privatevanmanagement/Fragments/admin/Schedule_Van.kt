package com.example.privatevanmanagement.Fragments.admin

import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.AdminNav_Activity
import com.example.privatevanmanagement.adapters.Adapter_scheduleVan
import com.example.privatevanmanagement.adapters.Spinner_Adapter
import com.example.privatevanmanagement.models.Shift_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.*
import java.util.*


class Schedule_Van : Fragment() {

    private var rootView: View? = null
    private var group_Spinner: Spinner? = null
    private var group_spinner_adapter: ArrayAdapter<String>? = null
    private var shift_Spinner: Spinner? = null
    private lateinit var shift_spinner_adapter: Adapter
    private var radioGroup: RadioGroup? = null
    private var schedule_rv: RecyclerView? = null
    private var adapter: Adapter_scheduleVan? = null
    var btn_schedule: Button? = null
    var selected_service = "pick"
    var selected_group = ""
    var selected_shift = ""


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
        radioGroup = view!!.findViewById(R.id.radioGroup) as RadioGroup
        shift_Spinner = view!!.findViewById(R.id.shit_Spinner) as Spinner
        group_Spinner = view!!.findViewById(R.id.group_Spinner) as Spinner
        group_Spinner = view!!.findViewById(R.id.group_Spinner) as Spinner
        btn_schedule = view!!.findViewById(R.id.btn_schedule) as Button

        group_spinner_adapter = ArrayAdapter<String>(
            this!!.context!!, android.R.layout.simple_spinner_item,
            group_list!!
        )
        group_spinner_adapter?.setDropDownViewResource(R.layout.spinner_item)
        group_Spinner?.adapter = group_spinner_adapter


        shift_spinner_adapter = Spinner_Adapter(
            this!!.context!!, R.id.spinner_item_tv, R.id.spinner_item_tv,
            Objects.shift_list as ArrayList<Shift_Model>?
        )
        shift_Spinner?.adapter = shift_spinner_adapter as Spinner_Adapter


        shift_Spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println(" Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selected_shift = (parent?.getItemAtPosition(position) as Shift_Model).shift_name
                updateList(selected_service)
            }
        }

        group_Spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println(" Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selected_group = parent?.getItemAtPosition(position).toString()
                updateList(selected_service)
            }
        }

        radioGroup!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.radio_pirates) {
                selected_service = "pick"
                updateList(selected_service) //pick service
            } else if (i == R.id.radio_ninjas) {
                selected_service = "drop"
                updateList(selected_service)//drop service
            }
        })


        schedule_rv?.setLayoutManager(LinearLayoutManager(activity))
        schedule_rv?.setNestedScrollingEnabled(false)
        schedule_rv?.setHasFixedSize(true)
        schedule_rv?.addItemDecoration(
            DividerItemDecoration(
                schedule_rv!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        btn_schedule!!.setOnClickListener(View.OnClickListener {

            if (sortedList!!.size > 0) {
                var activity: AdminNav_Activity = activity as AdminNav_Activity
                val bundle = Bundle()
                bundle.putString("selected_shift", selected_shift)
                activity.replaceFragment(Allocate_toStudent(), bundle)
            } else {
                Toast.makeText(context, "No Student to Allocate Van", Toast.LENGTH_SHORT).show()
            }
        })


    }


    private fun updateList(status: String) {
        sortedList.clear()
        if (status.equals("pick")) // by default its status is to "Pick"
        {
            for (i in 0 until student_modelList.size) {
                if (student_modelList.get(i).group.equals(selected_group) && student_modelList.get(i).shift_time.equals(
                        selected_shift
                    ) && student_modelList.get(i).allocated_van.equals("") && student_modelList.get(
                        i
                    ).arrival.equals("")
                ) {
                    sortedList!!.add(student_modelList.get(i))
                }
            }
        } else {
            for (i in 0 until student_modelList.size) {
                if (student_modelList.get(i).group.equals(selected_group) && student_modelList.get(i).drop_time.equals(
                        selected_shift
                    ) && student_modelList.get(i).allocated_van.equals("") && student_modelList.get(
                        i
                    ).arrival.equals("dropped")
                ) {
                    sortedList!!.add(student_modelList.get(i))
                }
            }
        }
        adapter = Adapter_scheduleVan(sortedList, activity)
        adapter!!.notifyDataSetChanged()
        schedule_rv?.setAdapter(adapter)
    }

}
