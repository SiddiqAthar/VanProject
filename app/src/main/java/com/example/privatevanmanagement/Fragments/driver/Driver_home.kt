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
            showDialogMakeAnnouncment()        }
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


}
