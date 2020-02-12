package com.example.privatevanmanagement.Fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.BaseActivity
import com.example.privatevanmanagement.utils.Objects
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddStudent : Fragment() {
    lateinit var dialog:Dialog
    var password: String = "default123"
    var rootView: View? = null
    var StudentName: EditText? = null
    var StudentEmail: EditText? = null
    var StudentCnic: EditText? = null
    var StudentContact: EditText? = null
    var StudentAddress: EditText? = null
    var btn_StudentInfo: Button? = null
    lateinit var databaseReference: DatabaseReference
    var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews()
        }
        rootView = inflater!!.inflate(
            R.layout.fragment_add_student, container,
            false
        )
        init(rootView)

        return rootView
    }

    private fun init(rootView: View?) {
        mAuth = FirebaseAuth.getInstance()
        btn_StudentInfo = rootView?.findViewById(R.id.btn_StudentInfo) as Button
        StudentName = rootView?.findViewById(R.id.StudentName) as EditText
        StudentEmail = rootView?.findViewById(R.id.StudentEmail) as EditText
        StudentCnic = rootView?.findViewById(R.id.StudentCnic) as EditText
        StudentContact = rootView?.findViewById(R.id.StudentContact) as EditText
        StudentAddress = rootView?.findViewById(R.id.StudentAddress) as EditText

        databaseReference = FirebaseDatabase.getInstance().reference.child("StudentDetails")

        btn_StudentInfo?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                createAccount(StudentEmail!!.text.toString(), "default123")
            }
        })
    }

    private fun createAccount(email: String, password: String) {
        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    val newPost =
                        databaseReference.child(Objects.getInstance().currentUser?.uid.toString())
                    newPost.push()
                    newPost.child("StudentName").setValue(StudentName!!.text.toString())
                    newPost.child("StudentEmail").setValue(StudentEmail!!.text.toString())
                    newPost.child("StudentCnic").setValue(StudentCnic!!.text.toString())
                    newPost.child("StudentContact").setValue(StudentContact!!.text.toString())
                    newPost.child("StudentAddress").setValue(StudentAddress!!.text.toString())

                    databaseReference = FirebaseDatabase.getInstance().reference.child("UserType")
                    val newPost2 = databaseReference.child(Objects.getInstance().currentUser?.uid.toString())
                    newPost2.push()
                    newPost2.child("User Type").setValue("Student")

                    Toast.makeText(
                        activity, "Authentication Succedd.",
                        Toast.LENGTH_SHORT
                    ).show()
                    showDialog(
                        "Send Credentials to User",
                        StudentEmail?.text.toString()
                    )
                } else {
                    Toast.makeText(
                        activity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    fun sentEmail(email: String) {
        if (dialog != null) {
            dialog.dismiss()
        }
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        i.putExtra(Intent.EXTRA_SUBJECT, "Van Management Credentials")
        i.putExtra(
            Intent.EXTRA_TEXT,
            "Your User Name is your Email id:   " + email + "\nYour Password is:    " + password
        )
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(
                activity,
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    protected fun showDialog(msg: String, email: String) {
         dialog = Dialog(activity!!)
        dialog.setCancelable(true)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog, null)
        dialog.setContentView(view)
        val text = dialog.findViewById(R.id.successTextView) as TextView
        text.setText(msg)
        val dialogButton = dialog.findViewById(R.id.okButton) as Button
        dialogButton.text = "Sent Email"
        val dialogcancel = dialog.findViewById(R.id.cancelButton) as Button
        dialogcancel.text = "Cancel"
        dialogButton.setOnClickListener {
            if (!email.toString().isNullOrEmpty()) {
                sentEmail(email)
            } else {
                dialog.dismiss()
                Toast.makeText(activity, "Enter Valid Email", Toast.LENGTH_LONG)
            }
        }
        dialogcancel.setOnClickListener {
            dialog.dismiss()
        }
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

    }
}
