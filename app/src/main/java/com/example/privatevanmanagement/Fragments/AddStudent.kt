package com.example.privatevanmanagement.Fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.database.*
import android.widget.ArrayAdapter


class AddStudent : Fragment() {
    lateinit var dialog: Dialog
    var password: String = "default123"
    var rootView: View? = null
    var StudentName: EditText? = null
    var StudentEmail: EditText? = null
    var StudentCnic: EditText? = null
    var StudentContact: EditText? = null
    var StudentAddress: EditText? = null
    var spinnerVan_Student: Spinner? = null
    var van_array: ArrayList<String> = ArrayList()
    var arrayAdapter: ArrayAdapter<String>? = null
    var van_allocated: String = ""
    var btn_StudentInfo: Button? = null
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getVanDta()
    }

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
//        mAuth = FirebaseAuth.getInstance()
        btn_StudentInfo = rootView?.findViewById(R.id.btn_StudentInfo) as Button
        StudentName = rootView?.findViewById(R.id.StudentName) as EditText
        StudentEmail = rootView?.findViewById(R.id.StudentEmail) as EditText
        StudentCnic = rootView?.findViewById(R.id.StudentCnic) as EditText
        StudentContact = rootView?.findViewById(R.id.StudentContact) as EditText
        StudentAddress = rootView?.findViewById(R.id.StudentAddress) as EditText
        spinnerVan_Student = rootView?.findViewById(R.id.spinnerVan_Student) as Spinner



        btn_StudentInfo?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                createAccount(StudentEmail!!.text.toString(), "default123")
            }
        })

        arrayAdapter = ArrayAdapter<String>(
            activity!!.applicationContext,
            android.R.layout.simple_list_item_1,
            van_array
        )

        spinnerVan_Student?.adapter = arrayAdapter
        spinnerVan_Student?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Error")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                van_allocated = parent?.getItemAtPosition(position).toString()
            }

        }

    }

    private fun getVanDta() {
        val ordersRef = Objects.getFirebaseInstance().reference.child("AddVan")
            .child(Objects.UserID.Globaluser_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.getChildren()) {
                    van_array.add(postSnapshot.key.toString())

                }
                arrayAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)

    }

    private fun createAccount(email: String, password: String) {
        Objects.getInstance()?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    databaseReference = FirebaseDatabase.getInstance().reference.child("StudentDetails")

                    val newPost =
                        databaseReference.child(Objects.getInstance().currentUser?.uid.toString())
                    newPost.push()
                    newPost.child("StudentName").setValue(StudentName!!.text.toString())
                    newPost.child("StudentEmail").setValue(StudentEmail!!.text.toString())
                    newPost.child("StudentCnic").setValue(StudentCnic!!.text.toString())
                    newPost.child("StudentContact").setValue(StudentContact!!.text.toString())
                    newPost.child("StudentAddress").setValue(StudentAddress!!.text.toString())
                    newPost.child("Van_ID").setValue(van_allocated)
                    databaseReference = Objects.getFirebaseInstance().reference.child("UserType")
                    val newPost2 =
                        databaseReference.child(Objects.getInstance().currentUser?.uid.toString())
                    newPost2.push()
                    newPost2.child("User Type").setValue("Student")

                    databaseReference = Objects.getFirebaseInstance().reference.child("Allocated_to_Student")
                    val newPost3 = databaseReference.child(van_allocated)
                    newPost3.child("Student_id").setValue(newPost.key.toString())

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
