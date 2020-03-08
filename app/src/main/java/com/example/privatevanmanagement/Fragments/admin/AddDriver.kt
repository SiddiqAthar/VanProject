package com.example.privatevanmanagement.Fragments.admin

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.R
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.widget.*
import android.widget.TextView
import com.example.privatevanmanagement.activities.NavDrawer
import com.google.firebase.database.*


class AddDriver : Fragment() {
    var password: String = "default123"
    var rootView: View? = null
    var mainActivity:NavDrawer?=null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getVanDta()
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

        return rootView
    }

    private fun init(rootView: View?) {
        mAuth = FirebaseAuth.getInstance()

        mainActivity=activity as NavDrawer

        btn_DriverInfo = rootView?.findViewById(R.id.btn_DriverInfo) as Button
        DriverName = rootView?.findViewById(R.id.DriverName) as EditText
        DriverEmail = rootView?.findViewById(R.id.DriverEmail) as EditText
        DriverCnic = rootView?.findViewById(R.id.DriverCnic) as EditText
        DriverContact = rootView?.findViewById(R.id.DriverContact) as EditText
        DriverAddress = rootView?.findViewById(R.id.DriverAddress) as EditText
        spinnerVan_Driver = rootView?.findViewById(R.id.spinnerVan_Driver) as Spinner


        btn_DriverInfo?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(activity, "Add Successfully", Toast.LENGTH_SHORT).show();
                mainActivity!!.replaceFragment(Admin_home(),null)

//                createAccount(DriverEmail!!.text.toString(), password)
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
        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    databaseReference =
                        FirebaseDatabase.getInstance().reference.child("DriverDetails")

                    val newPost =
                        databaseReference.child(Objects.getInstance().currentUser?.uid.toString())
                    newPost.push()
                    newPost.child("DriverName").setValue(DriverName!!.text.toString())
                    newPost.child("DriverEmail").setValue(DriverEmail!!.text.toString())
                    newPost.child("DriverCnic").setValue(DriverCnic!!.text.toString())
                    newPost.child("DriverContact").setValue(DriverContact!!.text.toString())
                    newPost.child("DriverAddress").setValue(DriverAddress!!.text.toString())
                    newPost.child("Van_ID").setValue(van_allocated)

                    databaseReference = FirebaseDatabase.getInstance().reference.child("UserType")
                    val newPost2 =
                        databaseReference.child(Objects.getInstance().currentUser?.uid.toString())
                    newPost2.push()
                    newPost2.child("User Type").setValue("Driver")

                    databaseReference =
                        Objects.getFirebaseInstance().reference.child("Allocated_to_Driver")
                    val newPost3 = databaseReference.child(van_allocated)
                    newPost3.child("Driver_id").setValue(newPost.key.toString())


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
