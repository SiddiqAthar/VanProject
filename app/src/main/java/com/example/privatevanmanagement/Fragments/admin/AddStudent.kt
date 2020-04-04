package com.example.privatevanmanagement.Fragments.admin

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.example.privatevanmanagement.activities.AdminNav_Activity
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects.group_list
 import com.google.android.libraries.places.widget.AutocompleteSupportFragment
 import android.util.Log
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class AddStudent : Fragment() {
    lateinit var dialog: Dialog

    var password: String? = "default123"
    var selectedGroup: String? = null

    var bundle_student_id: String? = null
    var bundle_student_name: String? = null
    var bundle_student_contact: String? = null
    var bundle_student_cnic: String? = null
    var bundle_student_address: String? = null

    var rootView: View? = null
    var Email_tv: TextView? = null
    var StudentName: EditText? = null
    var StudentEmail: EditText? = null
    var StudentCnic: EditText? = null
    var StudentContact: EditText? = null
    var StudentAddress: EditText? = null
    var group_Spinner: Spinner? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    var group: String? = null
    var btn_StudentInfo: Button? = null
    var pd: ProgressDialog? = null
    lateinit var databaseReference: DatabaseReference

    var mainActivity: AdminNav_Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        getVanDta2()
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
        activity?.setTitle("Add Student")

        init(rootView)

        if (arguments != null) {
            reloadData()
        }


        return rootView
    }


    private fun init(rootView: View?) {
//        mAuth = FirebaseAuth.getInstance()

        mainActivity = activity as AdminNav_Activity

        btn_StudentInfo = rootView?.findViewById(R.id.btn_StudentInfo) as Button
        Email_tv = rootView?.findViewById(R.id.Email_tv) as TextView
        StudentName = rootView?.findViewById(R.id.StudentName) as EditText
        StudentEmail = rootView?.findViewById(R.id.StudentEmail) as EditText
        StudentCnic = rootView?.findViewById(R.id.StudentCnic) as EditText
        StudentContact = rootView?.findViewById(R.id.StudentContact) as EditText
        StudentAddress = rootView?.findViewById(R.id.StudentAddress) as EditText
        group_Spinner = rootView?.findViewById(R.id.group_Spinner) as Spinner



        btn_StudentInfo?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //                createAccount(StudentEmail!!.text.toString(), "default123")
                if (arguments == null) {
                    if (validateForm()) {
                        // add new
                        pd = ProgressDialog(context)
                        pd!!.setMessage("Adding Student Info")
                        pd!!.setCancelable(false)
                        pd!!.show()
                        createAccount(StudentEmail!!.text.toString(), "default123")

                    }
                } else {
                    //update existing
                    updateAccount(bundle_student_id!!)
                 }
            }
        })

        arrayAdapter = ArrayAdapter<String>(
            activity!!.applicationContext,
            android.R.layout.simple_list_item_1,
            group_list
        )

        group_Spinner?.adapter = arrayAdapter
        group_Spinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("Error")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    group = parent?.getItemAtPosition(position).toString()
                }

            }


        val autocompleteFragment = (activity as AdminNav_Activity).getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME))

// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("YES", "Place: " + place.getName() + ", " + place.getId())
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("cs", "An error occurred: $status")
            }
        })

    }

    private fun reloadData() {
        //visibility gone due to no need of change Email
        StudentEmail!!.visibility = View.GONE
        Email_tv!!.visibility = View.GONE

        if (arguments!!.containsKey("student_id")) {
            bundle_student_id = arguments!!.getString("student_id")
        }
        if (arguments!!.containsKey("student_name")) {
            bundle_student_name = arguments!!.getString("student_name")
        }
        if (arguments!!.containsKey("student_cnic")) {
            bundle_student_cnic = arguments!!.getString("student_cnic")
        }
        if (arguments!!.containsKey("student_contact")) {
            bundle_student_contact = arguments!!.getString("student_contact")
        }
        if (arguments!!.containsKey("student_address")) {
            bundle_student_address = arguments!!.getString("student_address")
        }

        // update wali side sy a rha hai, to update k mutabiq kam karna hai ab uska
        btn_StudentInfo!!.text = "Update Detail"
        StudentName!!.isEnabled = false
        StudentName?.setText(bundle_student_name)
        StudentCnic?.setText(bundle_student_cnic)
        StudentContact?.setText(bundle_student_contact)
        StudentAddress?.setText(bundle_student_address)
    }

    private fun createAccount(email: String, password: String) {
        Objects.getInstance()?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    databaseReference =
                        Objects.getFirebaseInstance().reference.child("StudentDetails")
                    val newPost =
                        databaseReference.child(Objects.getInstance().currentUser?.uid.toString())

                    newPost.setValue(
                        StudentDetail_Model(
                            Objects.getInstance().currentUser?.uid.toString(),
                            "0.0",
                            "0.0",
                            StudentName!!.text.toString(),
                            StudentEmail!!.text.toString(),
                            StudentCnic!!.text.toString(),
                            StudentContact!!.text.toString()
                            ,
                            StudentAddress!!.text.toString(),
                            group,
                            "Un-Paid",
                            "2000",
                            "UnBlocked",
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                    )


                    /*     newPost.push()
                         newPost.child("StudentName").setValue(StudentName!!.text.toString())
                         newPost.child("StudentEmail").setValue(StudentEmail!!.text.toString())
                         newPost.child("StudentCnic").setValue(StudentCnic!!.text.toString())
                         newPost.child("StudentContact").setValue(StudentContact!!.text.toString())
                         newPost.child("StudentAddress").setValue(StudentAddress!!.text.toString())
                         newPost.child("Group").setValue(group)*/


                    databaseReference = Objects.getFirebaseInstance().reference.child("UserType")

                    val newPost2 =
                        databaseReference.child(Objects.getInstance().currentUser?.uid.toString())
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
                pd?.dismiss()
            }
    }

    private fun updateAccount(student_id: String) {


        val ref = Objects.getFirebaseInstance().reference.child("StudentDetails").child(student_id)
        val updates = HashMap<String, Any>()


        if (!StudentName!!.text.toString().isNullOrEmpty())
            updates["student_name"] = StudentName!!.text.toString()
        if (!StudentCnic!!.text.toString().isNullOrEmpty())
            updates["student_cnic"] = StudentCnic!!.text.toString()
        if (!StudentContact!!.text.toString().isNullOrEmpty())
            updates["student_contact"] = StudentContact!!.text.toString()
        if (!StudentAddress!!.text.toString().isNullOrEmpty())
            updates["student_address"] = StudentAddress!!.text.toString()
        if (!group.isNullOrEmpty())
            updates["group"] = group.toString()

        if (updates.size > 0) {
            ref.updateChildren(updates)
            Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show()
            // at end go to main Home
            mainActivity!!.replaceFragment(Admin_home(), null)
        } else {
            Toast.makeText(context, "Nothing to update", Toast.LENGTH_SHORT).show()
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
                // at end go to main Home
                mainActivity!!.replaceFragment(Admin_home(), null)
            } else {
                dialog.dismiss()
                Toast.makeText(activity, "Enter Valid Email", Toast.LENGTH_LONG)
            }
        }
        dialogcancel.setOnClickListener {
            dialog.dismiss()
            // at end go to main Home
            mainActivity!!.replaceFragment(Admin_home(), null)
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

    private fun validateForm(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(StudentName!!.text.toString())) {
            StudentName!!.error = "Name Required"
            valid = false
        } else {
            StudentName!!.error = null
        }
        if (TextUtils.isEmpty(StudentEmail!!.text.toString())) {
            StudentEmail!!.error = "Email Required"
            valid = false
        } else {
            StudentEmail!!.error = null
        }
        if (TextUtils.isEmpty(StudentCnic!!.text.toString())) {
            StudentCnic!!.error = "Cnic Required"
            valid = false
        } else {
            StudentCnic!!.error = null
        }
        if (TextUtils.isEmpty(StudentContact!!.text.toString())) {
            StudentContact!!.error = "Contact Required"
            valid = false
        } else {
            StudentContact!!.error = null
        }

        return valid
    }


}
