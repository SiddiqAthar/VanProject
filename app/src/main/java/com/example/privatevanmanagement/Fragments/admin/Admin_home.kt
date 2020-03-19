package com.example.privatevanmanagement.Fragments.admin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.NavDrawer
import com.example.privatevanmanagement.adapters.Adapter_manageFeeextends
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.student_modelList
import com.example.privatevanmanagement.utils.SendNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import android.app.ProgressDialog
import com.example.privatevanmanagement.models.VanDetail_Model
import com.example.privatevanmanagement.utils.Objects.vanList


class Admin_home : Fragment(), View.OnClickListener {


    var mContext: Context? = null
    var mainActivity: NavDrawer? = null

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

        if (student_modelList.isEmpty()) {
            pd = ProgressDialog(context)
            pd!!.setMessage("Fetching Data From Server")
            pd!!.setCancelable(false)
            pd!!.show()
            getStudentList()
            getVanData()

        }
        init(rootView)

        return rootView


    }

    private fun init(rootView: View?) {
        mainActivity = activity as NavDrawer

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
            mainActivity!!.ChangeManagementFragment(Schedule_Van(), null)
        } else if (v?.id == R.id.add_TrackVan) {
            mainActivity!!.ChangeManagementFragment(Admin_TrackVans(), null)
        } else if (v?.id == R.id.add_announcmnet) {
            showDialogMakeAnnouncment()
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.show()
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
                pd!!.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
                Toast.makeText(context, "Unable to fetch data from Server", Toast.LENGTH_LONG)
                    .show()
                pd!!.dismiss()
            }
        })
    }
      fun getVanData() {
        val myRef = Objects.getFirebaseInstance().getReference("AddVan")
        myRef.addValueEventListener(object : ValueEventListener {
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

}
