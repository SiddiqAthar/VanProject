package com.example.privatevanmanagement.Fragments.admin

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.AdminNav_Activity
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.group_list
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.custom_trip_student.*

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

    var lat: String? = null
    var long: String? = null

    var rootView: View? = null
    var Email_tv: TextView? = null
    var StudentName: EditText? = null
    var StudentEmail: EditText? = null
    var StudentCnic: EditText? = null
    var StudentContact: EditText? = null
    var StudentAddress: TextView? = null
    var group_Spinner: Spinner? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    var group: String? = null
    var btn_StudentInfo: Button? = null
    var pd: ProgressDialog? = null
    lateinit var databaseReference: DatabaseReference

    private val PLACE_PICKER_REQUEST = 1

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
        StudentAddress = rootView?.findViewById(R.id.StudentAddress) as TextView
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

        StudentAddress!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onAddPlaceButtonClicked()
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
                            lat,
                            long,
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
        if (TextUtils.isEmpty(StudentAddress!!.text.toString())) {
            StudentAddress!!.error = "Address Required"
            valid = false
        } else {
            StudentAddress!!.error = null
        }

        return valid
    }

    fun onAddPlaceButtonClicked() {
        if (ActivityCompat.checkSelfPermission(
                this!!.context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this!!.context!!, "Permission_message",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        try {
            val builder = PlacePicker.IntentBuilder()
            val i = builder.build(activity)
            startActivityForResult(
                i,
                PLACE_PICKER_REQUEST
            )
        } catch (e: GooglePlayServicesRepairableException) {
            Log.e(
                "Tag1",
                String.format("GooglePlayServices Not Available [%s]", e.message)
            )
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.e(
                "Tag2",
                String.format("GooglePlayServices Not Available [%s]", e.message)
            )
        } catch (e: java.lang.Exception) {
            Log.e(
                "Tag3",
                String.format("PlacePicker Exception: %s", e.message)
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place: Place = PlacePicker.getPlace(data, this.context)
                StudentAddress!!.setText(place.address.toString())
                lat = place.latLng!!.latitude.toString()
                long = place.latLng!!.longitude.toString()
            }
        }
    }


}
