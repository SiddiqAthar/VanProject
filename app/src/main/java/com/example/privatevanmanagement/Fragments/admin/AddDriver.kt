package com.example.privatevanmanagement.Fragments.admin

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.R
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import android.widget.*
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.privatevanmanagement.activities.AdminNav_Activity
import com.example.privatevanmanagement.models.DriverDetail_Model
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.database.*
import java.util.HashMap


class AddDriver : Fragment() {
    private val PLACE_PICKER_REQUEST = 1


    var bundle_driver_id: String? = null
    var password: String = "default123"
    var rootView: View? = null
    var mainActivity: AdminNav_Activity? = null

    var lat: String? = null
    var long: String? = null


    var DriverName: EditText? = null
    var DriverEmail: EditText? = null
    var Email_tv: TextView? = null
    var DriverCnic: EditText? = null
    var DriverContact: EditText? = null
    var DriverAddress: TextView? = null
    var spinnerVan_Driver: Spinner? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    var van_array: ArrayList<String> = ArrayList()
    var van_allocated: String = ""
    var btn_DriverInfo: Button? = null
    lateinit var databaseReference: DatabaseReference
    var mAuth: FirebaseAuth? = null
    lateinit var dialog: Dialog
    var pd: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews()
        }
        rootView = inflater!!.inflate(
            R.layout.fragment_add_driver, container,
            false
        )
        activity?.setTitle("Add Driver")
        init(rootView)
        if (arguments != null) {
            reloadData()
        }
        return rootView
    }

    private fun init(rootView: View?) {
        mAuth = FirebaseAuth.getInstance()

        mainActivity = activity as AdminNav_Activity

        btn_DriverInfo = rootView?.findViewById(R.id.btn_DriverInfo) as Button
        DriverName = rootView?.findViewById(R.id.DriverName) as EditText
        DriverEmail = rootView?.findViewById(R.id.DriverEmail) as EditText
        Email_tv = rootView?.findViewById(R.id.Email_tv) as TextView
        DriverCnic = rootView?.findViewById(R.id.DriverCnic) as EditText
        DriverContact = rootView?.findViewById(R.id.DriverContact) as EditText
        DriverAddress = rootView?.findViewById(R.id.DriverAddress) as TextView
        spinnerVan_Driver = rootView?.findViewById(R.id.spinnerVan_Driver) as Spinner


        btn_DriverInfo?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (arguments == null) {
                    if (validateForm()) {
                        // add new
                        pd = ProgressDialog(context)
                        pd!!.setMessage("Adding Driver Info")
                        pd!!.setCancelable(false)
                        pd!!.show()
                        createAccount(DriverEmail!!.text.toString(), "default123")
                    }
                } else {
                    //update existing
                    updateAccount(bundle_driver_id.toString())

                }
            }
        })

        DriverAddress?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onAddPlaceButtonClicked()
            }
        })

        arrayAdapter = ArrayAdapter<String>(
            activity!!.applicationContext,
            android.R.layout.simple_list_item_1,
            van_array
        )

        spinnerVan_Driver?.adapter = arrayAdapter
        spinnerVan_Driver?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun reloadData() {
        //visibility gone due to no need of change Email
        DriverEmail!!.visibility = View.GONE
        Email_tv!!.visibility = View.GONE
        btn_DriverInfo!!.text = "Update Detail"
        DriverName!!.isEnabled = false



        if (arguments!!.containsKey("driver_id")) {
            bundle_driver_id = arguments!!.getString("driver_id")
        }
        if (arguments!!.containsKey("driver_name")) {
            DriverName!!.setText(arguments!!.getString("driver_name"))
        }
        if (arguments!!.containsKey("driver_cnic")) {
            DriverCnic!!.setText(arguments!!.getString("driver_cnic"))
        }
        if (arguments!!.containsKey("driver_contact")) {
            DriverContact!!.setText(arguments!!.getString("driver_contact"))
        }
        if (arguments!!.containsKey("driver_address")) {
            DriverAddress!!.setText(arguments!!.getString("driver_address"))
        }
    }

    private fun createAccount(email: String, password: String) {
        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    databaseReference =
                        Objects.getFirebaseInstance().reference.child("DriverDetails")
                    val newPost =
                        databaseReference.child(Objects.getInstance().currentUser?.uid.toString())
                    newPost.setValue(
                        DriverDetail_Model(
                            Objects.getInstance().currentUser?.uid.toString(),
                            lat,
                            long,
                            DriverName!!.text.toString(),
                            DriverEmail!!.text.toString(),
                            DriverCnic!!.text.toString(),
                            DriverContact!!.text.toString(),
                            DriverAddress!!.text.toString(),
                            "",
                            "Un-Paid",
                            "5000",
                            "UnBlocked",
                            "",
                            "",
                            "",
                            ""
                        )
                    )

                    databaseReference = FirebaseDatabase.getInstance().reference.child("UserType")
                    val newPost2 =
                        databaseReference.child(Objects.getInstance().currentUser?.uid.toString())
                    newPost2.push()
                    newPost2.child("User Type").setValue("Driver")

                    Toast.makeText(
                        activity, "Authentication Succedd.",
                        Toast.LENGTH_SHORT
                    ).show()
                    showDialog(
                        "Send Credentials to User",
                        DriverEmail?.text.toString()
                    )

                } else {
                    Toast.makeText(
                        activity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                pd!!.dismiss()
            }

    }


    private fun updateAccount(driver_id: String) {
        val ref = Objects.getFirebaseInstance().reference.child("DriverDetails").child(driver_id)
        val updates = HashMap<String, Any>()

        if (!DriverCnic!!.text.toString().isNullOrEmpty())
            updates["driver_cnic"] = DriverCnic!!.text.toString()
        if (!DriverContact!!.text.toString().isNullOrEmpty())
            updates["driver_contact"] = DriverContact!!.text.toString()
        if (!DriverAddress!!.text.toString().isNullOrEmpty())
            updates["driver_address"] = DriverAddress!!.text.toString()

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
        val dialog = Dialog(activity!!)
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
                mainActivity!!.replaceFragment(Admin_home(), null)

            } else {
                dialog.dismiss()
                Toast.makeText(activity, "Enter Valid Email", Toast.LENGTH_LONG)


            }
        }
        dialogcancel.setOnClickListener {
            dialog.dismiss()
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

        if (TextUtils.isEmpty(DriverName!!.text.toString())) {
            DriverName!!.error = "Name Required"
            valid = false
        } else {
            DriverName!!.error = null
        }
        if (TextUtils.isEmpty(DriverEmail!!.text.toString())) {
            DriverEmail!!.error = "Email Required"
            valid = false
        } else {
            DriverEmail!!.error = null
        }
        if (TextUtils.isEmpty(DriverCnic!!.text.toString())) {
            DriverCnic!!.error = "Cnic Required"
            valid = false
        } else {
            DriverCnic!!.error = null
        }
        if (TextUtils.isEmpty(DriverContact!!.text.toString())) {
            DriverContact!!.error = "Contact Required"
            valid = false
        } else {
            DriverContact!!.error = null
        }
        if (DriverAddress!!.text.toString().equals("")) {
            DriverAddress!!.error = "Address Required"
            valid = false
        } else {
            DriverAddress!!.error = null
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
                DriverAddress!!.setText(place.address.toString())
                DriverAddress!!.error = null
                lat = place.latLng!!.latitude.toString()
                long = place.latLng!!.longitude.toString()
            }
        }
    }
}

