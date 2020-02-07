package com.example.privatevanmanagement.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterationActivity : BaseActivity() {
//    var mAuth: FirebaseAuth? = null
    var fieldEmail: EditText? = null
    var fieldPassword: EditText? = null
    var CreateAccountButton: Button? = null
    val user: Objects.UserID = Objects.UserID()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)
//        mAuth = FirebaseAuth.getInstance();
        CreateAccountButton = findViewById(R.id.btn_Registeration) as Button
        fieldEmail = findViewById(R.id.et_email) as EditText
        fieldPassword = findViewById(R.id.et_password) as EditText
        CreateAccountButton?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                createAccount(fieldEmail!!.text.toString(), fieldPassword!!.text.toString())
            }
        })
    }

    private fun createAccount(email: String, password: String) {
        if (!validateForm()) {
            return
        }


        Objects.getInstance()!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    val user = mAuth!!.currentUser
                    var user_id = Objects.getInstance()!!.currentUser?.uid.toString()
                    user.Globaluser_ID = user_id

//                    startActivity(Intent(this@RegisterationActivity, MainActivity::class.java))
                    // updateUI(user)
                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateUI(null)
                }
//                hideProgressBar()
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail!!.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail!!.error = "Required."
            valid = false
        } else {
            fieldEmail!!.error = null
        }

        val password = fieldPassword!!.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword!!.error = "Required."
            valid = false
        } else {
            fieldPassword!!.error = null
        }

        return valid
    }


}
