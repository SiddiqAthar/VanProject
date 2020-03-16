package com.example.privatevanmanagement.Fragments.driver

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.NavDrawer
import com.example.privatevanmanagement.adapters.Adapter_comingTrip
import com.example.privatevanmanagement.models.ManageFee_Model
import java.util.ArrayList

class Driver_Coming_Trip : Fragment() {

    var rootView: View? = null
    private var rv_tripStudents: RecyclerView? = null
    private var btn_start_ride: Button? = null
    private var adapter_comingTrip: Adapter_comingTrip? = null
    var ArrayList_Model: ArrayList<ManageFee_Model>? = null
    var mainActivity: NavDrawer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_driver__coming__trip, container, false)
        init(rootView)
        return rootView
    }

    private fun init(rootView: View?) {
        mainActivity = activity as NavDrawer
        rv_tripStudents = rootView?.findViewById(R.id.rv_tripStudents)
        btn_start_ride = rootView?.findViewById(R.id.btn_start_ride)

/*
         //"Set here model according to trip and add data in list, Idr just for UI yahi model pass kar dia ")
*/


        ArrayList_Model = ArrayList<ManageFee_Model>()
        //add dummy data
        ArrayList_Model!!.add(ManageFee_Model("Street 1, G-13, Islamabad", "Siddiq", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 2, G-13, Islamabad", "Uzair", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 3, G-13, Islamabad", "Usman", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 4, G-13, Islamabad", "Zohaib", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 5, G-13, Islamabad", "Umer", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 1, G-11, Islamabad", "Ali", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 1, G-12, Islamabad", "Ahtisham", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 1, G-13, Islamabad", "Athar Iqbal", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 1, G-14, Islamabad", "Nabeeel Shoukat", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 1, G-15, Islamabad", "Abu Bakar", "", ""))
        ArrayList_Model!!.add(ManageFee_Model("Street 1, G-10, Islamabad", "Zaheer", "", ""))


        rv_tripStudents?.setLayoutManager(LinearLayoutManager(activity))
        rv_tripStudents?.setNestedScrollingEnabled(false)
        rv_tripStudents?.setHasFixedSize(true)
        rv_tripStudents?.addItemDecoration(
            DividerItemDecoration(
                rv_tripStudents!!.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        adapter_comingTrip = Adapter_comingTrip(ArrayList_Model, activity)
        rv_tripStudents?.setAdapter(adapter_comingTrip)



        btn_start_ride!!.setOnClickListener(View.OnClickListener {
            mainActivity!!.replaceFragment(Driver_Track(), null)
        })

    }
}
