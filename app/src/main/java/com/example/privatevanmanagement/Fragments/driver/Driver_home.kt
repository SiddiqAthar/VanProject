package com.example.privatevanmanagement.Fragments.driver

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
import com.example.privatevanmanagement.models.DriverDetail_Model
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.driver_modelList
import com.example.privatevanmanagement.utils.SendNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class Driver_home : Fragment(), View.OnClickListener {


    var mContext: Context? = null
    var mainActivity: NavDrawer? = null

    var driver_comingTrip: CardView? = null
    var driver_chat: CardView? = null
    var driver_announcement: CardView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews()
        }
        val rootView = inflater!!.inflate(R.layout.fragment_driver_home, container, false)
        mContext = rootView.context

        if(driver_modelList.isEmpty())
            getDriverList()

        init(rootView)

        return rootView


    }

    private fun init(rootView: View?) {
        mainActivity = activity as NavDrawer

        driver_comingTrip = rootView?.findViewById(R.id.driver_comingTrip) as CardView
        driver_chat = rootView?.findViewById(R.id.driver_chat) as CardView
        driver_announcement = rootView?.findViewById(R.id.driver_announcment) as CardView

        driver_comingTrip!!.setOnClickListener(this)
        driver_chat!!.setOnClickListener(this)
        driver_announcement!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.driver_comingTrip) {
            mainActivity!!.replaceFragment(Driver_Coming_Trip(), null)
        } else if (v?.id == R.id.driver_chat) {
//            mainActivity!!.ChangeManagementFragment(AddDriver(), null)
        } else if (v?.id == R.id.driver_announcment) {
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
            var msg = make_Annoncment.text.toString()
            if (!msg.isNullOrEmpty()) {
                sendMessage(msg)
                //add data here on firebase and send notification
                Toast.makeText(activity, "Notification Sending", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
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


    fun getDriverList() {
        val myRef = Objects.getFirebaseInstance().getReference("DriverDetails")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                driver_modelList.clear()

                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(DriverDetail_Model::class.java)!!
                    driver_modelList.add(listDataRef)
                 }
              }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
                Toast.makeText(context, "Unable to fetch data from Server", Toast.LENGTH_LONG)
                    .show()
             }
        })
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





}
