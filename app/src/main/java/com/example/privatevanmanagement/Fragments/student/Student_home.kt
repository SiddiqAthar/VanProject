package com.example.privatevanmanagement.Fragments.student

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.AdminNav_Activity
import com.example.privatevanmanagement.activities.UserActivity
import com.example.privatevanmanagement.adapters.Spinner_Adapter
import com.example.privatevanmanagement.models.Shift_Model
import com.example.privatevanmanagement.utils.MyInterface
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.shift_list
import java.util.*


class Student_home : Fragment(), View.OnClickListener {


    var mContext: Context? = null
    var mainActivity: UserActivity? = null

    var stud_setTime: CardView? = null
    var stud_editTime: CardView? = null
    var stud_payFee: CardView? = null
    var stud_trackVan: CardView? = null
    var stud_sendComplaint: CardView? = null
    var stud_chatDriver: CardView? = null

    var shift: String? = null
    var drop: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews()
        }
        val rootView = inflater!!.inflate(R.layout.fragment_student_home, container, false)
        mContext = rootView.context
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Van Management")

        init(rootView)

        return rootView
    }

    private fun init(rootView: View?) {
        mainActivity = activity as UserActivity

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
            showDialogSetTime("Set")
        } else if (v?.id == R.id.stud_editTime) {
            showDialogSetTime("Update")
        } else if (v?.id == R.id.stud_payFee) {
            mainActivity!!.replaceFragmentUserActivity(Student_payFee(), null)
        } else if (v?.id == R.id.stud_trackVan) {
            mainActivity!!.replaceFragmentUserActivity(Student_TrackVans(), null)
        } else if (v?.id == R.id.stud_sendComplaint) {
            showDialogMakeComplaint()
        } else if (v?.id == R.id.stud_chatDriver) {
//            mainActivity!!.replaceFragmentUserActivity(Student_Chat(), null)
        }
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
            if (!et_registerComplaint.text.toString().isNullOrEmpty()) {
                val newPost2 =
                    Objects.getFirebaseInstance().reference.child("Complaints")
                newPost2.child(Objects.studentDetail_model.student_id)
                    .child(Objects.studentDetail_model.student_name)
                    .setValue(et_registerComplaint.text.toString())

                Toast.makeText(context, "Complaint register", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                Toast.makeText(context, "Complaint field is Empty", Toast.LENGTH_SHORT).show()
            }
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

    fun showDialogSetTime(msg: String) {
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
        var shift_spinner_adapter: Adapter

        // pickup and dropoff adapter will be same
        shift_spinner_adapter = Spinner_Adapter(
            this!!.context!!, R.id.spinner_item_tv, R.id.spinner_item_tv,
            shift_list as ArrayList<Shift_Model>?
        )

        //set adapter to both spinner'S
        pickUp_Spinner?.adapter = shift_spinner_adapter
        dropOff_Spinner?.adapter = shift_spinner_adapter

        pickUp_Spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                position: Int,
                l: Long
            ) {
                shift = shift_list!!.get(position).shift_name.toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        })


        dropOff_Spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                drop = shift_list!!.get(position).shift_name.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })


        btn_setTiming.setOnClickListener(View.OnClickListener
        {
            // add pickup and drop off vale to DB here, then change close dialog

            val ref = Objects.getFirebaseInstance().reference.child("StudentDetails")
                .child(Objects.UserID.Globaluser_ID)
            val updates = HashMap<String, Any>()
            updates["shift_time"] = shift.toString()
            updates["drop_time"] = drop.toString()

            if (!shift.equals(drop) && updates.size > 0) {
                ref.updateChildren(updates)
                Toast.makeText(context, msg + " Time Successfully", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                Toast.makeText(context, "Pick and Drop time can't be Same", Toast.LENGTH_SHORT)
                    .show()
            }
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
