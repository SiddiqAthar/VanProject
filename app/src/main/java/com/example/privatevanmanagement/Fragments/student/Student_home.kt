package com.example.privatevanmanagement.Fragments.student

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
import java.util.ArrayList


class Student_home : Fragment(), View.OnClickListener {


    var mContext: Context? = null
    var mainActivity: NavDrawer? = null

    var stud_setTime: CardView? = null
    var stud_editTime: CardView? = null
    var stud_payFee: CardView? = null
    var stud_trackVan: CardView? = null
    var stud_sendComplaint: CardView? = null
    var stud_chatDriver: CardView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews()
        }
        val rootView = inflater!!.inflate(R.layout.fragment_student_home, container, false)
        mContext = rootView.context

        init(rootView)

        return rootView


    }

    private fun init(rootView: View?) {
        mainActivity = activity as NavDrawer

        stud_setTime = rootView?.findViewById(R.id.stud_setTime) as CardView
        stud_editTime = rootView?.findViewById(R.id.stud_editTime) as CardView
        stud_payFee = rootView?.findViewById(R.id.stud_payFee) as CardView
        stud_trackVan = rootView?.findViewById(R.id.stud_trackVan) as CardView
        stud_sendComplaint = rootView?.findViewById(R.id.stud_sendComplaint) as CardView
        stud_chatDriver = rootView?.findViewById(R.id.stud_chatDriver) as CardView

        stud_setTime!!.setOnClickListener(this)
        stud_editTime!!.setOnClickListener(this)
        stud_payFee!!.setOnClickListener(this)
        stud_trackVan!!.setOnClickListener(this)
        stud_sendComplaint!!.setOnClickListener(this)
        stud_chatDriver!!.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.stud_setTime) {
            showDialogSetTime()
        } else if (v?.id == R.id.stud_editTime) {
            showDialogEditTime()
        } else if (v?.id == R.id.stud_payFee) {
            mainActivity!!.replaceFragment(Student_payFee(), null)
        } else if (v?.id == R.id.stud_trackVan) {
            mainActivity!!.replaceFragment(Student_TrackVans(), null)
        } else if (v?.id == R.id.stud_sendComplaint) {
            showDialogMakeComplaint()
        } else if (v?.id == R.id.stud_chatDriver) {
//            mainActivity!!.replaceFragment(Student_Chat(), null)
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

    fun showDialogMakeComplaint() {
        val dialogBuilder = AlertDialog.Builder(this!!.activity!!)
        val inflater = this!!.layoutInflater
        dialogBuilder.setCancelable(false)
        val dialogView = inflater.inflate(R.layout.dialog_register_complaint, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val et_registerComplaint = dialogView.findViewById(R.id.et_registerComplaint) as EditText
        val btn_sendNotficaion = dialogView.findViewById(R.id.btn_submitComplaint) as Button
        val btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog) as ImageButton

        btn_sendNotficaion.setOnClickListener(View.OnClickListener {

            //here send complaint to Admin after that

            Toast.makeText(context, "Complaint register", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        })

        btn_closeDialog.setOnClickListener(
            View.OnClickListener
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

    fun showDialogSetTime() {
        val dialogBuilder = AlertDialog.Builder(this!!.activity!!)
        val inflater = this!!.layoutInflater
        dialogBuilder.setCancelable(false)
        val dialogView = inflater.inflate(R.layout.dialog_set_pick_time, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val pickUp_Spinner = dialogView.findViewById(R.id.pickUp_Spinner) as Spinner
        val dropOff_Spinner = dialogView.findViewById(R.id.dropOff_Spinner) as Spinner

        val btn_setTiming = dialogView.findViewById(R.id.btn_setTiming) as Button
        val btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog) as ImageButton

        var ArrayList_pickup_shift: ArrayList<String>
        var ArrayList_dropoff_shift: ArrayList<String>
        var shift_spinner_adapter: Adapter

        // set array list of timing here to Spinner
        // 1 in Pickup
        // 2 in drop off


        //dumy shift data
        ArrayList_pickup_shift = ArrayList<String>()
        ArrayList_pickup_shift!!.add("Shift 1")
        ArrayList_pickup_shift!!.add("Shift 2")
        ArrayList_pickup_shift!!.add("Shift 3")

        // pickup and dropoff adapter will be same
        shift_spinner_adapter = ArrayAdapter<String>(
            this!!.context!!, android.R.layout.simple_spinner_item,
            ArrayList_pickup_shift!!
        )
        shift_spinner_adapter?.setDropDownViewResource(R.layout.spinner_item)

        //set adapter to both spinner'S
        pickUp_Spinner?.adapter = shift_spinner_adapter
        dropOff_Spinner?.adapter = shift_spinner_adapter

        btn_setTiming.setOnClickListener(View.OnClickListener
        {
            // add pickup and drop off vale to DB here, then change close dialog
            Toast.makeText(context, "Set Time Successfully", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        })

        btn_closeDialog.setOnClickListener(
            View.OnClickListener
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

    fun showDialogEditTime() {
        val dialogBuilder = AlertDialog.Builder(this!!.activity!!)
        val inflater = this!!.layoutInflater
        dialogBuilder.setCancelable(false)
        val dialogView = inflater.inflate(R.layout.dialog_edit_pick_time, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val edit_pickUp_Spinner = dialogView.findViewById(R.id.edit_pickUp_Spinner) as Spinner
        val edit_dropOff_Spinner = dialogView.findViewById(R.id.edit_dropOff_Spinner) as Spinner

        val edit_btn_setTiming = dialogView.findViewById(R.id.btn_edit_setTiming) as Button
        val btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog) as ImageButton

        var ArrayList_pickup_shift: ArrayList<String>
        var ArrayList_dropoff_shift: ArrayList<String>
        var shift_spinner_adapter: Adapter

        // set array list of timing here to Spinner
        // 1 in Pickup
        // 2 in drop off


        //dumy shift data
        ArrayList_pickup_shift = ArrayList<String>()
        ArrayList_pickup_shift!!.add("Shift 1")
        ArrayList_pickup_shift!!.add("Shift 2")
        ArrayList_pickup_shift!!.add("Shift 3")

        // pickup and dropoff adapter will be same
        shift_spinner_adapter = ArrayAdapter<String>(
            this!!.context!!, android.R.layout.simple_spinner_item,
            ArrayList_pickup_shift!!
        )
        shift_spinner_adapter?.setDropDownViewResource(R.layout.spinner_item)

        //set adapter to both spinner'S
        edit_pickUp_Spinner?.adapter = shift_spinner_adapter
        edit_dropOff_Spinner?.adapter = shift_spinner_adapter

        edit_btn_setTiming.setOnClickListener(View.OnClickListener
        {
            // edit pickup and drop off vale to DB here, then change close dialog
            Toast.makeText(context, "Edit Time Successfully", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        })

        btn_closeDialog.setOnClickListener(
            View.OnClickListener
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
