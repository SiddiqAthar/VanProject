package com.example.privatevanmanagement.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminAddVan : Fragment() {

    var rootView: View? = null
    var vanName: EditText? = null
    var vanModel: EditText? = null
    var vanNumber: EditText? = null
    var btn_AddVan: Button? = null
    lateinit var databaseReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews()
        }
        rootView = inflater!!.inflate(
            R.layout.fragment_admin_add_van, container,
            false
        )
        init(rootView)

        return rootView
    }

    private fun init(rootView: View?) {
        btn_AddVan = rootView?.findViewById(R.id.btn_AddVan) as Button
        vanName = rootView?.findViewById(R.id.VanName) as EditText
        vanModel = rootView?.findViewById(R.id.VanModel) as EditText
        vanNumber = rootView?.findViewById(R.id.VanNumber) as EditText

        databaseReference = FirebaseDatabase.getInstance().reference.child("AddVan")

        btn_AddVan?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                addVandetails()
            }
        })
    }

    private fun addVandetails() {
        val newPost = databaseReference.child(Objects.getInstance().currentUser?.uid.toString()).push()
        newPost.child("VanName").setValue(vanName!!.text.toString())
        newPost.child("VanModel").setValue(vanModel!!.text.toString())
        newPost.child("VanNumber").setValue(vanNumber!!.text.toString())
    }
}
