package com.example.privatevanmanagement.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.UserID.Globaluser_ID
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var user_id: String
    var btnSignIn: Button? = null
    var btnNewUser: Button? = null
    var btnForgotPassword: Button? = null
    var email: EditText? = null
    var password: EditText? = null
    val user: Objects.UserID = Objects.UserID()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignIn = findViewById(R.id.btn_SignIn) as Button
        btnNewUser = findViewById(R.id.btn_createaccount) as Button
        btnForgotPassword = findViewById(R.id.btn_forgotpassword) as Button
        email = findViewById(R.id.Email) as EditText
        password = findViewById(R.id.Password) as EditText

        mAuth = FirebaseAuth.getInstance()




        btnSignIn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {


                if (!TextUtils.isEmpty(email?.text.toString()) && !TextUtils.isEmpty(password?.text.toString())) {
                    mAuth.signInWithEmailAndPassword(
                        email?.text.toString(),
                        password?.text.toString()
                    )
                        .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                            if (task.isSuccessful) {
                                user_id = FirebaseAuth.getInstance().currentUser?.uid.toString()
                                 Globaluser_ID = user_id
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            } else {
                                Toast.makeText(this@LoginActivity, "Error Login", Toast.LENGTH_LONG)
                                    .show()
                            }
                        })
                }

            }
        })
        btnNewUser?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@LoginActivity, RegisterationActivity::class.java))
            }
        })
        btnForgotPassword?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
            }
        })
    }

}
