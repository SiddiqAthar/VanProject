package com.example.privatevanmanagement.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog

import com.example.privatevanmanagement.R
import androidx.appcompat.app.AppCompatActivity



class HomeFragment : Fragment() {
    var mContext: Context? = null
    lateinit var btn_makeAnnouncment: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        if (container != null) {
            container.removeAllViews()
        }
        val rootView = inflater!!.inflate(R.layout.fragment_home, container, false)
        mContext = rootView.context


        btn_makeAnnouncment = rootView.findViewById(R.id.btn_makeAnnouncment)

        btn_makeAnnouncment.setOnClickListener(View.OnClickListener {
            showDialogMakeAnnouncment()
        })

        return rootView

    }



    fun showDialogMakeAnnouncment() {
        val dialogBuilder = AlertDialog.Builder(this!!.mContext!!)
        val inflater = activity!!.layoutInflater
        dialogBuilder.setCancelable(false)
        val dialogView = inflater.inflate(R.layout.dialog_register_complaint, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val btn_sendNotficaion = dialogView.findViewById(R.id.btn_submitComplaint) as Button
        val btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog) as ImageButton

        btn_sendNotficaion.setOnClickListener(View.OnClickListener { })

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
