package com.example.privatevanmanagement.Fragments.admin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.NavDrawer
import com.example.privatevanmanagement.adapters.Adapter_manageStudent
import com.example.privatevanmanagement.adapters.Spinner_Adapter
import com.example.privatevanmanagement.adapters.Spinner_Van_Adapter
import com.example.privatevanmanagement.models.Shift_Model
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.models.VanDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.vanList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Allocate_toStudent : Fragment() {

    private var rootView: View? = null
    var mainActivity: NavDrawer? = null
    private var vanNumber_Spinner: Spinner? = null
    private var driverName_Spinner: Spinner? = null
    private lateinit var vanNumber_adapter: Adapter
    private var driverName_adapter: ArrayAdapter<String>? = null
    //    var ArrayList_number: ArrayList<String>? = null
    var ArrayList_name: ArrayList<String>? = null
    var btn_Allocate_toStudent: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_allocate_to_student, container, false)
        activity?.setTitle("Allocate to Student")


        init(rootView)

        return rootView
    }



    private fun init(rootView: View?) {
        mainActivity = activity as NavDrawer

        vanNumber_Spinner = rootView!!.findViewById(R.id.vanNumber_Spinner) as Spinner
        driverName_Spinner = rootView!!.findViewById(R.id.driverName_Spinner) as Spinner
        btn_Allocate_toStudent = rootView!!.findViewById(R.id.btn_Allocate_toStudent) as Button

        ArrayList_name = ArrayList<String>()
        ArrayList_name!!.add("Hamza")
        ArrayList_name!!.add("Siddiq")
        ArrayList_name!!.add("Raza")
        ArrayList_name!!.add("Zohaib")
        ArrayList_name!!.add("Sheryar")


        vanNumber_adapter = Spinner_Van_Adapter(
            this!!.context!!, R.id.spinner_item_tv, R.id.spinner_item_tv,
            Objects.vanList as ArrayList<VanDetail_Model>
        )
        vanNumber_Spinner?.adapter = vanNumber_adapter as Spinner_Van_Adapter

      /*    vanNumber_adapter = Spinner_Adapter(
            this!!.context!!, R.id.spinner_item_tv, R.id.spinner_item_tv,
            Objects.shift_list as ArrayList<Shift_Model>
        )
        vanNumber_Spinner?.adapter = vanNumber_adapter as Spinner_Adapter*/



        driverName_adapter = ArrayAdapter<String>(
            this!!.context!!, android.R.layout.simple_spinner_item,
            ArrayList_name!!
        )
        driverName_adapter?.setDropDownViewResource(R.layout.spinner_item)
        driverName_Spinner?.adapter = driverName_adapter


        btn_Allocate_toStudent!!.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "Allocate Van Successfully", Toast.LENGTH_SHORT).show();
            mainActivity!!.replaceFragment(Admin_home(), null)

        })

    }

}
