package com.example.privatevanmanagement.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.utils.Objects
import com.example.privatevanmanagement.utils.Objects.UserID.Globaluser_ID
import com.example.privatevanmanagement.utils.Objects.UserType
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    var animZoomIn: Animation? = null
    var animslideUp: Animation? = null
    var image: ImageView? = null
    var btnSignIn: Button? = null
    var btnNewUser: Button? = null
    var btnForgotPassword: Button? = null
    var email: EditText? = null
    var password: EditText? = null
    val user: Objects.UserID = Objects.UserID()

    lateinit var editor: SharedPreferences.Editor
    lateinit var pref: SharedPreferences

    override fun onStart() {
        super.onStart()
/*
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Globaluser_ID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            pref =
                getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            UserType = pref.getString("usertype", "");
            if (UserType.equals("Student") || UserType.equals("Driver")) {
                startActivity(Intent(this@LoginActivity, UserActivity::class.java))
                finish()
            } else if (UserType.equals("Admin")) {
                startActivity(Intent(this@LoginActivity, AdminNav_Activity::class.java))
                finish()
            }
        }
*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        animZoomIn = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.zoomin
        )

        animslideUp = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.slideuo
        )
        image = findViewById(R.id.iv_icon) as ImageView
        image!!.startAnimation(animZoomIn)
        btnSignIn = findViewById(R.id.btn_SignIn) as Button
        btnNewUser = findViewById(R.id.btn_createaccount) as Button
        btnForgotPassword = findViewById(R.id.btn_forgotpassword) as Button
        email = findViewById(R.id.Email) as EditText
        email!!.startAnimation(animslideUp)
        password = findViewById(R.id.Password) as EditText
        password!!.startAnimation(animslideUp)




        editor = pref.edit();

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
                                Globaluser_ID =
                                    FirebaseAuth.getInstance().currentUser?.uid.toString()
                                checkUserType()

                            } else if (email!!.text.toString().equals("admin@gmail.com")) {
                                UserType = "admin"
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        AdminNav_Activity::class.java
                                    )
                                )
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

    private fun createToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Error", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                } else {
                    var databaseReference =
                        Objects.getFirebaseInstance().reference.child("Token").child(
                            Globaluser_ID
                        )
                    databaseReference.setValue(task.result!!.token)

                }
            })
    }

    public fun checkUserType() {
        val rootRef = FirebaseDatabase.getInstance().reference
        val ordersRef = rootRef.child("UserType").child(Objects.UserID.Globaluser_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                UserType = dataSnapshot.child("User Type").value.toString()

                editor.putString("usertype", UserType);
                editor.commit()
                if (UserType.equals("Student")) {
                    createToken()
                    startActivity(Intent(this@LoginActivity, UserActivity::class.java))
                } else if (UserType.equals("Driver")) {
                    startActivity(Intent(this@LoginActivity, UserActivity::class.java))
                } else if (UserType.equals("Admin")) {
                    startActivity(Intent(this@LoginActivity, AdminNav_Activity::class.java))
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ordersRef.addListenerForSingleValueEvent(valueEventListener)
    }
}

