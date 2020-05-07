package com.example.privatevanmanagement.Fragments.admin

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.AdminNav_Activity
import com.example.privatevanmanagement.activities.LoginActivity
import com.example.privatevanmanagement.adapters.Spinner_Adapter
import com.example.privatevanmanagement.adapters.TrackVan_Spinner_Adapter
import com.example.privatevanmanagement.models.DriverDetail_Model
import com.example.privatevanmanagement.models.Shift_Model
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.models.VanDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.*
import com.example.privatevanmanagement.utils.SendNotification
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.HashMap


class Admin_home : Fragment(), View.OnClickListener {


    var mContext: Context? = null
    var mainActivity: AdminNav_Activity? = null

    var add_newStudent: CardView? = null
    var add_newDriver: CardView? = null
    var add_newVan: CardView? = null
    var add_scheduleVan: CardView? = null
    var add_TrackVan: CardView? = null
    var add_announcmnet: CardView? = null
    var pd: ProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews()
        }
        val rootView = inflater!!.inflate(R.layout.fragment_admin_home, container, false)
        mContext = rootView.context

        activity?.setTitle("PVM")

        if (student_modelList.isEmpty()) {
            pd = ProgressDialog(context)
            pd!!.setMessage("Fetching Data From Server")
            pd!!.setCancelable(false)
            pd!!.show()
            getStudentList()
            getDriverList()
            getVanData()
            getFreeVanData()
            getfreeDriverrData()
        }
        init(rootView)

        return rootView


    }

    private fun init(rootView: View?) {
        mainActivity = activity as AdminNav_Activity

        add_newStudent = rootView?.findViewById(R.id.add_newStudent) as CardView
        add_newDriver = rootView?.findViewById(R.id.add_newDriver) as CardView
        add_newVan = rootView?.findViewById(R.id.add_newVan) as CardView
        add_scheduleVan = rootView?.findViewById(R.id.add_scheduleVan) as CardView
        add_TrackVan = rootView?.findViewById(R.id.add_TrackVan) as CardView
        add_announcmnet = rootView?.findViewById(R.id.add_announcmnet) as CardView

        add_newStudent!!.setOnClickListener(this)
        add_newDriver!!.setOnClickListener(this)
        add_newVan!!.setOnClickListener(this)
        add_scheduleVan!!.setOnClickListener(this)
        add_TrackVan!!.setOnClickListener(this)
        add_announcmnet!!.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.add_newStudent) {
            mainActivity!!.replaceFragment(AddStudent(), null)


        } else if (v?.id == R.id.add_newDriver) {
            mainActivity!!.replaceFragment(AddDriver(), null)
        } else if (v?.id == R.id.add_newVan) {
            mainActivity!!.replaceFragment(AdminAddVan(), null)
        } else if (v?.id == R.id.add_scheduleVan) {
            mainActivity!!.replaceFragment(Schedule_Van(), null)
        } else if (v?.id == R.id.add_TrackVan) {
            showDialogTrackVan()
        } else if (v?.id == R.id.add_announcmnet) {
            showDialogMakeAnnouncment()
        }
    }

    fun showDialogMakeAnnouncment() {
        val dialogBuilder = AlertDialog.Builder(this!!.mContext!!)
        val inflater = activity!!.layoutInflater
        dialogBuilder.setCancelable(false)
        val dialogView = inflater.inflate(R.layout.dialog_make_announcement, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val make_Annoncment = dialogView.findViewById(R.id.make_Annoncment) as EditText
        val btn_sendNotficaion = dialogView.findViewById(R.id.btn_sendNotficaion) as Button
        val btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog) as ImageButton

        btn_sendNotficaion.setOnClickListener(View.OnClickListener {
            sendMessage(make_Annoncment.text.toString())
            //add data here on firebase and send notification
            Toast.makeText(activity, "Notification Sending", Toast.LENGTH_SHORT).show()
            if (!make_Annoncment.text.isNullOrEmpty())
                alertDialog.dismiss()
            else {
                Toast.makeText(activity, "Announcment field is empty", Toast.LENGTH_SHORT).show()
            }
        })

        btn_closeDialog.setOnClickListener(View.OnClickListener
        {
            alertDialog.dismiss()
        })

        // alertDialog.show()
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        } else {
            alertDialog.show()
            alertDialog.getWindow()!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }


    private fun sendMessage(toString: String) {
//        val myRef = Objects.getFirebaseInstance().getReference("Token").child(Globaluser_ID)
        val myRef = Objects.getFirebaseInstance().getReference("Token")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userList = ArrayList<String>()
                for (postSnapshot in snapshot.children) {
                    userList.add(postSnapshot.value.toString())
                }
                msgsend(userList, toString)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })
    }

    fun msgsend(to: ArrayList<String>, body: String) {
        var sendNoteaa = SendNotification()
        sendNoteaa.execute(to, body);
    }

    fun getStudentList() {
        val myRef = Objects.getFirebaseInstance().getReference("StudentDetails")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                student_modelList.clear()

                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(StudentDetail_Model::class.java)!!
                    student_modelList.add(listDataRef)
                    // here you can access to name property like university.name
                }
                /* adapter_manageFee = Adapter_manageFeeextends(Objects.student_modelList, activity)
                 rv_manageFee?.setAdapter(adapter_manageFee)*/
                if (pd != null && pd!!.isShowing)
                    pd!!.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
                Toast.makeText(context, "Unable to fetch data from Server", Toast.LENGTH_LONG)
                    .show()
                if (pd != null && pd!!.isShowing)
                    pd!!.dismiss()
            }
        })
    }


    fun getDriverList() {
        val myRef = Objects.getFirebaseInstance().getReference("DriverDetails")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                driver_modelList.clear()

                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(DriverDetail_Model::class.java)!!
                    driver_modelList.add(listDataRef)
                }
                if (pd != null && pd!!.isShowing)
                    pd!!.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
                Toast.makeText(context, "Unable to fetch data from Server", Toast.LENGTH_LONG)
                    .show()
                if (pd != null && pd!!.isShowing)
                    pd!!.dismiss()
            }
        })
    }

    fun getfreeDriverrData() {
        val myRef = Objects.getFirebaseInstance().getReference()
        val query = myRef.child("DriverDetails").orderByChild("assigned_status").equalTo("")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                freeDriverList.clear()
                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(DriverDetail_Model::class.java)!!
                    freeDriverList.add(listDataRef)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })
    }

    fun getVanData() {
        val myRef = Objects.getFirebaseInstance().getReference()
        val query = myRef.child("AddVan")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                vanList.clear()
                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(VanDetail_Model::class.java)!!
                    vanList.add(listDataRef)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })
    }

    fun getFreeVanData() {
        val myRef = Objects.getFirebaseInstance().getReference()
        val query = myRef.child("AddVan").orderByChild("assign_Status").equalTo("")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                freevanList.clear()
                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(VanDetail_Model::class.java)!!
                    freevanList.add(listDataRef)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })
    }


    fun showDialogTrackVan() {
        val dialogBuilder = AlertDialog.Builder(this!!.activity!!)
        val inflater = this!!.layoutInflater
        dialogBuilder.setCancelable(false)
        val dialogView = inflater.inflate(R.layout.dialog_track_van, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val van_Spinner = dialogView.findViewById(R.id.van_Spinner) as Spinner
        val btn_search = dialogView.findViewById(R.id.btn_search) as Button
        val btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog) as ImageButton

        var van_spinner_adapter: Adapter

        var driver_modelList_dummy = ArrayList<DriverDetail_Model>()
        driver_modelList_dummy.addAll(driver_modelList)

        // pickup and dropoff adapter will be same
        van_spinner_adapter = TrackVan_Spinner_Adapter(
            this!!.context!!, R.id.spinner_item_tv
            , driver_modelList_dummy
        )

        //set adapter to both spinner'S
        van_Spinner?.adapter = van_spinner_adapter

        van_Spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                l: Long
            ) {
                trackDriverbyVan.clear()
                if (driver_modelList.get(position).assigned_status.equals("riding")) // track usi ko kare gy jo gari chla rha hoga
                    trackDriverbyVan.add(driver_modelList.get(position))
                if (trackDriverbyVan.size <= 0) {
                    Toast.makeText(
                        context,
                        "No Van is riding or allocated to Track",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        })

        btn_search.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (trackDriverbyVan.size > 0) {
                    mainActivity!!.replaceFragment(Admin_TrackVans(), null)
                } else {
                    Toast.makeText(
                        context,
                        "No Van is riding or allocated to Track",
                        Toast.LENGTH_LONG
                    ).show()
                }
                alertDialog.dismiss()

            }

        })
        btn_closeDialog.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                alertDialog.dismiss()
            }

        })

        // alertDialog.show()
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        } else {
            alertDialog.show()
            alertDialog.getWindow()!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }


}
