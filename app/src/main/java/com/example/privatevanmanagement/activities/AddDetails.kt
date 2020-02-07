package com.example.privatevanmanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects

class AddDetails : AppCompatActivity() {

    var AdminName: EditText? = null
    var AdminCnic: EditText? = null
    var AdminContact: EditText? = null
    var AdminAddress: EditText? = null
    var btn_AdminInfo: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_details)

        btn_AdminInfo = findViewById(R.id.btn_AdminInfo) as Button
        AdminName = findViewById(R.id.AdminName) as EditText
        AdminCnic = findViewById(R.id.AdminCnic) as EditText
        AdminContact = findViewById(R.id.AdminContact) as EditText
        AdminAddress = findViewById(R.id.AdminAddress) as EditText

        Objects.getDBInstance().child("AdminDetail")
        btn_AdminInfo?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Objects.getDBInstance().push()
                Objects.getDBInstance().child("AdminName").setValue(AdminName!!.text.toString())
                Objects.getDBInstance().child("AdminCnic").setValue(AdminCnic!!.text.toString())
                Objects.getDBInstance().child("AdminContact").setValue(AdminContact!!.text.toString())
                Objects.getDBInstance().child("AdminAddress").setValue(AdminAddress!!.text.toString())
            }})

    }
}
