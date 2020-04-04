package com.example.privatevanmanagement.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.LoginActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePassword : Fragment() {

    var rootView: View? = null


    var et_currentPswd: EditText? = null
    var et_newPswd: EditText? = null
    var et_confirmPswd: EditText? = null
    var btn_ChangePassword: Button? = null


    var pd: ProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater!!.inflate(
            R.layout.fragment_change_password, container,
            false
        )
        activity?.setTitle("Chnage Password")

        init(rootView)

        return rootView
    }

    private fun init(rootView: View?) {
        btn_ChangePassword = rootView?.findViewById(R.id.btn_ChangePassword) as Button
        et_currentPswd = rootView?.findViewById(R.id.et_currentPswd) as EditText
        et_newPswd = rootView?.findViewById(R.id.et_newPswd) as EditText
        et_confirmPswd = rootView?.findViewById(R.id.et_confirmPswd) as EditText


        btn_ChangePassword!!.setOnClickListener {
            if (validateForm()) {
                pd = ProgressDialog(context)
                pd!!.setMessage("Please wait")
                pd!!.setCancelable(false)
                pd!!.show()

                val user = FirebaseAuth.getInstance().currentUser
                val credential = EmailAuthProvider
                    .getCredential(user?.email.toString(), et_currentPswd!!.text.toString())
                user?.reauthenticate(credential)
                    ?.addOnCompleteListener {

                        if (it.isSuccessful) {

                            if (et_newPswd!!.text.toString().equals(et_confirmPswd!!.text.toString())) {
                                user.updatePassword(et_newPswd!!.text.toString())
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Password Change Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            pd!!.dismiss()
                                            activity!!.finishAffinity()
                                            startActivity(
                                                Intent(
                                                    context,
                                                    LoginActivity::class.java
                                                )
                                            )
                                            FirebaseAuth.getInstance().signOut();
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Unable to Change Password",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            pd!!.dismiss()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Confirmed Password Does't match",
                                    Toast.LENGTH_SHORT
                                ).show()
                                pd!!.dismiss()
                            }
                        }

                        else {
                            Toast.makeText(
                                context,
                                "Current Password does't match ",
                                Toast.LENGTH_SHORT
                            ).show()
                            pd!!.dismiss()
                        }
                    }
            }
        }

    }

    private fun validateForm(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(et_currentPswd!!.text.toString())) {
            et_currentPswd!!.error = "Current Password Required"
            valid = false
        } else {
            et_currentPswd!!.error = null
        }
        if (TextUtils.isEmpty(et_newPswd!!.text.toString())) {
            et_newPswd!!.error = "New Password Required"
            valid = false
        } else {
            et_newPswd!!.error = null
        }
        if (TextUtils.isEmpty(et_confirmPswd!!.text.toString())) {
            et_confirmPswd!!.error = "Confirm Password Required"
            valid = false
        } else {
            et_confirmPswd!!.error = null
        }
        return valid
    }
}
