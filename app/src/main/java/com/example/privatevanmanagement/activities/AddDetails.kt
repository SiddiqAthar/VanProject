package com.example.privatevanmanagement.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.UserID.Globaluser_ID
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddDetails : AppCompatActivity() {

    var AdminName: EditText? = null
    var AdminCnic: EditText? = null
    var AdminContact: EditText? = null
    var AdminAddress: EditText? = null
    var btn_AdminInfo: Button? = null
    lateinit var databaseReference:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_details)

        btn_AdminInfo = findViewById(R.id.btn_AdminInfo) as Button
        AdminName = findViewById(R.id.AdminName) as EditText
        AdminCnic = findViewById(R.id.AdminCnic) as EditText
        AdminContact = findViewById(R.id.AdminContact) as EditText
        AdminAddress = findViewById(R.id.AdminAddress) as EditText

        databaseReference = FirebaseDatabase.getInstance().reference.child("AdminDetails")

//        Objects.getDBInstance().child("Detail")
        btn_AdminInfo?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val newPost = databaseReference.child(Globaluser_ID)
                newPost.push()
                newPost.child("AdminName").setValue(AdminName!!.text.toString())
                newPost.child("AdminCnic").setValue(AdminCnic!!.text.toString())
                newPost.child("AdminContact").setValue(AdminContact!!.text.toString())
                newPost.child("AdminAddress").setValue(AdminAddress!!.text.toString())
                startActivity(Intent(this@AddDetails, NavDrawer::class.java))
            }})

    }
}
