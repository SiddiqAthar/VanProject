package com.example.privatevanmanagement.Fragments.student

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.privatevanmanagement.R
import com.example.privatevanmanagement.activities.UserActivity
import com.example.privatevanmanagement.utils.Objects


class Student_payFee : Fragment() {

    var mContext: Context? = null

    var StudentCardNumber: EditText? = null
    var StudentCardDate: EditText? = null
    var StudentCardCvv: EditText? = null
    var StudentCardAmmount: EditText? = null
    var btn_StudentPayFee: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_student_pay_fee, container, false)
        mContext = rootView.context

        (activity as AppCompatActivity).supportActionBar!!.setTitle("Pay Fee");

        init(rootView)

        return rootView
    }

    private fun init(rootView: View?) {
        StudentCardNumber = rootView!!.findViewById(R.id.StudentCardNumber)
        StudentCardDate = rootView!!.findViewById(R.id.StudentCardDate)
        StudentCardCvv = rootView!!.findViewById(R.id.StudentCardCvv)
        StudentCardAmmount = rootView!!.findViewById(R.id.StudentCardAmmount)
        btn_StudentPayFee = rootView!!.findViewById(R.id.btn_StudentPayFee)

        btn_StudentPayFee?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (validate()) {
                    if (Objects.getStudentDetailInstance().fee_status.equals("Un-Paid")) {
                        //if status is not paid, then
                        //add Fee payment data to firebase, set status from UnPid to Paid
                        Toast.makeText(context, "Fee Payed Successfully", Toast.LENGTH_SHORT).show()

                        val ref = Objects.getFirebaseInstance().getReference("StudentDetails")
                            .child(Objects.UserID.Globaluser_ID).child("fee_status")

/*
                        val map: HashMap<String, Any> = hashMapOf(
                            "fee_status" to "Paid"
//                            e.t.c. to UPDATE BULK OF DATA


*/
/*                            "email" to email
 *//*

                        )
*/

                        ref.setValue("Paid")

                        var mainActivity: UserActivity = activity as UserActivity
                        mainActivity.replaceFragmentUserActivity(Student_home(), null)
                    } else {
                        Toast.makeText(context, "You already Payed", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        })

    }

    public fun validate(): Boolean {
        if (StudentCardNumber?.text.toString().length < 19) {
            StudentCardNumber?.requestFocus()
            StudentCardNumber?.error = "Enter Valid Number"
            return false
        }
        if (StudentCardDate?.text.toString().length < 4) {
            StudentCardDate?.requestFocus()
            StudentCardDate?.error = "Enter Valid Date"
            return false
        }
        if (StudentCardCvv?.text.toString().length < 3) {
            StudentCardCvv?.requestFocus()
            StudentCardCvv?.error = "Enter Valid CVV"
            return false
        }
        return true
    }
}
