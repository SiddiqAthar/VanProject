package com.example.privatevanmanagement.Fragments.admin

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
import android.text.TextUtils
import android.widget.*
import android.widget.TextView
import com.example.privatevanmanagement.activities.NavDrawer
import com.example.privatevanmanagement.models.DriverDetail_Model
import com.google.firebase.database.*


class AddDriver : Fragment() {
    var bundle_driver_id: String? = null
    var bundle_driver_name: String? = null
    var password: String = "default123"
    var rootView: View? = null
    var mainActivity: NavDrawer? = null
    var DriverName: EditText? = null
    var DriverEmail: EditText? = null
    var DriverCnic: EditText? = null
    var DriverContact: EditText? = null
    var DriverAddress: EditText? = null
    var spinnerVan_Driver: Spinner? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    var van_array: ArrayList<String> = ArrayList()
    var van_allocated: String = ""
    var btn_DriverInfo: Button? = null
    lateinit var databaseReference: DatabaseReference
    var mAuth: FirebaseAuth? = null
    lateinit var dialog: Dialog
    var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        getVanDta()
    }

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
            if (arguments!!.containsKey("driver_id")) {
                bundle_driver_id = arguments!!.getString("driver_id")
            }
            if (arguments!!.containsKey("driver_name")) {
                bundle_driver_name = arguments!!.getString("driver_name")
            }
            // update wali side sy a rha hai, to update k mutabiq kam karna hai ab uska
            btn_DriverInfo!!.text = "Update Detail"
            DriverName!!.isEnabled = false
            DriverName?.setText(bundle_driver_name)
        }
        return rootView
    }

    private fun init(rootView: View?) {
        mAuth = FirebaseAuth.getInstance()

        mainActivity = activity as NavDrawer

        btn_DriverInfo = rootView?.findViewById(R.id.btn_DriverInfo) as Button
        DriverName = rootView?.findViewById(R.id.DriverName) as EditText
        DriverEmail = rootView?.findViewById(R.id.DriverEmail) as EditText
        DriverCnic = rootView?.findViewById(R.id.DriverCnic) as EditText
        DriverContact = rootView?.findViewById(R.id.DriverContact) as EditText
        DriverAddress = rootView?.findViewById(R.id.DriverAddress) as EditText
        spinnerVan_Driver = rootView?.findViewById(R.id.spinnerVan_Driver) as Spinner


        btn_DriverInfo?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (validateForm()) {
                    if (bundle_driver_name.isNullOrEmpty()) {
                        // add new
                        pd = ProgressDialog(context)
                        pd!!.setMessage("Adding Driver Info")
                        pd!!.setCancelable(false)
                        pd!!.show()
                        createAccount(DriverEmail!!.text.toString(), "default123")

                    } else {
                        //update existing
                        Toast.makeText(activity, "Update Successfully", Toast.LENGTH_SHORT).show();
                        // at end go to main Home
                        mainActivity!!.replaceFragment(Admin_home(), null)
                    }


                }
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

    /*  private fun getVanDta() {
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

      }*/

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
                            "0.0",
                            "0.0",
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
        if (TextUtils.isEmpty(DriverAddress!!.text.toString())) {
            DriverAddress!!.error = "Address Required"
            valid = false
        } else {
            DriverAddress!!.error = null
        }

        return valid
    }
}


;
//    fun openNew(fragment: Fragment) {
//        val fragmentManager = activity?.getSupportFragmentManager()
//        val fragmentTransaction = fragmentManager?.beginTransaction()
//        if (fragmentTransaction != null) {
//            fragmentTransaction.replace(R.id.mlayout, fragment)
//            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .addToBackStack("std").commit()
//        }
//    }
