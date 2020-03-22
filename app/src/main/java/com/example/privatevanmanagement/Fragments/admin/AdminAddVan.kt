package com.example.privatevanmanagement.Fragments.admin

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.AdminNav_Activity
import com.example.privatevanmanagement.models.VanDetail_Model
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AdminAddVan : Fragment() {
    var mainActivity: AdminNav_Activity? = null

    var rootView: View? = null

    var van_Registeration: EditText? = null
    var van_Model: EditText? = null
    var van_Make: EditText? = null
    var van_Color: EditText? = null
    var van_Type: EditText? = null
    var van_Capacity: EditText? = null

    var btn_AddVan: Button? = null
    lateinit var databaseReference: DatabaseReference

    var pd: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews()
        }
        rootView = inflater!!.inflate(
            R.layout.fragment_add_van, container,
            false
        )
        activity?.setTitle("Add Van")


        init(rootView)

        return rootView
    }

    private fun init(rootView: View?) {

        mainActivity = activity as AdminNav_Activity

        btn_AddVan = rootView?.findViewById(R.id.btn_AddVan) as Button
        van_Registeration = rootView?.findViewById(R.id.van_Registeration) as EditText
        van_Model = rootView?.findViewById(R.id.van_Model) as EditText
        van_Make = rootView?.findViewById(R.id.van_Make) as EditText
        van_Color = rootView?.findViewById(R.id.van_Color) as EditText
        van_Type = rootView?.findViewById(R.id.van_Type) as EditText
        van_Capacity = rootView?.findViewById(R.id.van_Capacity) as EditText



        btn_AddVan?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (validateForm()) {
                    pd = ProgressDialog(context)
                    pd!!.setMessage("Adding Van Info")
                    pd!!.setCancelable(false)
                    pd!!.show()
                    addVandetails()
                    Toast.makeText(activity, "Add Successfully", Toast.LENGTH_SHORT).show();
                    mainActivity!!.replaceFragment(Admin_home(), null)
                }
            }
        })
    }

    private fun addVandetails() {
        databaseReference = FirebaseDatabase.getInstance().reference.child("AddVan").push()
        databaseReference.setValue(
            VanDetail_Model(
                databaseReference.key,
                van_Registeration!!.text.toString(),
                van_Model!!.text.toString(),
                van_Make!!.text.toString(),
                van_Color!!.text.toString(),
                van_Type!!.text.toString(),
                van_Capacity!!.text.toString(),
                "",
                "",
                ""
            )
        )
        pd!!.dismiss()
    }

    private fun validateForm(): Boolean {
        var valid = true
        if (TextUtils.isEmpty(van_Registeration!!.text.toString())) {
            van_Registeration!!.error = "Van Number Required"
            valid = false
        } else {
            van_Registeration!!.error = null
        }
        if (TextUtils.isEmpty(van_Model!!.text.toString())) {
            van_Model!!.error = "Van Model Required"
            valid = false
        } else {
            van_Model!!.error = null
        }
        if (TextUtils.isEmpty(van_Make!!.text.toString())) {
            van_Make!!.error = "Maker Required"
            valid = false
        } else {
            van_Make!!.error = null
        }
        if (TextUtils.isEmpty(van_Color!!.text.toString())) {
            van_Color!!.error = "Van Color Required"
            valid = false
        } else {
            van_Color!!.error = null
        }
        if (TextUtils.isEmpty(van_Type!!.text.toString())) {
            van_Type!!.error = "Van Type Required"
            valid = false
        } else {
            van_Type!!.error = null
        }
        if (TextUtils.isEmpty(van_Capacity!!.text.toString())) {
            van_Capacity!!.error = "Van Capacity Required"
            valid = false
        } else {
            van_Capacity!!.error = null
        }

        return valid
    }
}

