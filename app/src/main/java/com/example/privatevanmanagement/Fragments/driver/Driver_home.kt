package com.example.privatevanmanagement.Fragments.driver

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.privatevanmanagement.ChatModule.ShowActivities.Driver_chat_list
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.UserActivity
import com.example.privatevanmanagement.models.StudentDetail_Model
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.*
import com.example.privatevanmanagement.utils.SendNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class Driver_home : Fragment(), View.OnClickListener {


    var mContext: Context? = null
    var mainActivity: UserActivity? = null

    var driver_comingTrip: CardView? = null
    var driver_chat: CardView? = null
    var driver_announcement: CardView? = null
    var pd: ProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews()
        }

        val rootView = inflater!!.inflate(R.layout.fragment_driver_home, container, false)
        mContext = rootView.context
        activity?.setTitle("PVM")

        pd = ProgressDialog(context)
        pd!!.setMessage("Loading Trip Data")
        pd!!.setCancelable(false)

        init(rootView)

        return rootView


    }

    private fun init(rootView: View?) {
        mainActivity = activity as UserActivity

        driver_comingTrip = rootView?.findViewById(R.id.driver_comingTrip) as CardView
        driver_chat = rootView?.findViewById(R.id.driver_chat) as CardView
        driver_announcement = rootView?.findViewById(R.id.driver_announcment) as CardView

        driver_comingTrip!!.setOnClickListener(this)
        driver_chat!!.setOnClickListener(this)
        driver_announcement!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.driver_comingTrip) {
            pd?.show()
            getAllocatedStudentList()
//             mainActivity!!.replaceFragmentUserActivity(Driver_Coming_Trip(), null)
        } else if (v?.id == R.id.driver_chat) {
            startActivity(Intent(context, Driver_chat_list::class.java))
        } else if (v?.id == R.id.driver_announcment) {
            showDialogMakeAnnouncment()
        }
    }

    fun getAllocatedStudentList() {
        val myRef = Objects.getFirebaseInstance().getReference()
        val query = myRef.child("StudentDetails").orderByChild("driver_id")
            .equalTo(Objects.UserID.Globaluser_ID)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                student_modelList.clear()
                for (postSnapshot in snapshot.children) {
                    val listDataRef = postSnapshot.getValue(StudentDetail_Model::class.java)!!
                    if (postSnapshot.child("arrival").value!!.equals("")
                        || postSnapshot.child("arrival").value!!.equals("dropped"))// agr ar    rival status abi tk null hai to jae driver
                    {
                        student_modelList.add(listDataRef)
                    }
                    /*  // to reset all data we use another dummy list name as sorted
                      else if (!postSnapshot.child("arrival").value!!.equals(""))// agr arrival status sirf empty na ho tooooo
                      {
                          sortedList.add(listDataRef)
                      }*/
                }

                pd!!.dismiss()
                mainActivity!!.replaceFragmentUserActivity(Driver_Track(), null)


                if (student_modelList.size > 0) {
                    // agr ride already started hai to. . . list get kary or nxt frag pe move ho jae
                    if (Objects.getDriverDetailInstance().assigned_status.equals("riding")) {
                        pd!!.dismiss()
                        mainActivity!!.replaceFragmentUserActivity(Driver_Track(), null)
                    } else {
                        pd!!.dismiss()
                        mainActivity!!.replaceFragmentUserActivity(Driver_Coming_Trip(), null)
                    }
                } else if (student_modelList.size == 0) {
                    pd!!.dismiss()
                    mainActivity!!.replaceFragmentUserActivity(Driver_Coming_Trip(), null)
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                System.out.println("The read failed: " + databaseError.getMessage())
            }
        })

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

    private fun sendMessage(toString: String) {
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
