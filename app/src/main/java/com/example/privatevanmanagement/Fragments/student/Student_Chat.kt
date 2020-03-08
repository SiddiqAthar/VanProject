package com.example.privatevanmanagement.Fragments.student

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privatevanmanagement.ChatModule.Model.ChatModel
import com.example.privatevanmanagement.ChatModule.UtilitiesClasses.UserDetails
import com.example.privatevanmanagement.ChatModule.UtilitiesClasses.Utils

import com.example.privatevanmanagement.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import java.util.HashMap

class Student_Chat : Fragment() {

    var rootView: View? = null

    internal var sendButton: ImageView? = null
    internal var messageArea: EditText? = null
//    internal var scrollView: ScrollView? = null
    internal var chatRecView: RecyclerView? = null

    internal var messageRef: DatabaseReference? = null
    internal var userRef: DatabaseReference? = null
    internal var type = false
    internal var chatwithEmail: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater!!.inflate(
            R.layout.fragment_student__chat, container,
            false
        )
        activity?.setTitle("Chat to Driver")



        sendButton = rootView?.findViewById(R.id.sendButton) as ImageView
        messageArea = rootView?.findViewById(R.id.messageArea) as EditText
//        scrollView = rootView?.findViewById(R.id.scrollView) as ScrollView
        chatRecView = rootView?.findViewById(R.id.chat_recycler_view) as RecyclerView

        val layoutManager = LinearLayoutManager(context)
        layoutManager.setStackFromEnd(true)
        chatRecView!!.setHasFixedSize(true)
        chatRecView!!.setLayoutManager(layoutManager)

/*
        messageRef = FirebaseDatabase.getInstance().getReference("/messages")
        userRef = FirebaseDatabase.getInstance().getReference("/users")
*/


        sendButton!!.setOnClickListener(View.OnClickListener {
            val messageText = messageArea!!.getText().toString()

            if (messageText != "") {
//                val map = HashMap<String, String>()
//                map["message"] = messageText
//                map["user"] = UserDetails.userID
//                UserDetails.chatRef.push().setValue(map)
                messageArea!!.setText("")
                messageArea!!.setHint("Message")
            }
        })

        return rootView

    }

    override fun onStart() {
        super.onStart()
        setAdapter()
/*
        valueEventListener(this!!.userRef!!, this!!.chatwithEmail!!)
*/

    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var chatMessage: TextView
        internal var userText: TextView
        internal var linearLayout: LinearLayout

        init {
            chatMessage = itemView.findViewById(R.id.text_chat) as TextView
            userText = itemView.findViewById(R.id.user_chat) as TextView
            linearLayout = itemView.findViewById(R.id.lin_lay) as LinearLayout
        }

        fun setChatMessage(message: String) {
            chatMessage.text = message
        }


        fun setUserText(userName: String, type: Boolean) {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.FILL_PARENT
            )
            if (type) {
                //current logged in user
                params.gravity = Gravity.END
                userText.text = "You"
                linearLayout.gravity = Gravity.END
                linearLayout.setBackgroundResource(R.drawable.bubble_in)
            } else {
                params.gravity = Gravity.START
                userText.text = UserDetails.chatwithEmail
                linearLayout.gravity = Gravity.START
                linearLayout.setBackgroundResource(R.drawable.bubble_out)
            }
            linearLayout.layoutParams = params
        }
    }

    fun valueEventListener(dbref: DatabaseReference, checkChild: String) {
        messageRef = FirebaseDatabase.getInstance().getReference("/messages")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    if (child.value == checkChild) {
                        UserDetails.chatwithID = child.key
                        val type1: String
                        val type2: String
                        type1 = UserDetails.userID + "_" + UserDetails.chatwithID
                        type2 = UserDetails.chatwithID + "_" + UserDetails.userID

                        messageRef!!.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val child1 = dataSnapshot.child(type1)
                                val child2 = dataSnapshot.child(type2)
                                if (child1.exists()) {
                                    UserDetails.userType = "type1"
                                    UserDetails.chatRef =
                                        FirebaseDatabase.getInstance().getReference("/messages")
                                            .child(type1)
                                    //chatRef2 = messageRef.child(type1);
                                } else if (child2.exists()) {
                                    UserDetails.userType = "type2"
                                    UserDetails.chatRef =
                                        FirebaseDatabase.getInstance().getReference("/messages")
                                            .child(type2)

                                } else {
                                    UserDetails.userType = "type1"
                                    messageRef!!.child(type1).setValue("none")
                                    UserDetails.chatRef =
                                        FirebaseDatabase.getInstance().getReference("/messages")
                                            .child(type1)
                                    //chatRef2 = messageRef.child(type2);
                                }
                                if (UserDetails.chatRef != null) {
                                    val firebaseRecyclerAdapter =
                                        object : FirebaseRecyclerAdapter<ChatModel, ChatViewHolder>(
                                            ChatModel::class.java,
                                            R.layout.chat_row,
                                            ChatViewHolder::class.java,
                                            UserDetails.chatRef
                                        ) {
                                            override fun populateViewHolder(
                                                viewHolder: ChatViewHolder,
                                                model: ChatModel,
                                                position: Int
                                            ) {
                                                //                                                    final String chatKey = getRef(position).getKey();
                                                viewHolder.setChatMessage(model.getMessage())
                                                type = model.getUser() == UserDetails.userID
                                                viewHolder.setUserText(model.getUser(), type)

                                            }
                                        }
                                    chatRecView?.setAdapter(firebaseRecyclerAdapter)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun setAdapter()
    {

/*
        var list:ArrayList<ChatModel>
        list= ArrayList()
        list.add(ChatModel())
*/

         val firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<ChatModel, ChatViewHolder>(
                ChatModel::class.java,
                R.layout.chat_row,
                ChatViewHolder::class.java,
                null
            )
            {
                override fun populateViewHolder(
                    viewHolder: ChatViewHolder,
                    model: ChatModel,
                    position: Int
                ) {
                    //                                                    final String chatKey = getRef(position).getKey();
                    viewHolder.setChatMessage(model.getMessage())
                    type = model.getUser() == UserDetails.userID
                    viewHolder.setUserText(model.getUser(), type)

                }
            }
        chatRecView?.setAdapter(firebaseRecyclerAdapter)

    }


}


